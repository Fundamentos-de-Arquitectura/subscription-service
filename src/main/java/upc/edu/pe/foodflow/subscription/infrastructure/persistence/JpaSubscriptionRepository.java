package upc.edu.pe.foodflow.subscription.infrastructure.persistence;

import upc.edu.pe.foodflow.subscription.domain.model.Subscription;
import upc.edu.pe.foodflow.subscription.domain.model.SubscriptionStatus;
import upc.edu.pe.foodflow.subscription.domain.repository.SubscriptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaSubscriptionRepository extends JpaRepository<Subscription, Long>, SubscriptionRepository {

    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    default Optional<Subscription> findActiveByUserId(Long userId) {
        return findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
    }

    List<Subscription> findAllByUserId(Long userId);
}
