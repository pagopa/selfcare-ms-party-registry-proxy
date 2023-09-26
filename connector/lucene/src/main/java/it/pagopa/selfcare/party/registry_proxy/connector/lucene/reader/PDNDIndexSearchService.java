package it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.PDNDTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter.DocumentToStationConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.StationQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
class PDNDIndexSearchService extends IndexSearchServiceTemplate<Station> {

    @Autowired
    public PDNDIndexSearchService(Directory pdndDirectory) {
        super(pdndDirectory, new PDNDTokenAnalyzer(), new DocumentToStationConverter());
    }

    @Override
    protected QueryResult<Station> getQueryResult(List<Station> items, long totalHits) {
        final StationQueryResult queryResult = new StationQueryResult();
        queryResult.setItems(items);
        queryResult.setTotalHits(totalHits);
        return queryResult;
    }

}
