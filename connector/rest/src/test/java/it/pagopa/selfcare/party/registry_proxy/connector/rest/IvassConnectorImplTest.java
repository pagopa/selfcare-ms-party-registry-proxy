package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.IVASSRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.IvassDataTemplate;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.IvassUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IvassConnectorImplTest {
    private IVASSRestClient ivassRestClient;
    private IvassUtil ivassUtil;
    private IvassConnectorImpl ivassConnector;

    @BeforeEach
    void setUp() {
        this.ivassRestClient = mock(IVASSRestClient.class);
        this.ivassUtil = mock(IvassUtil.class);
        List<String> registryTypes = Arrays.asList("ElencoI","ElencoII","SezioneI","SezioneII");
        List<String> workTypes = Arrays.asList("VITA","PICCOLO CUMULO","MISTA");
        ivassConnector = new IvassConnectorImpl(ivassRestClient, registryTypes, workTypes, ivassUtil);
    }

    @Test
    void getInsurances_shouldReturnFilteredCompanies() {
        byte[] zip = new byte[]{0, 1, 2, 3, 4};
        byte[] csv = new byte[]{5, 6, 7, 8, 9};

        IvassDataTemplate company1 = new IvassDataTemplate();
        company1.setDigitalAddress("digitalAddress1");
        company1.setWorkType("VITA");
        company1.setRegisterType("ElencoI - test");
        company1.setTaxCode("taxCode1");

        IvassDataTemplate company2 = new IvassDataTemplate();
        company2.setDigitalAddress("digitalAddress2");
        company2.setWorkType("VITA");
        company2.setRegisterType("ElencoII - test");
        company2.setTaxCode("taxCode2");


        List<InsuranceCompany> companies = Arrays.asList(company1, company2);

        when(ivassRestClient.getInsurancesZip()).thenReturn(zip);
        when(ivassUtil.extractSingleFileFromZip(zip)).thenReturn(csv);
        when(ivassUtil.readCsv(csv)).thenReturn(companies);

        List<InsuranceCompany> result = ivassConnector.getInsurances();

        assertEquals(companies.size(), result.size());
        verify(ivassRestClient, times(1)).getInsurancesZip();
        verify(ivassUtil, times(1)).extractSingleFileFromZip(zip);
    }

    @Test
    void getInsurances_shouldReturnEmptyList_whenNoCompaniesMatchFilter() {
        byte[] zip = new byte[]{0, 1, 2, 3, 4};
        byte[] csv = new byte[]{5, 6, 7, 8, 9};
        List<InsuranceCompany> companies = Arrays.asList(new IvassDataTemplate(), new IvassDataTemplate());

        when(ivassRestClient.getInsurancesZip()).thenReturn(zip);
        when(ivassUtil.extractSingleFileFromZip(zip)).thenReturn(csv);
        when(ivassUtil.readCsv(csv)).thenReturn(companies);

        ivassConnector = spy(ivassConnector);

        List<InsuranceCompany> result = ivassConnector.getInsurances();

        assertEquals(0, result.size());
        verify(ivassRestClient, times(1)).getInsurancesZip();
        verify(ivassUtil, times(1)).extractSingleFileFromZip(zip);
    }
}