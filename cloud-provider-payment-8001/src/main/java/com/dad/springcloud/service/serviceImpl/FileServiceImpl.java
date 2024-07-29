package com.dad.springcloud.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.dad.springcloud.Utils.WordTableToPdf.SoMap;
import com.dad.springcloud.Utils.WordTableToPdf.WordUtil;
import com.dad.springcloud.Utils.wordToPdf.WordToPdfUtil;
import com.dad.springcloud.Utils.wordToPdf.WordUtils;
import com.dad.springcloud.entities.vo.BaseClass;
import com.dad.springcloud.entities.vo.ReportVo;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.spire.doc.FileFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import com.dad.springcloud.Utils.WordTableToPdf.ExportData;
import com.dad.springcloud.service.FileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xwpf.usermodel.*;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

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
            reportVo.setAddress("地址" + i);
            reportVo.setCondition("设备状态" + i);
            reportVo.setTel("手机" + i);
            reportVo.setTransmissibility("测试" + i);
            reportVo.setDemo1("demo1" + i);
            reportVo.setDemo2("demo2" + i);
            reportVo.setDemo3("demo3" + i);
            reportVo.setDemo4("demo4" + i);
            reportVo.setDemo5("demo5" + i);
            reportVo.setDemo6("demo6" + i);
            reportVo.setDemo7("demo7" + i);
            reportVo.setDemo8("demo8" + i);
            reportVo.setDemo9("demo9" + i);
            reportVo.setDemo10("demo10" + i);
            reportVo.setDemo11("demo11" + i);
            reportVo.setDemo12("demo12" + i);
            reportVo.setDemo13("demo13" + i);
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



/*    public static void  main(String[] args) throws IOException, DocumentException {
        String file = "D:\\dailyTestFIle\\toPdf\\testWind07075.pdf";
        String htmlFile="D:\\dailyTestFIle\\statementinfo\\20230707\\020000000042.20231130104152423.html";
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
    }*/


    @Override
    public String getOutwardRemittanceNotice(Map paramMap, HttpServletResponse response) {

        String out = "";
        log.info("FileServiceImpl getOutwardRemittanceNotice begin>>>>>>>>>>>>>>>");
        log.info(paramMap.toString());
        String pdfFileNm = "OutwardRemittanceNotice.pdf";
        DecimalFormat df1 = new DecimalFormat("##,##0.00");
        WordUtils wordUtil = new WordUtils();
        Map<String, Object> params = new HashMap<String, Object>(16);
        params.put("${custNameEN}", paramMap.get("custNameEN"));
        params.put("${custNameCN}", paramMap.get("custNameCN"));
        response.reset();
        response.setCharacterEncoding("utf-8");
        log.info("参数初始化处理完成>>>>>>>>>>>>>>>");
        try {
            //模板文件位置
            String path = "D:/dailyTestFIle/wordToPdf";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(pdfFileNm, "UTF-8"));
            response.setHeader("filename", URLEncoder.encode(pdfFileNm, "UTF-8"));
            out = wordUtil.getWordPrePdf(path, "/Daily-test.docx ", params, new ArrayList<>(), pdfFileNm);
            //Daily-test.docx test1.docx
        } catch (Exception e) {
            log.info("生成PDF文件流异常", e.getMessage());
        }
        //文件上传ftp
        File file = new File(out);
        String date = String.format(DateUtil.today(), "yyyyMMdd");
        //流水号
        String tlSnCd = (String) paramMap.get("tlSnCd");
        String customerId = "020000000032";
        String fileName = "testWordToPdf.pdf";
        String path = "D:/dailyTestFIle/wordToPdf/produce/" + date + "/" + customerId + "/";
        String tmpFileName = path + fileName;

        return out;
    }


    public static void dealWithMsoStatement() {

        HashMap<String, Object> finalMap = new HashMap<>();
        finalMap.put("custNameCN", "帕骐电子(香港)有限公司");
        finalMap.put("custNameEN","PUMPKIN ELECTRONICS (HK) LIMITED");
        finalMap.put("custNo", "020000000176");
        finalMap.put("custAddress", "测试地址");
        finalMap.put("dateStr", "2024年03月01日 至 2024年03月31日" );
        finalMap.put("accNo", "01010123000401");

        ArrayList<Object> balList = CollUtil.newArrayList();
        for (int i = 0; i < 10; i++) {
            // 模拟从mysql查询列表数据
            HashMap<String, Object> workItem = new HashMap<>();
            workItem.put("currency", i + "curr");
            workItem.put("preBalance", 6000.28 + i);
            workItem.put("repPreAvlBal", i + 10 + "小时");
            workItem.put("endBal", "800" + i);
            workItem.put("repEndAvlBal", "1000" + i);
            balList.add(workItem);
        }
       // finalMap.put("balList", balList);
        // 头部模板
        File wordTemplate = new File("D:\\dailyTestFIle\\moban\\MsoStatementTemplate_Head.docx");
        // 此处使用了poi-tl的<表格行循环插件>，此处一定要进行参数bind，方便word模板参数替换
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure build = Configure.builder().bind(policy, "").build();
        XWPFTemplate render = XWPFTemplate.compile(wordTemplate, build).render(finalMap);
        // 此处是利用File，直接在本地创建文件，将参数替换后的文件流写入到该文件，word就是最终的结果
        String fileName = File.separator + IdUtil.getSnowflake(1, 1).nextId() + "-MsoStatementTemplate_Head" + ".docx";
        String path = "D:\\dailyTestFIle\\MSO\\testWordToPdf" + fileName;
        File word = new File(path);
        try {
            render.writeToFile(word.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean midFlag=true;

        List<String> pathList=new ArrayList<>();
        pathList.add(path);
        // 中部标题模板
      /*  String titlePath = "D:\\dailyTestFIle\\moban\\MsoStatementTemplate-MidTitle.docx" ;
        pathList.add(titlePath);*/
        boolean noTradeFlag=false;
        if(midFlag){
        List<String> currList=new ArrayList<>();
        currList.add("EUR 欧元");
        currList.add("USD 美元");
        for(String curr:currList){
            finalMap.put("Curr", curr);
            ArrayList<Object> currDetailList = CollUtil.newArrayList();
            for (int i = 0; i < 23; i++) {
                // 模拟从mysql查询列表数据
                HashMap<String, Object> workItem = new HashMap<>();
                workItem.put("index", i );
                workItem.put("tradeTime", 6000.28 + i);
                workItem.put("crAmount", i + 10 + "小时");
                workItem.put("drAmount", "800" + i);
                workItem.put("accountBal", "1000" + i);
                workItem.put("tradeDesc", "1000" + i);
                workItem.put("remark", "1000" + i);
                workItem.put("oppositeAccountName", "1000" + i);
                currDetailList.add(workItem);
            }
            finalMap.put("currList", currDetailList);


            // 中间模板
            File wordTemplate2 = new File("D:\\dailyTestFIle\\moban\\MsoStatementTemplate-MidPure.docx");
            // 此处使用了poi-tl的<表格行循环插件>，此处一定要进行参数bind，方便word模板参数替换
            LoopRowTableRenderPolicy policy2 = new LoopRowTableRenderPolicy();
            Configure build2 = Configure.builder().bind(policy2, "currList").build();
            XWPFTemplate render2 = XWPFTemplate.compile(wordTemplate2, build2).render(finalMap);
            // 此处是利用File，直接在本地创建文件，将参数替换后的文件流写入到该文件，word就是最终的结果
            String fileName2 = File.separator + IdUtil.getSnowflake(1, 1).nextId() + "-MsoStatement-MidPure" + ".docx";
            String purePath = "D:\\dailyTestFIle\\MSO\\testWordToPdf" + fileName2;
            pathList.add(purePath);
            File word2 = new File(purePath);
            try {
                render2.writeToFile(word2.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

         }
        }else {
            //无交易模板
            String noTradePath="D:\\dailyTestFIle\\moban\\MsoStatementTemplate-MidNoTrade.docx";
            pathList.add(noTradePath);
            noTradeFlag=true;
        }
        // 尾模板
        String endPath = "D:\\dailyTestFIle\\moban\\MsoStatementTemplate-Last.docx" ;
        //gatherDoc(path,path1,path2,path3);
        pathList.add(endPath);
        gatherDoc2(pathList ,noTradeFlag);

    }


    /**
     * 浏览器下载指定文件
     *
     * @param path        完整物理路径
     * @param setFileName 文件名称
     * @param isDel       导出后是否删除磁盘文件,方法调用之前的这个文件的流必须先关闭掉才有生效
     * @return
     */
    public static HttpServletResponse downloadFile(HttpServletResponse response, String path, String setFileName, boolean isDel) {
        InputStream fis = null;
        OutputStream toClient = null;
        File file = null;
        try {
            // path是指欲下载的文件的路径。
            file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            if (StringUtils.isEmpty(setFileName)) {
                setFileName = filename;
            }
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件。
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + java.net.URLDecoder.decode(setFileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (toClient != null) {
                try {
                    toClient.flush();
                    toClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //下载之后删除
            try {
                if (isDel && file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }


    public static void main(String[] args) {


        dealWithMsoStatement();

    }


    //复制文档中的段落
    private static void copyParagraphs(XWPFDocument sourceDoc, XWPFDocument mergedDoc) {
        List<XWPFParagraph> paragraphs = sourceDoc.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            XWPFParagraph newParagraph = mergedDoc.createParagraph();
            newParagraph.getCTP().setPPr(paragraph.getCTP().getPPr());
            newParagraph.getCTP().setRArray(paragraph.getCTP().getRArray());

        }
    }

        //复制文档中的表格
    private static void copyTables(XWPFDocument sourceDoc, XWPFDocument mergedDoc) {
        List<XWPFTable> tables = sourceDoc.getTables();
        for (XWPFTable table : tables) {
            XWPFTable newTable = mergedDoc.createTable();
            newTable.getCTTbl().setTblPr(table.getCTTbl().getTblPr());
            newTable.getCTTbl().setTrArray(table.getCTTbl().getTrArray());
        }
    }

    //两个文档合并
    public  void gatherWord(String path,String path1){
        //合开的日标文件
        String targetFile = "D:\\dailyTestFIle\\MSO\\testWordToPdf\\MsoStatementTemplate-merged.docx";
        try {
            //创建一个新的空白文档
            XWPFDocument mergedDoc = new XWPFDocument();
            // 创建或获取文档的body部分
//            CTBody body = mergedDoc.getDocument().getBody();
//
//            // 确保body有一个section属性（通常应该已经有一个）
//            if (body.getSectPr() == null) {
//                body.addNewSectPr();
//            }
//
//            CTSectPr sectPr = body.getSectPr();
//
//            // 创建或获取page size设置
//            if (sectPr.getPgSz() == null) {
//                sectPr.addNewPgSz();
//            }
//
//            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz pgSz = sectPr.getPgSz();
//
//        // 设置纸张大小（A4）
//            pgSz.setW(BigInteger.valueOf(Math.round(210 * 36000 / 25.4))); // 转换为twips（1/20点）
//            pgSz.setH(BigInteger.valueOf(Math.round(297 * 36000 / 25.4)));
//
//            // 设置页面方向（可选）
//            pgSz.setOrient(STPageOrientation.PORTRAIT); // 默认为PORTRAIT，但你可以设置为LANDSCAPE
//
//            // 设置页边距（可选）
//            CTPageMar pgMar = sectPr.addNewPgMar();
//            pgMar.setTop(BigInteger.valueOf(720)); // 上边距，以twips为单位（720twips = 0.5英寸）
//            pgMar.setRight(BigInteger.valueOf(720)); // 右边距
//            pgMar.setBottom(BigInteger.valueOf(720)); // 下边距
//            pgMar.setLeft(BigInteger.valueOf(720)); // 左边距


            //需要合并的文档列表
            String[] sourceFiles = {path, path1};
            for (String sourceFile : sourceFiles) {
                //读取需要合并的文档
                FileInputStream fis = new FileInputStream(sourceFile);
                XWPFDocument sourceDoc = new XWPFDocument(fis);

                //遍历文档的段落和表格,将其内容复制到新文档中
                copyParagraphs(sourceDoc, mergedDoc);
                copyTables(sourceDoc,mergedDoc);
                //关闭输入流
                fis.close();
                //保存新文档
                FileOutputStream fos = new FileOutputStream(targetFile);
                mergedDoc.write(fos);
                fos.close();
                System.out.println("合并完成!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    public static void gatherDoc(String path, String path1,String path2,String path3){
        //创建Document对象并加载一个示例文档
         com.spire.doc.Document document = new com.spire.doc.Document(path);

        //将另一个Word文档完全插入到文档中
        document.insertTextFromFile(path1, FileFormat.Docx_2013);
        document.insertTextFromFile(path2, FileFormat.Docx_2013);
        document.insertTextFromFile(path3, FileFormat.Docx_2013);
        //保存结果文档
        document.saveToFile("D:\\dailyTestFIle\\MSO\\testWordToPdf\\MsoStatement.docx", FileFormat.Docx_2013);

    }

    public static void gatherDoc2(List<String> path ,boolean noTradeFlag){
        //创建两个Document对象并加载两个示例文档
        String ct=DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        //遍历第二个文档，获取所有节
//        List<com.spire.doc. Document> docList=new ArrayList<>();

        com.spire.doc. Document document1 = new com.spire.doc.Document(path.get(0));
        for( int i=1;i<path.size();i++){
            com.spire.doc. Document d = new com.spire.doc.Document(path.get(i));
            for (Object sectionObj : d.getSections()) {
                com.spire.doc.Section sec=(com.spire.doc.Section)sectionObj;
                //遍历第二个文档的所有节，获取其子对象
                for (Object docObj : sec.getBody().getChildObjects()) {
                    com.spire.doc. DocumentObject obj=(com.spire.doc.DocumentObject)docObj;
                    //获取第一个文档的最后一节
                    com.spire.doc.Section lastSection = document1.getLastSection();
                    //将子对象添加到第一个文档的最后一节中
                    com.spire.doc.Body body = lastSection.getBody();
                    body.getChildObjects().add(obj.deepClone());
                }
            }

        }
        path.remove(path.size()-1);
        if(noTradeFlag){
            path.remove(path.size()-1);
        }
        delFile(path);

        //保存结果文档
        String newPath="D:\\dailyTestFIle\\MSO\\testWordToPdf\\"+ct+"-MsoStatement-NEW.docx";
        document1.saveToFile(newPath, FileFormat.Docx_2013);
        String pdfPath="D:\\dailyTestFIle\\MSO\\testWordToPdf\\"+ct+"-MsoStatement-NEW.pdf";
        WordToPdfUtil wp=new WordToPdfUtil();
        try{
            wp.word2Pdf2(newPath,pdfPath);
        }catch (Exception e){
                e.printStackTrace();
        }

    }

       public static void delFile(List<String> pathList){
           // 指定要删除的文件路径
           //String filePath = "你的文件路径"; // 替换为你的文件路径
            for(String filePath:pathList){
                // 创建Path对象
                Path path = Paths.get(filePath);

                try {
                    // 尝试删除文件
                    boolean deleted = Files.deleteIfExists(path);

                    // 输出结果
                    if (deleted) {
                        System.out.println("文件删除成功！");
                    } else {
                        System.out.println("文件不存在或删除失败。");
                    }
                } catch (Exception e) {
                    // 处理任何异常
                    e.printStackTrace();
                    System.out.println("删除文件时发生异常：" + e.getMessage());
                }

            }


       }


}

