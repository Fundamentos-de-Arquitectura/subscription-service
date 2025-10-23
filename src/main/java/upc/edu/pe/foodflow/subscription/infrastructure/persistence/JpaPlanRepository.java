package upc.edu.pe.foodflow.subscription.infrastructure.persistence;

import upc.edu.pe.foodflow.subscription.domain.model.SubscriptionPlan;
import upc.edu.pe.foodflow.subscription.domain.repository.PlanRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPlanRepository extends JpaRepository<SubscriptionPlan, Long>, PlanRepository {

    List<SubscriptionPlan> findByActiveTrue();

    Optional<SubscriptionPlan> findByName(String name);

    default List<SubscriptionPlan> findAllActive() {
        return findByActiveTrue();
    }
}
