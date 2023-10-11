package it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.config.InMemoryIndexConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import org.apache.lucene.store.Directory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        InMemoryIndexConfig.class,
        PDNDIndexSearchService.class
})
class PDNDIndexSearchServiceTest {

    @Autowired
    private PDNDIndexSearchService indexSearchService;

    @Autowired
    private Directory pdndDirectory;

    @Test
    void getQueryResult() {
        final QueryResult<Station> results = indexSearchService.getQueryResult(new ArrayList<>(), 1);
        assertNotNull(results);
    }

}