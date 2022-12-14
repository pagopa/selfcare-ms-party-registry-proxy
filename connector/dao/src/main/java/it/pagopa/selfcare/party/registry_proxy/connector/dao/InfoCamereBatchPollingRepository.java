package it.pagopa.selfcare.party.registry_proxy.connector.dao;

import it.pagopa.selfcare.party.registry_proxy.connector.dao.model.InfoCamereBatchPollingEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoCamereBatchPollingRepository extends MongoRepository<InfoCamereBatchPollingEntity, ObjectId>, MongoCustomConnector  {

}
