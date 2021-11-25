package com.producer01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Producer01Application {
    public static void main(String[] args) {
        SpringApplication.run(Producer01Application.class,args);
    }
}
