package com.dad.springcloud.service.serviceImpl;

import com.dad.springcloud.Utils.HtmlToPdfUtils;
import com.dad.springcloud.Utils.WordTableToPdf.SoMap;
import com.dad.springcloud.Utils.WordTableToPdf.WordUtil;
import com.dad.springcloud.entities.vo.BaseClass;
import com.dad.springcloud.entities.vo.ReportVo;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;
import com.dad.springcloud.Utils.WordTableToPdf.ExportData;
import com.dad.springcloud.service.FileService;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;

/**
 * @Author gmq
 * @Date 2023-10-07
 * @Description
 **/
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    /**
     * 模板文件的地址
     */
    @Value("${report.path}")
    private String MODEL_PATH_DAY;

    @Override
    public byte[] getWordByte(String startDate, String endDate, String type) {
        //准备数据
        ExportData evaluation = WordUtil.createExportData(MODEL_PATH_DAY);

        //时间设置
        String[] date = getDate(startDate);
        evaluation.setData("beginYear", date[0]);
        evaluation.setData("beginMonth", date[1]);
        evaluation.setData("beginDay", date[2]);
        String[] dateEnd = getDate(endDate);
        evaluation.setData("endYear", dateEnd[0]);
        evaluation.setData("endMonth", dateEnd[1]);
        evaluation.setData("endDay", dateEnd[2]);
        evaluation.setData("videoNum", "10");


        //设置视频监控状态
        List<SoMap> videoList = new ArrayList<SoMap>();
        for (int i = 0; i < 10; i++) {
            ReportVo reportVo = new ReportVo();
            reportVo.setName("园区名称" + i);
            reportVo.setPark("园区" + i);
            reportVo.setAddress("地址"+i);
            reportVo.setCondition("设备状态" + i);
            reportVo.setTel("手机"+i);
            reportVo.setTransmissibility("测试"+i);
            reportVo.setDemo1("demo1"+i);
            reportVo.setDemo2("demo2"+i);
            reportVo.setDemo3("demo3"+i);
            reportVo.setDemo4("demo4"+i);
            reportVo.setDemo5("demo5"+i);
            reportVo.setDemo6("demo6"+i);
            reportVo.setDemo7("demo7"+i);
            reportVo.setDemo8("demo8"+i);
            reportVo.setDemo9("demo9"+i);
            reportVo.setDemo10("demo10"+i);
            reportVo.setDemo11("demo11"+i);
            reportVo.setDemo12("demo12"+i);
            reportVo.setDemo13("demo13"+i);
            reportVo.setValue(new BaseClass(2));
            reportVo.setValue(new BaseClass("2"));
            reportVo.setValue(new BaseClass(Float.valueOf("3")));
            videoList.add(new SoMap(reportVo));
        }
        evaluation.setTable("video", videoList);
        // 获取新生成的文件流
        byte[] data = evaluation.getByteArr();


        return data;
    }

    private String[] getDate(String date) {
        if (date.contains(" ")) {

            return date.split("-| ");
        } else {
            return date.split("-");

        }
    }



    public static void testHtmlToPdf(String[] args) throws IOException, DocumentException {
        String file = "D:\\dailyTestFIle\\toPdf\\testWind1108.pdf";
        String htmlFile="D:\\dailyTestFIle\\statementinfo\\20230707\\020000000176.20231031183032521.html";
        String waterMarkText =  "";
        InputStream inputStream=new FileInputStream(htmlFile);
        //微软雅黑在windows系统里的位置如下，linux系统直接拷贝该文件放在linux目录下即可
        BaseFont bf = null;
        ApplicationHome home = new ApplicationHome(PaymentServiceImpl.class);
        File jarFile = home.getDir();
        String    rootPath = jarFile.getPath();
        String   mouldPath = rootPath.substring(0,rootPath.indexOf("cloud-provider-payment-8001")+"cloud-provider-payment-8001".length())+"\\src\\main\\resources\\Fonts";
        mouldPath = mouldPath.replace("\\","/");
        HtmlToPdfUtils.convertToPdf(inputStream, waterMarkText, mouldPath, new FileOutputStream(file));
    }








}
