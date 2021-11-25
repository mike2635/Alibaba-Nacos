package com.producer01.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
@Slf4j
public class Porducer {

    private static final String SEKILL_RULE = "seckill";

    public Porducer() {
        initSeckillRule();
    }
    @Value("${spring.name}")
    private String name;

    @GetMapping("/")
    public String producer(HttpServletRequest request) {
        System.out.println("这是生产者服务2号"+name);
        return "这是生产者服务2号,网关端口是:"+request.getHeader("serverPort");
    }

    @SentinelResource(value = "getOrderAnnotation2",fallback = "getOrderException")
    @GetMapping("/getOrderAnnotation2")
    public String getOrderAnnotation2(){
        try{
            Thread.sleep(300);
        }catch (Exception e) {
            return "出现异常了";
        }
        return "getOrderAnnotation2222";
    }

    public String getOrderException(){
        return "服务降级了！！！";
    }

    @RequestMapping("/seckill")
    public String seckill(Long userId, Long orderId) {
        try {
            Entry entry = SphU.entry(SEKILL_RULE, EntryType.IN, 1, userId);
            return "秒杀成功";
        } catch (Exception e) {
            return "当前用户访问过度频繁，请稍后重试!";
        }
    }

    private void initSeckillRule() {
        ParamFlowRule rule = new ParamFlowRule(SEKILL_RULE)
                // 对我们秒杀接口第0个参数实现限流
                .setParamIdx(0)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                // 每秒QPS最多只有1s
                .setCount(1);
        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
        log.info(">>>秒杀接口限流策略配置成功<<<");
    }

}
