package upc.edu.pe.foodflow.subscription.infrastructure.payment;

import upc.edu.pe.foodflow.subscription.application.dto.PaymentData;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentResult;
import upc.edu.pe.foodflow.subscription.domain.service.PaymentClient;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class IzipayPaymentClient implements PaymentClient {

    @Override
    public PaymentResult processPayment(PaymentData data) {
        if (data.cardNumber() == null || data.cardNumber().isBlank()) {
            return PaymentResult.failure("Card number is invalid!");
        }

        String transactionId = UUID.randomUUID().toString();
        return PaymentResult.success(transactionId);
    }
}
