package com.dad.springcloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult <T>{
    private  Integer code;
    private String msg;
    private T data;

    public  CommonResult(Integer code ,String msg){
        this(code,msg,null);
    }


}
