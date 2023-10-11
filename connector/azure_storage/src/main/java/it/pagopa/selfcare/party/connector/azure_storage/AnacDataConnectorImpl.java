package it.pagopa.selfcare.party.connector.azure_storage;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.connector.azure_storage.model.AnacDataTemplate;
import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.FileStorageConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.ResourceResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import org.springframework.util.StringUtils;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnacDataConnectorImpl implements AnacDataConnector {

    private final FileStorageConnector fileStorageConnector;
    private final String sourceFilename;

    public AnacDataConnectorImpl(@Value("${blobStorage.filename}") String pdndCsvFileName,
                                 FileStorageConnector fileStorageConnector) {
        this.fileStorageConnector = fileStorageConnector;
        this.sourceFilename = pdndCsvFileName;
    }

    @Override
    public List<Station> getStations() {
       log.trace("getStations start");
        List<Station> stations;
        final ResourceResponse resourceResponse = fileStorageConnector.getFile(sourceFilename);
        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resourceResponse.getData())))) {
            CsvToBean<Station> csvToBean = new CsvToBeanBuilder<Station>(reader)
                    .withType(AnacDataTemplate.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            stations = csvToBean.parse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("getStations result = {}", stations);
        log.trace("getStations end");
        return stations
                .stream()
                .filter(station -> !StringUtils.hasText(station.getOriginId()))
                .collect(Collectors.toList());
    }
}
