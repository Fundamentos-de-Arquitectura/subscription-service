package upc.edu.pe.foodflow.subscription.domain.service;

import upc.edu.pe.foodflow.subscription.application.dto.PaymentData;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentResult;
import java.math.BigDecimal;

public interface PaymentClient {
    PaymentResult processPayment(PaymentData data, BigDecimal amount);
}
