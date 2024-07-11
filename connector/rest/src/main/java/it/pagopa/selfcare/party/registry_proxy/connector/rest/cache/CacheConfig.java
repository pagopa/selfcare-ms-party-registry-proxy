package it.pagopa.selfcare.party.registry_proxy.connector.rest.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.PDND_CLIENT_ASSERTION_CACHE;
import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.PDND_TOKEN_CACHE;

@Configuration
@EnableCaching
public class CacheConfig {

    @Primary
    @Bean(name = PDND_CLIENT_ASSERTION_CACHE)
    public CacheManager cacheManagerClientAssertion(@Value("${rest-client.pdnd.client-assertion.deadline}") Integer pdndClientAssertionDeadlineInSeconds) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(pdndClientAssertionDeadlineInSeconds, TimeUnit.SECONDS));
        return caffeineCacheManager;
    }

    @Bean(name = PDND_TOKEN_CACHE)
    public CacheManager cacheManagerToken(@Value("${rest-client.pdnd.token.deadline}") Integer pdndTokenDeadlineInSeconds) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(pdndTokenDeadlineInSeconds, TimeUnit.SECONDS));
        return caffeineCacheManager;
    }
}
