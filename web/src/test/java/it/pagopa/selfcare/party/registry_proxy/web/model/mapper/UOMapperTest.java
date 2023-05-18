package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import it.pagopa.selfcare.party.registry_proxy.web.model.DummyUO;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOResource;
import org.junit.jupiter.api.Test;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.commons.utils.TestUtils.reflectionEqualsByName;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UOMapperTest {

    @Test
    void toResource_null() {
        //given
        final UO uo = null;
        //when
        final UOResource resource = UOMapper.toResource(uo);
        //then
        assertNull(resource);
    }


    @Test
    void toResource() {
        //given
        final UO uo = mockInstance(new DummyUO());
        //when
        final UOResource resource = UOMapper.toResource(uo);
        //then
        assertNotNull(resource);
        reflectionEqualsByName(uo, resource);
    }

}