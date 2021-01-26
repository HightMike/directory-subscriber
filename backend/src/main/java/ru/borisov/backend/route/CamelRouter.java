package ru.borisov.backend.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.borisov.backend.service.UserInfoService;

@Component
public class CamelRouter extends RouteBuilder {

    private final DeadLetterChannelBuilder error;
    private final UserInfoService userInfoService;

    @Autowired
    public CamelRouter(DeadLetterChannelBuilder deadLetterChannelBuilder,
                       UserInfoService userInfoService) {
        this.error = deadLetterChannelBuilder;
        this.userInfoService = userInfoService;
    }

    @Value("${file.name.filePathPrepare}")
    private String filePathPrepare;

    @Override
    public void configure(){

        from("file:/home/hightmike/testFiles/work/?scheduler=quartz&scheduler.cron={{scheduler.file.cron}}")
                .autoStartup(true)
                .routeId("FileProcessingRoute")
                .convertBodyTo(String.class)
                .log(LoggingLevel.INFO, "route started"+simple("Start task at ${date:now:yyyy-MM-dd'T'HH:mm:ssZ}"))
                .process(exchange -> {
                    userInfoService.checkFile(exchange.getIn().getBody());
                })
                .end();
//                .to("file:/home/hightmike/testFiles/data/");
    }
}
