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
public class AnacDataConnectorImpl implements AnacDataConnector {

    private final FileStorageConnector fileStorageConnector;
    private final String sourceFilename;

    public AnacDataConnectorImpl(@Value("${blobStorage.anac.filename}") String anacCsvFileName,
                                 FileStorageConnector fileStorageConnector) {
        this.fileStorageConnector = fileStorageConnector;
        this.sourceFilename = anacCsvFileName;
    }

    @Override
    public List<Station> getStations() {
        log.trace("getStations start");
        List<Station> stations = new ArrayList<>();
        ResourceResponse resourceResponse;
        try {
            resourceResponse = fileStorageConnector.getFile(sourceFilename);
        } catch (Exception e) {
            log.error("Impossible to retrieve file ANAC. Error: {}", e.getMessage(), e);
            return stations;
        }
        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resourceResponse.getData())))) {
            CsvToBean<Station> csvToBean = new CsvToBeanBuilder<Station>(reader)
                    .withType(AnacDataTemplate.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            stations = csvToBean.parse();
        } catch (Exception e) {
            log.error("Impossible to acquire data for ANAC. Error: {}", e.getMessage(), e);
        }
        log.debug("getStations result = {}", stations);
        log.trace("getStations end");
        return stations
                    .stream()
                    .filter(station -> !StringUtils.hasText(station.getOriginId()))
                    .collect(Collectors.toList());
    }
}
