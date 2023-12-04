package it.pagopa.selfcare.party.connector.ftp;

import it.pagopa.selfcare.party.connector.ftp.client.AnacFTPClient;
import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnacDataFromFTPConnectorImplTest {

    @Mock
    private AnacFTPClient ftpClient;

    @Test
    void getStationsFound() {
        final String filename = "test.csv";
        AnacDataConnector anacDataConnector = new AnacDataFromFTPConnectorImpl(filename, ftpClient);
        String bytes = """
                codice_IPA,cf_gestore,cenominazione,comicilio_digitale,anac_incaricato,anac_abilitato
                aaaaaaa,tax_code,denominazione,test@pec.it,false,false
                """;
        InputStream mockInputStream = new ByteArrayInputStream(bytes.getBytes(StandardCharsets.UTF_8));
        when(ftpClient.getFile(anyString())).thenReturn(Optional.of(mockInputStream));
        final Optional<InputStream> inputStream = anacDataConnector.getANACData();
        assertNotNull(inputStream);
        verify(ftpClient, times(1)).getFile(anyString());
        verifyNoMoreInteractions(ftpClient);
    }

    @Test
    void getStationsFileNotFound() {
        final String filename = "test.csv";
        AnacDataConnector anacDataConnector = new AnacDataFromFTPConnectorImpl(filename, ftpClient);
        when(ftpClient.getFile(anyString())).thenReturn(Optional.empty());
        final  Optional<InputStream> inputStream = anacDataConnector.getANACData();
        assertTrue(inputStream.isEmpty());
        verify(ftpClient, times(1)).getFile(any());
        verifyNoMoreInteractions(ftpClient);
    }
}
