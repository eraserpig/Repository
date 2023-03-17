package com.dad.springcloud.controller;

import com.dad.springcloud.entities.Payment;
import com.dad.springcloud.service.PaymentService;
import com.dad.springcloud.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;


@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @PostMapping(value = "/create")
    public CommonResult create(@Validated @RequestBody Payment payment){
        int result=paymentService.createPayment(payment);
        log.info("*****插入结果"+ result);
        if(result>0){
            return new CommonResult(200,"插入数据库成功",result);
        }else{
            return new CommonResult(500,"插入数据库失败",null);
        }
    }


    @GetMapping(value = "/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        if(id !=null){

            Payment payment=paymentService.getPaymentById(id);
            if(payment!=null){
                return new CommonResult(200,"插询成功",payment);
            }else {
                return  new CommonResult(233,"插询失败",null);
            }
        }else{
            return  new CommonResult(999,"id不可为空",null);
        }
    }

    @PostMapping(value="/upPayment")
    public CommonResult upPaymentById(@Validated @RequestBody Payment payment){

        int result=paymentService.upPayment(payment);
        if(result>0){
            return new CommonResult(200,"修改成功",result) ;
        }else{
            return new CommonResult(999,"修改失败请检查参数",null);
        }
    }

    @PostMapping(value="delPaymentById/{id}")
    public CommonResult delPayment(@PathVariable Long id ){
        int result =paymentService.delPaymentById(id);
        if(result >0){
            return new CommonResult(200,"删除成功",result);

        }else{
            return new CommonResult(999,"删除失败",result);
        }

    }

    @GetMapping(value="getPaymentList")
    public  CommonResult getPaymentList(){
        ArrayList<Payment> paymentList=paymentService.getPayMentList();
        if(paymentList.size()>0){
            return  new CommonResult(200,"插询成功",paymentList);
        }else{
            return new CommonResult(999,"插询失败",null);
        }

    }






}
