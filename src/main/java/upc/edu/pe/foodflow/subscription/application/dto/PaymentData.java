package upc.edu.pe.foodflow.subscription.application.dto;

public record PaymentData(
        String cardNumber,
        String cardHolder,
        String expirationDate,
        String cvv,
        Double amount
) {}