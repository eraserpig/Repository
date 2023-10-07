package com.dad.springcloud.Utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author gmq
 * @Date 20230828
 * @Description
 **/
@Slf4j
public class PdfKit {




    private PdfKit() {
    }

    /**
     * Creates a PDF with the words
     *
     * @param html
     * @param file
     * @throws IOException
     * @throws DocumentException
     */
    public static void creatHtmlpdf(String html, String file) throws IOException, DocumentException {
        // step 1 new Document 默认大小A4
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(file));
        // step 3
        document.open();
        // step 4
        Paragraph context = new Paragraph();
        ElementList elementList = parseToElementList(html, null);
        for (Element element : elementList) {
            context.add(element);
        }
        document.add(context);
        // step 5
        document.close();
    }

    /**
     * 设置字体信息
     * @return
     */
    private static Font getFontInf() {
        String path = PdfKit.class.getClassLoader().getResource("").getPath();

            //执行后rootPath 为/D:/Workspace/tourservice/WebContent

        String rootPath = path.substring(0, path.indexOf("/WEB-INF/"));//rootPath为web项目的根目录。
        // 字体路径
        String fontPath =  rootPath + "/WEB-INF/resources/fonts/simsunb.ttf";
        BaseFont baseFont = null;
        Font font = null;
        try {
            // 设置字体路径,字体编码，是否将字体嵌入pdf（默认false）
            baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // 设置默认字体数据
            font = new Font(baseFont, 12f,Font.NORMAL,BaseColor.BLACK);
        } catch (DocumentException e) {
            log.error("get pdf font info DocumentException " , e );
        } catch (IOException e) {
            log.error("get pdf font info IOException " , e );
        }
        return font;
    }

    /**
     * html转pdf 写法
     * @param html
     * @param css
     * @return
     * @throws IOException
     */
    public static ElementList parseToElementList(String html, String css) throws IOException {
        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        if (css != null) {
            CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes()));
            cssResolver.addCss(cssFile);
        }
        String fontPath =  PdfKit.class.getClassLoader().getResource("simhei,ttf").getPath();
        // HTML
        MyFontsProvider fontProvider = new MyFontsProvider();
        fontProvider.register(fontPath);
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);

        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

        // XML Worker
        XMLWorker worker = new XMLWorker(cssPipeline, true);
        XMLParser p = new XMLParser(worker);
        html = html.replace("<br>", "").replace("<hr>", "").replace("<img>", "").replace("<param>", "").replace("<link>", "");
        p.parse(new ByteArrayInputStream(html.getBytes()));
        return elements;
    }

    static class MyFontsProvider extends XMLWorkerFontProvider {
        public MyFontsProvider() {
            super(null, null);
        }

        @Override
        public Font getFont(final String fontname, String encoding, float size, final int style) {
            return getFontInf();
        }
    }













}
