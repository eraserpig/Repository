package com.dad.springcloud.Utils;

import com.dad.springcloud.entities.DTO.SensitiveWordDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author gmq
 * @Date 2023-09-15
 * @Description
 **/
@Slf4j
public class SensitiveWordDeal {

    private Set<String> text;

    public Set<String> getText(){
        return text;
    }

    public void setText(String dealText,List<SensitiveWordDTO> sensitiveWords){
        SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
        // 构建敏感词库
        Map sensitiveWordMap = sensitiveWordInit.initKeyWord(sensitiveWords);
        // 传入SensitivewordEngine类中的敏感词库
        SensitivewordEngine.sensitiveWordMap = sensitiveWordMap;
        // 得到敏感词有哪些，传入2表示获取所有敏感词
        Set<String> set = SensitivewordEngine.getSensitiveWord(dealText, 2);
        int aa=      SensitivewordEngine.getWordSize();
        log.info("----------敏感词数量------------"+aa);
        String newText= SensitivewordEngine.replaceSensitiveWord(dealText, 2,"*") ;
        log.info("----------替换后内容------------"+newText);
        text=set;
    }




}
