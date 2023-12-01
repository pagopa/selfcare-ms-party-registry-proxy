package it.pagopa.selfcare.party.registry_proxy.core;

import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AnacDataTemplate;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ANACServiceImpl implements ANACService {


    private final AnacDataConnector anacDataConnector;
    private final IndexWriterService<Station> stationIndexWriterService;

    @Scheduled(cron = "0 0 0/6 * * *")
    void updateStationIndex() {
        log.trace("start update ANAC Stations index");
        List<Station> stations = getStations();
        if (!stations.isEmpty()) {
            stationIndexWriterService.cleanIndex(Entity.STATION.toString());
            stationIndexWriterService.adds(stations);
        }
        log.trace("updated ANAC Stations index end");
    }

    @Override
    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Optional<InputStream> optionalInputStream = anacDataConnector.getANACData();

        if (optionalInputStream.isPresent()) {
            stations = retrieveStationListFromCSV(optionalInputStream.get());
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
