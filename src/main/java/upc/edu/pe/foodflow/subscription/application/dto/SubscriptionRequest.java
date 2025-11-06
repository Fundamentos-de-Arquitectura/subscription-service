package upc.edu.pe.foodflow.subscription.application.dto;

public record SubscriptionRequest(
        String planName,
        PaymentData paymentData
) {
}
