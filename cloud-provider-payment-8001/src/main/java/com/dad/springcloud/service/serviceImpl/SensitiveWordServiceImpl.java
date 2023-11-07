package com.dad.springcloud.service.serviceImpl;

import com.dad.springcloud.Utils.SensitiveWordDeal;
import com.dad.springcloud.dao.SensitiveWordDao;
import com.dad.springcloud.entities.DTO.SensitiveWordDTO;
import com.dad.springcloud.entities.dao.SensitiveWord;
import com.dad.springcloud.service.SensitiveWordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @Author gmq
 * @Date 20230914
 * @Description
 **/
@Service
@Slf4j
public class SensitiveWordServiceImpl implements SensitiveWordService {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/dramatic";
    private static final String DB_USER = "dramatic";
    private static final String DB_PASSWORD = "dra123";

    @Resource
    SensitiveWordDao sensitiveWordDao;

    @Override
    public Set<String> sensitiveWordFiltering(String text) {
        SensitiveWordDeal sensitiveWordDeal=new SensitiveWordDeal();
        // 从数据库中获取敏感词对象集合（调用的方法来自Dao层，此方法是service层的实现类）
        List<SensitiveWordDTO> sensitiveWords = sensitiveWordDao.getSensitiveWordListAll();
        sensitiveWordDeal.setText(text,sensitiveWords);
        Set<String> set=sensitiveWordDeal.getText();
        return set;

    }

    @Override
    public boolean testAddData() {

            SensitiveWord sensitiveWord=new SensitiveWord();
            Random random = new Random();
            for (int i = 0; i < 10000; i++) {
               // pstmt.setInt(1, i);
                sensitiveWord.setKeyword(getRandomChineseWord()); // 使用你自定义的getRandomChineseWord()函数生成随机的中文单词或汉字
                sensitiveWord.setCreateUser("admin");
                sensitiveWord.setStatus(1);
               int  aa=sensitiveWordDao.createSensitiveWord(sensitiveWord);
            }

        return false;
    }
    // 你需要自定义这个函数来生成随机的中文单词或汉字
    private static String getRandomChineseWord() {
        // 这里只是一个例子，你需要自己实现这个函数
        String[] words = {"中文", "你好", "实现", "美丽", "禅道", "笔记"};
        return words[new Random().nextInt(words.length)];
    }


}
