//package com.producer.config;
//
//import com.producer.controller.Producer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SentinelApplicationRunner implements ApplicationRunner {
//    @Autowired
//    private Producer producer;
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        producer.initFlowQpsRule();
//    }
//}
