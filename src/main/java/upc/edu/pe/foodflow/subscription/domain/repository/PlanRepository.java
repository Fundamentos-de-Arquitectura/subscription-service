package upc.edu.pe.foodflow.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.edu.pe.foodflow.subscription.domain.model.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    List<SubscriptionPlan> findAllByActiveTrue();

    Optional<SubscriptionPlan> findByNameIgnoreCase(String name);
}
