package upc.edu.pe.foodflow.subscription.application.dto;

import java.time.LocalDateTime;

/**
 * DTO compartido para respuestas de suscripción
 */
public record SubscriptionResponse(
        Long id,
        Long userId,
        String planName,
        String status,
        LocalDateTime startDate,
        LocalDateTime endDate
) {}
