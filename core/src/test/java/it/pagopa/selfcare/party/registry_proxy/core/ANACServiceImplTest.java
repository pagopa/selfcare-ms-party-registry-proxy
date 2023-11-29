package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.function.Executable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ANACServiceImplTest {

    private ANACServiceImpl anacService;

    @Mock
    private AnacDataConnector anacDataConnector;

    @Test
    void getStations() {
        anacService = new ANACServiceImpl(anacDataConnector);
        InputStream inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        when(anacDataConnector.getANACData()).thenReturn(inputStream);
        Executable executable = () -> anacService.getStations();
        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void getStationsEmpty() {
        anacService = new ANACServiceImpl(anacDataConnector);
        when(anacDataConnector.getANACData()).thenReturn(InputStream.nullInputStream());
        List<Station> stationList = anacService.getStations();
        Assertions.assertEquals(0, stationList.size());
    }
}
