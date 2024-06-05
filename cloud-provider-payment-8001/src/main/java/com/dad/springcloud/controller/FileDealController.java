package com.dad.springcloud.controller;


import com.dad.springcloud.Utils.WordTableToPdf.ResUtil;
import com.dad.springcloud.service.FileService;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @Author gmq
 * @Date 2023-10-07
 * @Description
 **/
@RestController
@RequestMapping("/fileFix")
@Slf4j
public class FileDealController {
    @Resource
    FileService fileService;
    @GetMapping("/exportReportExcel")
    @ApiOperation("导出数据分析报告")
    public Object exportReportExcel (HttpServletRequest request,
                                    @RequestParam(value = "startDate", required = false) String startDate,
                                    @RequestParam(value = "endDate", required = false) String endDate,
                                    @RequestParam(value = "type", required = true) String type) {
        log.info("数据分析报告导出 开始时间：" + startDate + " 结束时间：" + endDate + " 类型：" + type);
        try {
            byte[] data = fileService.getWordByte(startDate, endDate, type);
            InputStream sbs = new ByteArrayInputStream(data);
            String file = "D:\\dailyTestFIle\\wordToPdf\\testWind.pdf";
            //保存生成word
            return ResUtil.getStreamData(request, data, "wordReportExport-" + type, "docx");

        }catch (Exception e){
            log.info("-exportReportExcel-e:{}", e);
        }

        return null;
    }



    @ApiOperation("下载文件")
    @PostMapping("/getOutwardRemittance")
    public String getOutwardRemittance(@RequestBody Map requestJson, HttpServletResponse response) {
        log.info("下载出款通知单");
        return fileService.getOutwardRemittanceNotice(requestJson, response);
    }





}
