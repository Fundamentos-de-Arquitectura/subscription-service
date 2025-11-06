package upc.edu.pe.foodflow.subscription.application.dto;

import upc.edu.pe.foodflow.subscription.domain.model.BillingPeriod;

import java.math.BigDecimal;

public record CreatePlanRequest(
        String name,
        BigDecimal price,
        BillingPeriod billingPeriod,
        String description
) {
}
