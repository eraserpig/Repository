package com.dad.springcloud.entities.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author gmq
 * @Date 2023-09-14
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveWordDTO {
    private Integer id;


    private String keyword;


    private String status;


    private Date createTime;

    private String createUser;


    private Date updatedTime;


    private String updateUser;


}
