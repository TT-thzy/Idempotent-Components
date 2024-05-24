package com.idempotent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @className: IdempotentRestApplication
 * @description:
 * @author: TT-Berg
 * @date: 2023/7/21
 **/
@EnableFeignClients
@EnableCaching
@EnableAsync
@EnableRetry
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@ComponentScan(
        basePackages = {"com.idempotent"}
)
@SpringBootApplication(exclude = {GsonAutoConfiguration.class, FlywayAutoConfiguration.class})
@PropertySource("classpath:application.yml")
public class IdempotentRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdempotentRestApplication.class, args);
    }
}
