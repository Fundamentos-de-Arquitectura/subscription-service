package upc.edu.pe.foodflow.subscription.domain.service;


import upc.edu.pe.foodflow.subscription.application.dto.PaymentData;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentResult;

public interface PaymentClient {



    PaymentResult processPayment(PaymentData data);
}