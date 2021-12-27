package org.abigtomato.nebula.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Iterator;

@Slf4j
@SpringBootApplication
public class NebulaGatewayApplication {

    public static void main(String[] args) {
        log.info("[NebulaGatewayApplication] 开始启动.");
        ConfigurableApplicationContext act = new SpringApplicationBuilder(NebulaGatewayApplication.class)
                .properties("spring.profiles.active=local")
                .run(args);

        Iterator<String> beanNamesIterator = act.getBeanFactory().getBeanNamesIterator();
        while (beanNamesIterator.hasNext()) {
            log.info("==========> {}", beanNamesIterator.next());
        }
        log.info("[NebulaGatewayApplication] 启动成功.");
    }
}
