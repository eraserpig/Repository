package com.dad.springcloud.service;

/**
 * @Author gmq
 * @Date 2023-10-07
 * @Description
 **/

public interface FileService {
    byte[] getWordByte(String startDate, String endDate, String type);
}
