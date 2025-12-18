package it.pagopa.selfcare.party.registry_proxy.connector.rest.utils;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchValue;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AzureUtils {

    private static final int AZURE_BATCH_SIZE = 1000;

    public static <T> List<List<T>> partition(List<T> list) {
        int size = AZURE_BATCH_SIZE;
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }

    public static boolean isBatchSuccessful(SearchServiceStatus status) {
        return status != null
                && status.getValue() != null
                && status.getValue().stream().allMatch(AzureSearchValue::getStatus);
    }
}
