package it.pagopa.selfcare.party.registry_proxy.connector.dao;

import it.pagopa.selfcare.party.registry_proxy.connector.dao.model.InfoCamereBatchRequestEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoCamereBatchRequestRepository extends MongoRepository<InfoCamereBatchRequestEntity, ObjectId>, MongoCustomConnector {
}
