package ru.borisov.backend.configuration;

import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.errorhandler.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcHandler extends RouteBuilder {

    @Bean
    public DeadLetterChannelBuilder myErrorHandler() {
        DeadLetterChannelBuilder deadLetterChannelBuilder = new DeadLetterChannelBuilder();
        deadLetterChannelBuilder.setDeadLetterUri("direct:error");
        deadLetterChannelBuilder.setRedeliveryPolicy(new RedeliveryPolicy().disableRedelivery());
        deadLetterChannelBuilder.useOriginalMessage();
        return deadLetterChannelBuilder;
    }

    @Override
    public void configure() throws Exception {
        from("direct:error")
                .to("log:error")
                .end();
    }
}
