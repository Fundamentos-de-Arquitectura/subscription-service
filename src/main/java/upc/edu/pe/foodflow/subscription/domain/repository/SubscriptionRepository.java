package upc.edu.pe.foodflow.subscription.domain.repository;

import upc.edu.pe.foodflow.subscription.domain.model.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(Long id);

    Optional<Subscription> findActiveByUserId(Long userId);

    List<Subscription> findAllByUserId(Long userId);

    void delete(Subscription subscription);
}