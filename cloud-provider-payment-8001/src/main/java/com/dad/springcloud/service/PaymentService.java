package com.dad.springcloud.service;


import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface PaymentService {
    int createPayment(Payment payment);
    int delPaymentById(Long id);
    Payment getPaymentById(Long id );
    ArrayList<Payment> getPayMentList();
    int upPayment(Payment payment);

}
