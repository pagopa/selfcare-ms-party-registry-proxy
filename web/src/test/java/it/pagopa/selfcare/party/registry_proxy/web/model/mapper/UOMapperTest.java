package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import it.pagopa.selfcare.party.registry_proxy.web.model.DummyUO;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOsResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.commons.utils.TestUtils.reflectionEqualsByName;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void toResourceList_null() {
        //given
        final List<UOResource> uos = null;
        final long count = 0;
        //when
        final UOsResource resource = UOMapper.toResource(uos,count);
        //then
        assertNull(resource);
    }


    @Test
    void toResourceList() {
        //given
        final List<UOResource> uos = List.of(mockInstance(new UOResource()));
        final long count = 0;
        //when
        final UOsResource resource = UOMapper.toResource(uos,count);
        //then
        assertNotNull(resource);
        assertSame(uos, resource.getItems());
    }

}