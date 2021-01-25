package ru.borisov.backend.configuration;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Bean
    public CamelContext camelContextTemplate() {
        CamelContext camelContext = new DefaultCamelContext();
        // configure where to load properties file in the properties component
        camelContext.getPropertiesComponent().setLocation("classpath:application.yml");
        return new DefaultCamelContext();
    }

}
