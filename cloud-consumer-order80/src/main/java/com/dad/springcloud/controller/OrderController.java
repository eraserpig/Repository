package com.dad.springcloud.controller;


import com.dad.springcloud.entities.dao.Payment;
import com.dad.springcloud.entities.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@RestController
@Slf4j
@RequestMapping("/consumer")

public class OrderController {
    public static final String PAYMENT_URL="http://localhost:8001/payment";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/create",payment, CommonResult.class);
    }

    @GetMapping("/getPaymentById/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL+"/get/"+id, CommonResult.class);
    }

    @GetMapping("/getPaymentList")
    public CommonResult<Payment> getPaymentList(){
        return restTemplate.getForObject(PAYMENT_URL+"/getPaymentList", CommonResult.class);
    }

    @GetMapping("/upPayment")
    public CommonResult<Payment> upPaymentById(Payment payment){
        return  restTemplate.postForObject(PAYMENT_URL+"/upPayment",payment, CommonResult.class);

    }
    @GetMapping("/delPayment/{id}")
    public CommonResult<Payment> delPaymentById(@PathVariable("id") Long id){
        return  restTemplate.postForObject(PAYMENT_URL+"/delPaymentById/"+id, HttpMethod.DELETE, CommonResult.class);

    }

}
