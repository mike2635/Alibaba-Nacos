package com.consumer.controller;

import com.consumer.balancer.LoadBalancer;
import com.consumer.feign.ConsumerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@RefreshScope
public class ConsumerController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalancer locationLoadBalancer;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private ConsumerFeign consumerFeign;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.name}")
    private String name;

    @GetMapping("/getURL")
    public Object getUrl(){
        //通过自定义负载均衡算法拿到一个服务
        List<ServiceInstance> instances = discoveryClient.getInstances("nacos-producer");
        ServiceInstance serviceInstance = locationLoadBalancer.getServiceInstance(instances);
//        ServiceInstance serviceInstance = instances.get(0);
        URI uri = serviceInstance.getUri();
        return restTemplate.getForObject(uri + "/producer/test01", String.class);
    }
    @GetMapping("/getInstance")
    public Object getServiceInstance(){
        //使用负载均衡器选择一个服务
        return loadBalancerClient.choose("nacos-producer");
    }
    @GetMapping("/selectOne")
    public Object getServiceInstance01(){
        return restTemplate.getForObject("http://nacos-producer/producer/test01",String.class);
    }

    @GetMapping("/selectByOne")
    public String getServiceInstance02(){
        System.out.println("这是消费者一号:"+name);
        return consumerFeign.producer();
    }

}
