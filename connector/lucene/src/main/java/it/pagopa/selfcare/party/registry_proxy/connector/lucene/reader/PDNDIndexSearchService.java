package it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.PDNDTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter.DocumentToPDNDConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.PDNDQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
class PDNDIndexSearchService extends IndexSearchServiceTemplate<PDND> {

    @Autowired
    public PDNDIndexSearchService(Directory pdndDirectory) {
        super(pdndDirectory, new PDNDTokenAnalyzer(), new DocumentToPDNDConverter());
    }

    @Override
    protected QueryResult<PDND> getQueryResult(List<PDND> items, long totalHits) {
        final PDNDQueryResult queryResult = new PDNDQueryResult();
        queryResult.setItems(items);
        queryResult.setTotalHits(totalHits);
        return queryResult;
    }

}
