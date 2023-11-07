package com.dad.springcloud.dao;



import com.dad.springcloud.entities.dao.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface PaymentDao {
    int createPayment(Payment payment);
    int delPaymentById(@Param("id") Long id);
    Payment getPaymentById(@Param("id") Long id );
    ArrayList<Payment>  getPayMentList();
    int upPayment(Payment payment);

}
