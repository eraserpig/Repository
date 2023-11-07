package com.dad.springcloud.service;

import java.util.Set;

/**
 * @Author gmq
 * @Date 20230914
 * @Description
 **/
public interface SensitiveWordService {

    Set<String> sensitiveWordFiltering(String text);

    boolean testAddData();
}


