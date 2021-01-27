package ru.borisov.backend.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.borisov.backend.service.UserInfoService;

import java.io.IOException;

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

    /**
     * основной маршрут приложения
     * exclude -  исключает файлы флаги с расширением .ready из дальнейшего пробрасывания по папкам
     * doneFileName - фильтрует маршрут для файлов для которых есть пара с таким же названием и расширением .ready
     * cron - запускает маршрут по планировщику
     */
    @Override
    public void configure(){

        from("file:{{file.name.filePathPrepare}}work/?exclude=.*.ready&scheduler=quartz&scheduler.cron={{scheduler.file.cron}}"
                + "?doneFileName=${file:name}.ready")
                .autoStartup(true)
                .errorHandler(error)
                .routeId("FileProcessingRoute")
                .convertBodyTo(String.class)
                .log(LoggingLevel.INFO, "route started"+simple("Start task at ${date:now:yyyy-MM-dd'T'HH:mm:ssZ}"))
                .choice()
                .when(exchange -> userInfoService.checkFile(exchange.getIn().getBody()))
                    .to("file:{{file.name.filePathPrepare}}data/")
                    .process(exchange -> {
                        Object node = userInfoService.loadWorkData(exchange.getIn().getBody());
                        exchange.getIn().setBody(node.toString());
                    })
                    .to("file:{{file.name.filePathPrepare}}done/")
                .otherwise()
                    .to("file:{{file.name.filePathPrepare}}invalid/");
    }
}
