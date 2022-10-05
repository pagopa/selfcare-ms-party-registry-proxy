package it.pagopa.selfcare.party.registry_proxy.web.config;

import it.pagopa.selfcare.commons.web.config.BaseWebConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(BaseWebConfig.class)
class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToOriginConverter());
    }

    public static class StringToOriginConverter implements Converter<String, Origin> {
        @Override
        public Origin convert(String source) {
            return Origin.fromValue(source);
        }
    }

}
