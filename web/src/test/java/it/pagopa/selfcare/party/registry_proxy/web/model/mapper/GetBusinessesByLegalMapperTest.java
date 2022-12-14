package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import it.pagopa.selfcare.party.registry_proxy.web.model.GetBusinessesByLegalDto;
import org.junit.jupiter.api.Test;

class GetBusinessesByLegalMapperTest {
    /**
     * Method under test: {@link GetBusinessesByLegalMapper#fromDto(GetBusinessesByLegalDto)}
     */
    @Test
    void testFromDto() {
        GetBusinessesByLegalDto getBusinessesByLegalDto = new GetBusinessesByLegalDto();
        getBusinessesByLegalDto.setLegalTaxId("42");
        assertEquals("42", GetBusinessesByLegalMapper.fromDto(getBusinessesByLegalDto).getLegalTaxId());
    }

    /**
     * Method under test: {@link GetBusinessesByLegalMapper#fromDto(GetBusinessesByLegalDto)}
     */
    @Test
    void testFromDtoNull() {
        assertNull(GetBusinessesByLegalMapper.fromDto(null));
    }
}

