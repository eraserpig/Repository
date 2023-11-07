package com.dad.springcloud.entities.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-09-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveWord {


    private Integer id;


    private String keyword;


    private Integer status;


    private Date createTime;

    private String createUser;


    private Date updatedTime;


    private String updateUser;

}
