package com.producer.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Producer {
    @Value("${spring.name}")
    private String name;
    private static final String GETORDER_KEY = "getOrder";

    @GetMapping("/")
    public String producer(HttpServletRequest request){
        System.out.println("这是生产者服务1号"+name);
        return "这是生产者服务1号,网关端口是:"+request.getHeader("serverPort");
    }
    @RequestMapping("/initFlowQpsRule")
    public String initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(GETORDER_KEY);
        // QPS控制在1以内
        rule1.setCount(1);
        // QPS限流
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
        return "....限流配置初始化成功..";
    }
    @RequestMapping("/getOrder")
    public String getOrders() {
        Entry entry = null;
        try {
            entry = SphU.entry(GETORDER_KEY);
            // 执行我们服务需要保护的业务逻辑
            return "getOrder接口";
        } catch (Exception e) {
            e.printStackTrace();
            return "该服务接口已经达到上线!";
        } finally {
            // SphU.entry(xxx) 需要与 entry.exit() 成对出现,否则会导致调用链记录异常
            if (entry != null) {
                entry.exit();
            }
        }
    }

    @SentinelResource(value = GETORDER_KEY, blockHandler = "getOrderQpsException")
    @RequestMapping("/getOrderAnnotation")
    public String getOrderAnnotation() {
        return "getOrder接口";
    }

    /**
     * 被限流后返回的提示
     *
     */
    public String getOrderQpsException(BlockException e) {
        e.printStackTrace();
        return "该接口已经被限流啦!";
    }


    @GetMapping("/getOrderAnnotation1")
    public String getOrderAnnotation1(){
//        try{
//            Thread.sleep(300);
//        }catch (Exception e) {
//            return "出现异常了";
//        }
        return "getOrderAnnotation1";
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
}
