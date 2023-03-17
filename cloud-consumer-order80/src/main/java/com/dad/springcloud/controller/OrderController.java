package com.dad.springcloud.controller;

import com.dad.springcloud.entities.CommontResult;
import com.dad.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/consumer")

public class OrderController {
    public static final String PAYMENT_URL="http://localhost:8001/payment";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/payment/create")
    public CommontResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/create",payment,CommontResult.class);
    }

    @GetMapping("/getPaymentById/{id}")
    public CommontResult<Payment> getPaymentById(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL+"/get/"+id,CommontResult.class);
    }

    @GetMapping("/getPaymentList")
    public CommontResult <Payment> getPaymentList(){
        return restTemplate.getForObject(PAYMENT_URL+"/getPaymentList",CommontResult.class);
    }

    @GetMapping("/upPayment")
    public CommontResult <Payment> upPaymentById(Payment payment){
        return  restTemplate.postForObject(PAYMENT_URL+"/upPayment",payment,CommontResult.class);

    }
    @GetMapping("/delPayment/{id}")
    public CommontResult <Payment> delPaymentById(@PathVariable("id") Long id){
        return  restTemplate.postForObject(PAYMENT_URL+"/delPaymentById/{id}",id,CommontResult.class);

    }



}
