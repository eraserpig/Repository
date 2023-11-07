package com.dad.springcloud.dao;

import com.dad.springcloud.entities.DTO.SensitiveWordDTO;
import com.dad.springcloud.entities.dao.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author gmq
 * @Date 20230914
 * @Description
 **/
@Mapper
public interface SensitiveWordDao {


    List<SensitiveWordDTO> getSensitiveWordListAll();

    int createSensitiveWord(SensitiveWord sensitiveWord);



}
