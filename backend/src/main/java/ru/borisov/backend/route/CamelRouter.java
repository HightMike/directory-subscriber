package ru.borisov.backend.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CamelRouter extends RouteBuilder {

    private final CamelContext camelContext;

    @Value("${flow.tpk.enable}")
    private boolean autoStartup;

    @Autowired
    public CamelRouter(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public void configure(){
        //TODO написать маршруты
    }
}
