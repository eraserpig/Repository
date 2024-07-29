package com.dad.springcloud.Utils.wordToPdf;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.dad.springcloud.Utils.CustomXWPFDocument;
import com.dad.springcloud.Utils.WordTableToPdf.ExportData;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************
 * 通过word模板生成新的word工具类
 * @Package com.cccuu.project.myUtils
 * @Author duan
 * @Date 2018/3/29 14:24
 * @Version V1.0
 *******************************************/
public class WordUtils {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据模板生成word
     * @param path     模板的路径
     * @param params   需要替换的参数
     * @param tableList   需要插入的参数
     * @param fileName 生成word文件的文件名
     * @param response
     */
    public ServletOutputStream getWord(String path ,String filename,Map<String, Object> params, List<String[]> tableList, String fileName, HttpServletResponse response) throws Exception {
        File file = new File(path+filename);
        InputStream is = new FileInputStream(file);
        CustomXWPFDocument doc = new CustomXWPFDocument(is);
        this.replaceInPara(doc, params);    //替换文本里面的变量
        this.replaceInTable(doc, params, tableList); //替换表格里面的变量
        ServletOutputStream os = response.getOutputStream();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        doc.write(os);
        this.close(os);
        this.close(is);
        return os;
    }

    /**
     * 根据模板生成word
     * @param path     模板的路径
     * @param params   需要替换的参数
     * @param tableList   需要插入的参数
     * @param fileName 生成word文件的文件名
     */
    public String getWordPrePdf(String path,String fileNme, Map<String, Object> params, List<String[]> tableList, String fileName ) throws Exception {
        // 验证License 若不验证则转化出的PDF文档会有水印产生
        if (!getLicense()) {
            logger.warn("====未获取到License信息，系统无法处理！====");
            return null;
        }
        File file = new File(path + fileNme);
        InputStream is = new FileInputStream(file);
        CustomXWPFDocument doc = new CustomXWPFDocument(is);
        logger.info(">>>>>>>>开始替换文本里的变量========");
        // 替换文本里面的变量
        this.replaceInPara(doc, params);
        logger.info(">>>>>>>>开始替换表格里的变量========");
        // 替换表格里面的变量
        this.replaceInTable(doc, params, tableList);
        OutputStream os = null;
        String rand=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String tmpPath = path +"/tmpFile/" + rand ;
        String pdfPath = path + File.separator + rand + fileName ;
        logger.info(">>>>>>>>创建临时文件，路径为：{} ========",tmpPath);
        logger.info(">>>>>>>>创建PDF文件，路径为：{} ========",pdfPath);
        try {
            os = new BufferedOutputStream(new FileOutputStream(pdfPath));
            FileOutputStream fos = new FileOutputStream(tmpPath);
            doc.write(fos);
            fos.flush();
            fos.close();
            logger.info(">>>>>>>>写入完成 ========");
            Document pdfDoc = null;
            logger.info(">>>>>>>>初始化PDF文件 ========");
            pdfDoc = new Document(tmpPath);
            logger.info(">>>>>>>>开始转化PDF文件 ========");
            // 全面支持 DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            pdfDoc.save(os, SaveFormat.PDF);
            logger.info(">>>>>>>>转化PDF文件完成 ========");
        } catch (Exception e) {
            logger.error(">>>>>>>>创建PDF文件异常，异常信息为： {} ========",e.getMessage());
        }finally {
            logger.info(">>>>>>>>关闭流处理 ========");
            try {
                if(os != null){
                    os.close();
                }
            } catch (IOException e) {
                logger.error(">>>>>>>>关闭流异常，异常信息为： {} ========",e.getMessage());
            }
        }
        this.close(os);
        this.close(is);
        logger.info(">>>>>>>>返回PDF文件路径为：{} ========",pdfPath);
        return pdfPath;
    }

    /**
     * 替换段落里面的变量
     * @param doc    要替换的文档
     * @param params 参数
     */
    private void replaceInPara(CustomXWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            this.replaceInPara(para, params, doc);
        }
    }

    /**
     * 替换段落里面的变量
     *
     * @param para   要替换的段落
     * @param params 参数
     */
    private void replaceInPara(XWPFParagraph para, Map<String, Object> params, CustomXWPFDocument doc) {
        List<XWPFRun> runs;
        Matcher matcher;
        while(this.matcher(para.getParagraphText()).find()){
            runs = para.getRuns();
            replaceInPara(para, params, doc, runs);
        }
//        if (this.matcher(para.getParagraphText()).find()) {
//
//        }
    }

    private void replaceInPara(XWPFParagraph para, Map<String, Object> params, CustomXWPFDocument doc,List<XWPFRun> runs){
        int start = -1;
        int end = -1;
        String str = "";
        StringBuilder fixStr=new StringBuilder();
//        CTRPr ctrpr = null;
//        if(runs!=null){
//        	ctrpr= runs.get(0).getCTR().getRPr();
//        }
        String fontFamily = "";
        int fontSize = 0;
        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            fontFamily = run.getFontFamily();
            fontSize = run.getFontSize();
            String runText = run.toString();
            if ('$' == runText.charAt(0) && '{' == runText.charAt(1)) {
                start = i;
            }
            if ((start != -1)) {
                fixStr=fixStr.append(runText) ;
            }
            if ('}' == runText.charAt(runText.length() - 1)) {
                if (start != -1) {
                    end = i;
                    break;
                }
            }
        }
        str=String.valueOf(fixStr);

        for (int i = start; i <= end; i++) {
            para.removeRun(i);
            i--;
            end--;
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            if (str.indexOf(key) != -1) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    str = str.replace(key, value.toString());
                    XWPFRun run = para.createRun();
                    run.setText(str, 0);
                    run.setFontFamily(fontFamily);
                    run.setFontSize(fontSize);
//                    run.getCTR().setRPr(ctrpr);
                    break;
                } else if (value instanceof Map) {
                    str = str.replace(key, "");
                    Map pic = (Map) value;
                    int width = Integer.parseInt(pic.get("width").toString());
                    int height = Integer.parseInt(pic.get("height").toString());
                    int picType = getPictureType(pic.get("type").toString());
                    byte[] byteArray = (byte[]) pic.get("content");
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                    try {
                        //int ind = doc.addPicture(byteInputStream,picType);
                        //doc.createPicture(ind, width , height,para);
                        doc.addPictureData(byteInputStream, picType);
                        String ind = doc.addPictureData(byteInputStream, picType);
                       // doc.createPicture(doc.getAllPictures().size() - 1, width, height, para);
                        //doc.createPicture(run, ind, doc.getNextPicNameNumber(picType), width, height);
                        para.createRun().setText(str, 0);
                        break;
                    } catch (Exception e) {
                        logger.error("图片处理异常，异常信息为：{} ",e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 为表格插入数据，行数不够添加新行
     *
     * @param table     需要插入数据的表格
     * @param tableList 插入数据集合
     */
    private static void insertTable(XWPFTable table, List<String[]> tableList) {
        //创建行,根据需要插入的数据添加新行，不处理表头
        for (int i = 0; i < tableList.size(); i++) {
            XWPFTableRow row = table.createRow();
        }
        //遍历表格插入数据
        List<XWPFTableRow> rows = table.getRows();
        int length = table.getRows().size();
        for (int i = 1; i < length - 1; i++) {
            XWPFTableRow newRow = table.getRow(i);
            List<XWPFTableCell> cells = newRow.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                String s = tableList.get(i - 1)[j];
                cell.setText(s);
            }
        }
    }

    /**
     * 替换表格里面的变量
     * @param doc    要替换的文档
     * @param params 参数
     */
    private void replaceInTable(CustomXWPFDocument doc, Map<String, Object> params, List<String[]> tableList) {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext()) {
            table = iterator.next();
            if (table.getRows().size() > 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (this.matcher(table.getText()).find()) {
                    rows = table.getRows();
                    for (XWPFTableRow row : rows) {
                        cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            paras = cell.getParagraphs();
                            for (XWPFParagraph para : paras) {
                                this.replaceInPara(para, params, doc);
                            }
                        }
                    }
                } else {
                    insertTable(table, tableList);  //插入数据
                }
            }
        }
    }


    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }


    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType) {
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if (picType.equalsIgnoreCase("png")) {
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib")) {
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf")) {
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf")) {
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("关闭输入流发生 IO 异常，异常信息为：{} ",e.getMessage());
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                logger.error("关闭输出流发生 IO 异常，异常信息为：{} ",e.getMessage());
            }
        }
    }

    public  boolean getLicense() {
        boolean result = false;
        try {
            InputStream is =this.getClass().getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            logger.error("读取License文件异常，异常信息为：{} ",e.getMessage());
        }
        return result;
    }


    /**
     * 获取 Word 模板的两个操作对象 IXDocReport 和 IContext
     * @param path 模板绝对地址
     * @return 模板数据对象
     */
    public static ExportData createExportData(String path) {
        try {
            IXDocReport report = createReport(path);
            IContext context = report.createContext();
            return new ExportData(report, context);
        } catch (XDocReportException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 加载模板的方法，主要是指定模板的路径和选择渲染数据的模板
     * @param url 模板相对于类路径的地址
     * @return word 文档操作类
     */
    private static IXDocReport createReport(String url) {
        try (
                InputStream in = new  FileInputStream(url);
        ) {
            IXDocReport ix = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);
            return ix;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


}