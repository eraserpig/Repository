package com.dad.springcloud.controller;

import com.dad.springcloud.entities.vo.CommonResult;
import com.dad.springcloud.service.SensitiveWordService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author gmq
 * @Date 20230914
 * @Description
 **/

@RestController
@RequestMapping("/sensitive")
@Slf4j
public class SensitiveWordController {

    @Resource
    SensitiveWordService sensitiveWordService;

    @GetMapping (value = "/filter")
    public CommonResult filter(String text){
        // 创建并启动计时器
        Stopwatch stopwatch = Stopwatch.createStarted();
        Set<String> set =sensitiveWordService.sensitiveWordFiltering(text);
        Map map=new HashMap<String, BigInteger>();
        log.info("*****插入结果"+set);
       return new  CommonResult(200,"查询成功",set);
    }


    @GetMapping (value = "/testAddData")
    public CommonResult testAddData(){
        // 创建并启动计时器
        Stopwatch stopwatch = Stopwatch.createStarted();
        boolean aa =sensitiveWordService.testAddData();
        Map map=new HashMap<String, BigInteger>();
        log.info("执行时长： {} 秒", stopwatch.elapsed().getSeconds());
        log.info("执行时长： {} 毫秒", stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return new  CommonResult(200,"插入成功",null);
    }






}
