package com.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 李龙发
 */
@FeignClient("nacos-producer")
public interface ConsumerFeign {

    @GetMapping("/")
    String producer();
}
