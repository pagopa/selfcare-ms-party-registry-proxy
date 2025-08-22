package it.pagopa.selfcare.party.registry_proxy.connector.model.institution;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

@Data
@FieldNameConstants(asEnum = true)
public class Onboarding {

    private String productId;
    private String tokenId;
    private String status;
    private String contract;
    private String pricingPlan;
    private Billing billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime closedAt;
    private Boolean isAggregator;
    private InstitutionType institutionType;
    private String origin;
    private String originId;

}
