package com.dad.springcloud.service.serviceImpl;

import com.dad.springcloud.Utils.WordTableToPdf.SoMap;
import com.dad.springcloud.Utils.WordTableToPdf.WordUtil;
import com.dad.springcloud.entities.vo.BaseClass;
import com.dad.springcloud.entities.vo.ReportVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;
import com.dad.springcloud.Utils.WordTableToPdf.ExportData;
import com.dad.springcloud.service.FileService;
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


}
