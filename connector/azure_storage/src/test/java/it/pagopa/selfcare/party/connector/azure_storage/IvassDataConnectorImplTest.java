package it.pagopa.selfcare.party.connector.azure_storage;

import it.pagopa.selfcare.party.connector.azure_storage.client.AzureBlobClient;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IvassDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
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
class IvassDataConnectorImplTest {

    @Mock
    private AzureBlobClient azureBlobClientMock;

    @Test
    void getInsuranceCompanies() {
        final String filename = "test.csv";
        IvassDataConnector ivassDataConnector = new IvassDataConnectorImpl(filename, azureBlobClientMock);
        ResourceResponse response = new ResourceResponse();
        String bytes = "CODICE_IVASS,CODICE_FISCALE,DENOMINAZIONE_IMPRESA,PEC,TIPO_LAVORO,TIPO_ALBO\n" +
                "aaaaaaa,codice_test,denominazione_test,test@pec.it,lavoro,albo\n";
        response.setData(bytes.getBytes(StandardCharsets.UTF_8));
        when(azureBlobClientMock.getFile(anyString())).thenReturn(response);
        final List<InsuranceCompany> companies = ivassDataConnector.getInsurances();
        assertNotNull(companies);
        verify(azureBlobClientMock, times(1)).getFile(filename);
        verifyNoMoreInteractions(azureBlobClientMock);
    }
}
