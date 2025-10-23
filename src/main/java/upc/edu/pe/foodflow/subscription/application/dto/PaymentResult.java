package upc.edu.pe.foodflow.subscription.application.dto;

public record PaymentResult(
        boolean success,
        String transactionId,
        String errorMessage
) {
    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, null);
    }

    public static PaymentResult failure(String error) {
        return new PaymentResult(false, null, error);
    }
}