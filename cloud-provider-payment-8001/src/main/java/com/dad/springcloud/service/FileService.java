package com.dad.springcloud.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author gmq
 * @Date 2023-10-07
 * @Description
 **/

public interface FileService {
    byte[] getWordByte(String startDate, String endDate, String type);

    String getOutwardRemittanceNotice(Map requestJson, HttpServletResponse response);
}
