package upc.edu.pe.foodflow.subscription.domain.repository;

import upc.edu.pe.foodflow.subscription.domain.model.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {

    List<SubscriptionPlan> findAllActive();

    Optional<SubscriptionPlan> findByName(String name);

    SubscriptionPlan save(SubscriptionPlan plan);
}