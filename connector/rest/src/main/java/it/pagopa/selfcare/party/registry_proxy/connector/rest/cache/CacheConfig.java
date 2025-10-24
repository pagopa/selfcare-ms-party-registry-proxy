package it.pagopa.selfcare.party.registry_proxy.connector.rest.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.*;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = PDND_CLIENT_ASSERTION_CACHE)
    public CacheManager cacheManagerClientAssertion(@Value("${rest-client.pdnd.client-assertion.deadline:3300}") Integer pdndClientAssertionDeadlineInSeconds) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(pdndClientAssertionDeadlineInSeconds, TimeUnit.SECONDS));
        return caffeineCacheManager;
    }

    @Bean(name = PDND_TOKEN_CACHE)
    public CacheManager cacheManagerToken(@Value("${rest-client.pdnd.token.deadline:60}") Integer pdndTokenDeadlineInSeconds) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(pdndTokenDeadlineInSeconds, TimeUnit.SECONDS));
        return caffeineCacheManager;
    }

    @Bean(name = PDND_VISURA_TOKEN_CACHE)
    public CacheManager cacheManagerVisuraToken(@Value("${rest-client.pdnd.token.deadline:60}") Integer pdndTokenDeadlineInSeconds) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(pdndTokenDeadlineInSeconds, TimeUnit.SECONDS));
        return caffeineCacheManager;
    }

  @Bean(name = INSTITUTION_CACHE)
  public CacheManager cacheInstitution(@Value("${rest-client.selc-institution.deadline:600}") Integer institutionDeadlineInSeconds) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    caffeineCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(institutionDeadlineInSeconds, TimeUnit.SECONDS));
    return caffeineCacheManager;
  }

    @Bean(name = PDND_VISURA_CLIENT_ASSERTION_CACHE)
    public CacheManager cacheManagerVisuraClientAssertion(@Value("${rest-client.pdnd.token.deadline:60}") Integer pdndTokenDeadlineInSeconds) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(pdndTokenDeadlineInSeconds, TimeUnit.SECONDS));
        return caffeineCacheManager;
    }

    @Bean(name = "visureRedisCacheManagerL1")
    public CacheManager caffeineCacheConfig() {
        CaffeineCacheManager caffeine = new CaffeineCacheManager();
        caffeine.setCaffeine(Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS));
        return caffeine;
    }

}
