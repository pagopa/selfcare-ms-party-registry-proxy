package it.pagopa.selfcare.party.connector.azure_storage;

import it.pagopa.selfcare.party.connector.azure_storage.client.AzureBlobClient;
import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import it.pagopa.selfcare.party.registry_proxy.connector.model.ResourceResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnacDataConnectorImplTest {

    @Mock
    private AzureBlobClient azureBlobClientMock;

    @Test
    void getStations() {
        final String filename = "test.csv";
        AnacDataConnector anacDataConnector = new AnacDataConnectorImpl(filename, azureBlobClientMock);
        ResourceResponse response = new ResourceResponse();
        String bytes = "codice_IPA,cf_gestore,cenominazione,comicilio_digitale,anac_incaricato,anac_abilitato\n" +
                "aaaaaaa,DDTPPL89M26F839I,Di Dato Pierpaolo,pierpaolo.didato@pec.it,false,false\n";
        response.setData(bytes.getBytes(StandardCharsets.UTF_8));
        when(azureBlobClientMock.getFile(anyString())).thenReturn(response);
        final List<Station> stations = anacDataConnector.getStations();
        assertNotNull(stations);
        verify(azureBlobClientMock, times(1)).getFile(filename);
        verifyNoMoreInteractions(azureBlobClientMock);
    }
}
