package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.web.model.UOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOsResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;

class UOsMapperTest {

    @Test
    void toResource_null() {
        //given
        final List<UOResource> uos = null;
        final long count = 0;
        //when
        final UOsResource resource = UOsMapper.toResource(uos,count);
        //then
        assertNull(resource);
    }


    @Test
    void toResource() {
        //given
        final List<UOResource> uos = List.of(mockInstance(new UOResource()));
        final long count = 0;
        //when
        final UOsResource resource = UOsMapper.toResource(uos,count);
        //then
        assertNotNull(resource);
        assertSame(uos, resource.getItems());
    }

}