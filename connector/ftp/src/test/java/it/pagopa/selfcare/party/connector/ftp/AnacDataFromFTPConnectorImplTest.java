package it.pagopa.selfcare.party.connector.ftp;

import it.pagopa.selfcare.party.connector.ftp.client.AnacFTPClient;
import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnacDataFromFTPConnectorImplTest {

    @Mock
    private AnacFTPClient ftpClient;

    @Test
    void getStations() {
        final String filename = "test.csv";
        AnacDataConnector anacDataConnector = new AnacDataFromFTPConnectorImpl(filename, ftpClient);
        String bytes = "codice_IPA,cf_gestore,cenominazione,comicilio_digitale,anac_incaricato,anac_abilitato\n" +
                "aaaaaaa,tax_code,denominazione,test@pec.it,false,false\n";
        InputStream mockInputStream = new ByteArrayInputStream(bytes.getBytes(StandardCharsets.UTF_8));
        when(ftpClient.getFile(anyString())).thenReturn(mockInputStream);
        final InputStream inputStream = anacDataConnector.getANACData();
        assertNotNull(inputStream);
        verify(ftpClient, times(1)).getFile(filename);
        verifyNoMoreInteractions(ftpClient);
    }

    @Test
    void getStationsFileNotFound() throws IOException {
        final String filename = "test.csv";
        AnacDataConnector anacDataConnector = new AnacDataFromFTPConnectorImpl(filename, ftpClient);
        when(ftpClient.getFile(anyString())).thenReturn(InputStream.nullInputStream());
        final InputStream inputStream = anacDataConnector.getANACData();
        assertTrue(inputStream.available() == 0);
        verify(ftpClient, times(1)).getFile(filename);
        verifyNoMoreInteractions(ftpClient);
    }
}
