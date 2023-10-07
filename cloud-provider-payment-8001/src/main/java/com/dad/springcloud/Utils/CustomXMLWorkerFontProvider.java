package com.dad.springcloud.Utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author gmq
 * @Date 20230828
 * @Description
 **/
@Slf4j
public class CustomXMLWorkerFontProvider extends XMLWorkerFontProvider {

    @Override
    public Font getFont(final String fontName, final String encoding, final boolean embedded, final float size, final int style,
                        final BaseColor color) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            Font font = new Font(bf, size, style, color);
            font.setColor(color);
            // log.info("PDF文档字体初始化完成!");
            return font;
        } catch (Exception e) {
            log.error("exception:", e);
        }
        return null;
    }

    private void convertToPDF(PdfWriter writer, Document document, String htmlString) {
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer,
                    document,
                    new ByteArrayInputStream(htmlString.getBytes()),
                    XMLWorkerHelper.class.getResourceAsStream("/default.css"),
                    Charset.forName("UTF-8"),
                    new CustomXMLWorkerFontProvider());
        } catch (IOException e) {
            e.printStackTrace();
            log.info("PDF文件生成异常", e);
        } finally {
            document.close();
        }

    }

}


