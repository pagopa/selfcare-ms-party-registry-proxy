package it.pagopa.selfcare.party.registry_proxy.core;

import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AnacDataTemplate;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ANACServiceImpl implements ANACService {

    private final AnacDataConnector anacDataConnector;

    @Override
    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        InputStream inputStream = anacDataConnector.getANACData();

        try {
            if (inputStream.available() != 0) {
                stations = retrieveStationListFromCSV(inputStream);
            }
        } catch (IOException e) {
            log.error("Impossible to acquire data for ANAC. Error: {}", e.getMessage(), e);
        }
        log.debug("getStations result = {}", stations);
        log.trace("getStations end");
        return stations;
    }

    private static List<Station> retrieveStationListFromCSV(InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream);
        CsvToBeanBuilder<Station> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(AnacDataTemplate.class);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        return csvToBeanBuilder.build()
                .parse()
                .stream()
                .filter(station -> !StringUtils.hasText(station.getOriginId()))
                .toList();
    }
}
