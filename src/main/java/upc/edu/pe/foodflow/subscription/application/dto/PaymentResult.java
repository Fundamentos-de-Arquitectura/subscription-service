package upc.edu.pe.foodflow.subscription.application.dto;

/**
 * DTO con el resultado del procesamiento del pago.
 * Indica si fue exitoso y provee detalles de la transacción.
 */
public class PaymentResult {

    private final boolean success;
    private final String transactionId;
    private final String message;

    public PaymentResult(boolean success, String transactionId, String message) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public String transactionId() {
        return transactionId;
    }

    public String message() {
        return message;
    }

    // 🔹 Métodos estáticos de conveniencia
    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, "Pago procesado correctamente");
    }

    public static PaymentResult failure(String message) {
        return new PaymentResult(false, null, message);
    }
}
