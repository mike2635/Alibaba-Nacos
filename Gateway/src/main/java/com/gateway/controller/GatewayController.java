package com.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @GetMapping("/gateway")
    public String getGateway(){
        return "this is a gateway,please enjoy the time";
    }
}
