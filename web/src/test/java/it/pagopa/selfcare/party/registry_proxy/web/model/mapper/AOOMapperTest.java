package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.web.model.AOOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.DummyAOO;
import org.junit.jupiter.api.Test;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.commons.utils.TestUtils.reflectionEqualsByName;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AOOMapperTest {

    @Test
    void toResource_null() {
        //given
        final AOO aoo = null;
        //when
        final AOOResource resource = AOOMapper.toResource(aoo);
        //then
        assertNull(resource);
    }


    @Test
    void toResource() {
        //given
        final AOO aoo = mockInstance(new DummyAOO());
        //when
        final AOOResource resource = AOOMapper.toResource(aoo);
        //then
        assertNotNull(resource);
        reflectionEqualsByName(aoo, resource);
    }

}