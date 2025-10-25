package upc.edu.pe.foodflow.subscription.infrastructure.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentData;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentResult;
import upc.edu.pe.foodflow.subscription.domain.service.PaymentClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación real que integrará con la API REST de Izipay.
 * Por ahora, la llamada está preparada pero no activa.
 */
@Component
public class IzipayPaymentClient implements PaymentClient {

    @Value("${izipay.api.url}")
    private String apiUrl;

    @Value("${izipay.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PaymentResult processPayment(PaymentData data, BigDecimal amount) {
        try {
            // Construir payload según API Izipay
            Map<String, Object> request = new HashMap<>();
            request.put("amount", amount);
            request.put("cardNumber", data.cardNumber());
            request.put("cardHolder", data.cardHolder());
            request.put("expirationDate", data.expirationDate());
            request.put("cvv", data.cvv());

            // TODO: reemplazar por endpoint real de Izipay
            String endpoint = apiUrl + "/v1/payments";

            // TODO: agregar headers con tu apiKey
            // ResponseEntity<String> response = restTemplate.postForEntity(endpoint, request, String.class);

            // TODO: parsear respuesta real de Izipay
            String fakeTransactionId = "pending-integration"; // temporal

            return PaymentResult.success(fakeTransactionId);

        } catch (Exception e) {
            return PaymentResult.failure("Error processing the payment with Izipay: " + e.getMessage());
        }
    }
}
