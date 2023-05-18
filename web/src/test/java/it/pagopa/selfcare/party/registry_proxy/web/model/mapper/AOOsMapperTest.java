package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.web.model.AOOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.AOOsResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;

class AOOsMapperTest {

    @Test
    void toResource_null() {
        //given
        final List<AOOResource> aoos = null;
        final long count = 0;
        //when
        final AOOsResource resource = AOOsMapper.toResource(aoos, count);
        //then
        assertNull(resource);
    }


    @Test
    void toResource() {
        //given
        final List<AOOResource> aoos = List.of(mockInstance(new AOOResource()));
        final long count = 0;
        //when
        final AOOsResource resource = AOOsMapper.toResource(aoos, count);
        //then
        assertNotNull(resource);
        assertSame(aoos, resource.getItems());
    }

}