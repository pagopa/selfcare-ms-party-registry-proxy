package it.pagopa.selfcare.party.connector.azure_storage;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.connector.azure_storage.model.IvassDataTemplate;
import it.pagopa.selfcare.party.registry_proxy.connector.api.FileStorageConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IvassDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.ResourceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IvassDataConnectorImpl implements IvassDataConnector {

    private final FileStorageConnector fileStorageConnector;
    private final String sourceFilename;

    public IvassDataConnectorImpl(@Value("${blobStorage.ivass.filename}") String ivassCsvFileName,
                                  FileStorageConnector fileStorageConnector) {
        this.fileStorageConnector = fileStorageConnector;
        this.sourceFilename = ivassCsvFileName;
    }

    @Override
    public List<InsuranceCompany> getInsurances() {
        log.trace("getInsurances start");
        List<InsuranceCompany> companies = new ArrayList<>();
        try {
            final ResourceResponse resourceResponse = fileStorageConnector.getFile(sourceFilename);
            Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resourceResponse.getData())));
            CsvToBean<InsuranceCompany> csvToBean = new CsvToBeanBuilder<InsuranceCompany>(reader)
                    .withType(IvassDataTemplate.class)
                    .withSeparator(';')
                    .build();
            companies = csvToBean.parse();
            reader.close();
        } catch (Exception e) {
            log.error("Impossible to acquire data for IVASS. Error: {}", e.getMessage(), e);
        }
        log.debug("getInsurances result = {}", companies);
        log.trace("getInsurances end");
        return companies
                .stream()
                .filter(company -> StringUtils.hasText(company.getTaxCode()) && StringUtils.hasText(company.getDigitalAddress()))
                .collect(Collectors.toList());
    }
}
