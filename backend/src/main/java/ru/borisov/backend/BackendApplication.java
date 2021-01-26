package ru.borisov.backend;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(BackendApplication.class, args);
        CamelSpringBootApplicationController applicationController =
                context.getBean(CamelSpringBootApplicationController.class);
        applicationController.run();
    }

}
