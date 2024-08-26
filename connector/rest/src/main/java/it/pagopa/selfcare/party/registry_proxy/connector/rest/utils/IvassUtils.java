package it.pagopa.selfcare.party.registry_proxy.connector.rest.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.IvassDataTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class IvassUtils {

    public byte[] extractSingleFileFromZip(byte[] zipBytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipBytes);
            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {
            ZipEntry entry = zipInputStream.getNextEntry();

            if (entry != null) {
                return toByteArray(entry, zipInputStream);
            } else {
                throw new IOException("No entries found in the zip file");
            }
        } catch (IOException e) {
            log.debug("Error extracting file from zip", e);
            return new byte[0];
        }
    }

    private static byte[] toByteArray(ZipEntry zipEntry, ZipInputStream zipInputStream) throws IOException {
        int totalSizeEntry = 0;
        int THRESHOLD_SIZE = 100000000; // 100 MB
        double THRESHOLD_RATIO = 10; // 10 times the compressed size


        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = zipInputStream.read(buffer)) != -1) {
                totalSizeEntry += length;

                // Check the compression ratio of the extracted file (security reasons)
                double compressionRatio = (double) totalSizeEntry / zipEntry.getCompressedSize();
                if(compressionRatio > THRESHOLD_RATIO) {
                    log.error("Compression ratio exceeds the maximum allowed limit of " + THRESHOLD_RATIO);
                    return new byte[0];
                }

                // Check if the extracted file size exceeds the maximum allowed limit (security reasons)
                if(totalSizeEntry > THRESHOLD_SIZE) {
                    log.error("Extracted file size exceeds the maximum allowed limit of " + THRESHOLD_SIZE + " bytes");
                    return new byte[0];
                }

                byteArrayOutputStream.write(buffer, 0, length);
            }

            return byteArrayOutputStream.toByteArray();
        }
    }

    public List<InsuranceCompany> readCsv(byte[] csv) {
        List<IvassDataTemplate> companies = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv)))) {
            CsvToBean<IvassDataTemplate> csvToBean = new CsvToBeanBuilder<IvassDataTemplate>(reader)
                    .withType(IvassDataTemplate.class)
                    .withSeparator(';')
                    .build();
            companies = csvToBean.parse();
        } catch (Exception e) {
            log.error("Impossible to acquire data for IVASS. Error: {}", e.getMessage(), e);
        }
        return new ArrayList<>(companies);
    }

    public byte[] manageUTF8BOM(byte[] csv) {
        // Check if the csv has the UTF-8 BOM and remove it
        if (csv.length > 3 && csv[0] == (byte) 0xEF && csv[1] == (byte) 0xBB && csv[2] == (byte) 0xBF){
            csv = Arrays.copyOfRange(csv, 3, csv.length);
        }
        return csv;
    }
}
