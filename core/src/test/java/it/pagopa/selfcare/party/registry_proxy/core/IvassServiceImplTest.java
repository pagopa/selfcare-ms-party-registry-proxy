package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.DummyInsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IvassServiceImplTest {

    @Mock
    private IndexSearchService<InsuranceCompany> indexSearchService;

    @InjectMocks
    private IvassServiceImpl ivassService;

    @Test
    void findById_ResourceNotFound() {
        // given
        final String taxId = "taxId";
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of());
        // when
        final Executable executable = () -> ivassService.findByTaxCode(taxId);
        // then
        assertThrows(ResourceNotFoundException.class, executable);
    }


    @Test
    void findById_TooManyResourceFound() {
        // given
        final String taxId = "taxId";
        final DummyInsuranceCompany dummyCompany = new DummyInsuranceCompany();
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of(dummyCompany, dummyCompany));
        // when
        final Executable executable = () -> ivassService.findByTaxCode(taxId);
        // then
        assertThrows(TooManyResourceFoundException.class, executable);
    }

    @Test
    void findById_found() {
        // given
        final String taxId = "taxId";
        final DummyInsuranceCompany dummyCompany = new DummyInsuranceCompany();
        dummyCompany.setId("id");
        dummyCompany.setTaxCode("taxId");
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of(dummyCompany));
        // when
        final InsuranceCompany result = ivassService.findByTaxCode(taxId);
        // then
        assertSame(dummyCompany, result);
    }
}