package ru.borisov.backend.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static ru.borisov.backend.constants.UserConstants.WORK_DIR;

@Component
public class CamelRouter extends RouteBuilder {

    @Value("${cron.scheduler}")
    private String scheduler;

    @Value("${file.name.filePathPrepare}")
    private String filePathPrepare;

    @Override
    public void configure(){
//        from("file:/static/files/work?scheduler=quartz2&scheduler.cron="+scheduler)
        from("file:/".concat(filePathPrepare.concat(WORK_DIR)))
                .routeId("file processing")
                .convertBodyTo(String.class)
                .setBody(simple("Start task at ${date:now:yyyy-MM-dd'T'HH:mm:ssZ}"))
                .choice()
                    .when(exchange -> ((String) exchange.getIn().getBody()).contains("test"))
                        .to("file:"+"data");
        //TODO написать маршруты
    }
}
