package com.dad.springcloud.service;


import com.dad.springcloud.entities.dao.Payment;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

public interface PaymentService {
    int createPayment(Payment payment);
    int delPaymentById(Long id);
    Payment getPaymentById(Long id );
    ArrayList<Payment> getPayMentList();
    int upPayment(Payment payment);

    String getOutwardRemittanceNotice(Map requestJson, HttpServletResponse response);
}
