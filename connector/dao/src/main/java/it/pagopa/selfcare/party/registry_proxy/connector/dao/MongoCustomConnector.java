package it.pagopa.selfcare.party.registry_proxy.connector.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface MongoCustomConnector {

    <O> List<O> find(Query query, Class<O> outputType);

    <O> Page<O> find(Query query, Pageable pageable, Class<O> outputType);

}