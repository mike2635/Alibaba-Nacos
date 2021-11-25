package com.consumer.utils;


import com.consumer.balancer.LoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LocationLoadBalancer implements LoadBalancer {
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final Random random = new Random();
    @Override
    public ServiceInstance getServiceInstance(List<ServiceInstance> serviceInstances) {
        //轮询算法
//        int count = atomicInteger.incrementAndGet();
//        int size = serviceInstances.size();
//        return serviceInstances.get(count%size);
        //随机算法
        return serviceInstances.get(random.nextInt(serviceInstances.size()));
    }
}
