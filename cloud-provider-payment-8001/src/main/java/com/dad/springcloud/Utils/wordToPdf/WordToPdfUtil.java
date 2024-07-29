package com.dad.springcloud.Utils.wordToPdf;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class WordToPdfUtil {
    public Boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            log.error("======license异常", e);
        }
        return result;
    }

    /**
     * inpath: 输入word的路径，例如： C:\\TEST.doc
     * outpath: 输出pdf的路径，例如： C:\\TEST.pdf
     */
    public void word2Pdf2(String inpath, String outpath) throws Exception {
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            log.warn("======未获取到License信息，系统无法处理！");
            return;
        }

        long old = System.currentTimeMillis();
        File file = new File(outpath);
        FileOutputStream os = new FileOutputStream(file);
        // 解决乱码
        // 如果是windows执行，不需要加这个
        // TODO 如果是linux执行，需要添加这个*****
        // FontSettings.setFontsFolder("/usr/share/fonts",true);
        // inpath是将要被转化的word文档
        Document doc = new Document(inpath);
        //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
        doc.save(os, SaveFormat.PDF);
        long now = System.currentTimeMillis();
        log.info("共耗时：" + ((now - old) / 1000.0) + "秒");
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param path      pdf输出路径
     * @param wordInput word输入流
     * @param wordName  word文档的名称
     */
    public void word2pdf(String path, InputStream wordInput, String wordName) throws FileNotFoundException {
        if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
            log.error("非法------------");
            return;
        }

        long old = System.currentTimeMillis();
        File file = new File(path + wordName + ".pdf");  //新建一个空白pdf文档
        FileOutputStream os = new FileOutputStream(file);

        Document doc = null;                    //Address是将要被转化的word文档
        try {
            doc = new Document(wordInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (doc != null) {
                doc.save(os, SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long now = System.currentTimeMillis();
        log.info("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
    }

}
