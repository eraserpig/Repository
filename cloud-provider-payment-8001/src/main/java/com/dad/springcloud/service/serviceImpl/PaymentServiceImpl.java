package com.dad.springcloud.service.serviceImpl;

import com.dad.springcloud.Utils.WordUtilsA;
import com.dad.springcloud.dao.PaymentDao;
import com.dad.springcloud.entities.dao.Payment;
import com.dad.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements  PaymentService{
    @Resource
    private PaymentDao paymentDao;

    //private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${file.path}")
    private String filePath;


    @Override
    public int createPayment(Payment payment){
        int a=paymentDao.createPayment(payment);
        return a ;
    }


    @Override
    public int delPaymentById(Long id) {

        return paymentDao.delPaymentById(id);
    }
    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }

    @Override
    public ArrayList<Payment> getPayMentList() {
        ArrayList<Payment> paymentList= paymentDao.getPayMentList();
        return paymentList;
    }

    @Override
    public int upPayment(Payment payment) {

        return paymentDao.upPayment(payment);
    }

    @Override
    public String getOutwardRemittanceNotice(Map paramMap,HttpServletResponse response) {

        String out = "";
        log.info("FileServiceImpl getOutwardRemittanceNotice begin>>>>>>>>>>>>>>>");
        log.info(paramMap.toString());
        String pdfFileNm = "OutwardRemittanceNotice.pdf";
        DecimalFormat df1 = new DecimalFormat("##,##0.00");
        WordUtilsA wordUtil=new WordUtilsA();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custEngNm", paramMap.get("custEngNm"));
        params.put("custChnNm", paramMap.get("custChnNm"));
        params.put("othrAcctnm", paramMap.get("othrAcctnm"));
        params.put("othrFnncBrchNoteCd", paramMap.get("othrFnncBrchNoteCd"));
        params.put("trdOppntAcct", paramMap.get("trdOppntAcct"));
        params.put("trdDt", paramMap.get("trdDt"));
        params.put("currCd", paramMap.get("currCd"));
        params.put("handcrg", df1.format(new BigDecimal(paramMap.get("handcrg")+"")).toString());
        params.put("custAcctno1", paramMap.get("custAcctno1"));
        params.put("acctBal", df1.format(new BigDecimal(paramMap.get("acctBal")+"")).toString());
        params.put("mmoInfo", paramMap.get("mmoInfo"));
        params.put("trdAmt", df1.format(new BigDecimal(paramMap.get("trdAmt")+"")).toString());
        params.put("handcrgCurr", paramMap.get("handcrgCurr"));
        response.reset();
//		response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        log.info("参数初始化处理完成>>>>>>>>>>>>>>>");
        try{
            String path= filePath;// +  "/SFCCKTZ.docx";  //模板文件位置
            path="E:\\haierPro\\HceRepo\\02项目管理\\17SFC线上化\\03系统设计\\模板文件";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(pdfFileNm, "UTF-8"));
            response.setHeader("filename",URLEncoder.encode(pdfFileNm, "UTF-8"));
            out = wordUtil.changWord(path+"/SFCCKTZ.docx",params,new ArrayList<>(),path+"/output/"+pdfFileNm);

        }catch(Exception e){
            log.info("生成PDF文件流异常",e.getMessage());
        }
        log.info("====生成PDF文件流完成 >>>>>>>>>>>>>>>");
        return out;
    }




}
