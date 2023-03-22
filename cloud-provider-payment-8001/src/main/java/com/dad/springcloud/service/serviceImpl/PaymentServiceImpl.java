package com.dad.springcloud.service.serviceImpl;

import com.dad.springcloud.dao.PaymentDao;
import com.dad.springcloud.entities.dao.Payment;
import com.dad.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class PaymentServiceImpl implements  PaymentService{
    @Resource
    private PaymentDao paymentDao;


    public int createPayment(Payment payment){
        int a=paymentDao.createPayment(payment);

        return a ;
    }


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
}
