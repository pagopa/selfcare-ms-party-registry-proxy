package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.DummyPDND;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.PDNDEntity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND.Field;
import org.apache.lucene.document.Document;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;

class PDNDToDocumentConverterTest {

    private final Function<PDND, Document> converter;


    PDNDToDocumentConverterTest() {
        this.converter = new PDNDToDocumentConverter();
    }


    @Test
    void apply_nullInput() {
        // given
        final PDND input = null;
        // when
        final Document output = converter.apply(input);
        // then
        assertNull(output);
    }


    @Test
    void apply() {
        // given
        final PDND input = new DummyPDND();
        // when
        final Document output = converter.apply(input);
        // then
        assertNotNull(output);
        assertEquals(input.getId(), output.get(Field.ID.toString()));
        assertEquals(input.getOriginId(), output.get(Field.ORIGIN_ID.toString()));
        assertEquals(input.getTaxCode(), output.get(Field.TAX_CODE.toString()));
        assertEquals(input.getDescription(), output.get(Field.DESCRIPTION.toString()));
        assertEquals(input.getDigitalAddress(), output.get(Field.DIGITAL_ADDRESS.toString()));
        assertTrue(input.isAnacEnabled(), output.get(Field.ANAC_ENABLED.toString()));
        assertTrue(input.isAnacEngaged(), output.get(Field.ANAC_ENGAGED.toString()));
        final Set<String> fieldValues = Arrays.stream(Field.values())
                .map(Field::toString)
                .collect(Collectors.toSet());
        assertEquals(1, output.getFields().stream()
                .filter(field -> !fieldValues.contains(field.name()))
                .count());
    }


    @Test
    void apply_NullOptionalFields() {
        // given
        final PDND input = mockInstance(new PDNDEntity());
        // when
        final Document output = converter.apply(input);
        // then
        assertNotNull(output);
        assertEquals(input.getId(), output.get(Field.ID.toString()));
        assertEquals(input.getOriginId(), output.get(Field.ORIGIN_ID.toString()));
        assertEquals(input.getTaxCode(), output.get(Field.TAX_CODE.toString()));
        assertEquals(input.getDescription(), output.get(Field.DESCRIPTION.toString()));
        assertEquals(input.getDigitalAddress(), output.get(Field.DIGITAL_ADDRESS.toString()));
        assertTrue(input.isAnacEnabled(), output.get(Field.ANAC_ENABLED.toString()));
        assertTrue(input.isAnacEngaged(), output.get(Field.ANAC_ENGAGED.toString()));
        final Set<String> fieldValues = Arrays.stream(Field.values())
                .map(Field::toString)
                .collect(Collectors.toSet());
        assertEquals(1, output.getFields().stream()
                .filter(field -> !fieldValues.contains(field.name()))
                .count());
    }

}