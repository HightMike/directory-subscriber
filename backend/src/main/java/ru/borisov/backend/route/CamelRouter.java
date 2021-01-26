package ru.borisov.backend.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CamelRouter extends RouteBuilder {

//    private final CamelContext camelContext;

//    @Value("${flow.tpk.enable}")
//    private boolean autoStartup;

    @Value("${cron.scheduler}")
    private String scheduler;

    @Value("${file.name.data}")
    private String data;
//    @Autowired
//    public CamelRouter(CamelContext camelContext) {
//        this.camelContext = camelContext;
//    }

    @Override
    public void configure(){
//        from("file:/static/files/work?scheduler=quartz2&scheduler.cron="+scheduler)
        from("file:/static/files/work")
                .routeId("file processing")
                .convertBodyTo(String.class)
                .setBody(simple("Start task at ${date:now:yyyy-MM-dd'T'HH:mm:ssZ}"))
                .choice()
                    .when(exchange -> ((String) exchange.getIn().getBody()).contains("test"))
                        .to("file:"+data);
//        camelContext.start();
        //TODO написать маршруты
    }
}
