package com.dad.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommontResult <T>{
    private  Integer code;
    private String msg;
    private  T data;
    public CommontResult(Integer code ,String msg){
        this(code,msg,null);
    }

}
