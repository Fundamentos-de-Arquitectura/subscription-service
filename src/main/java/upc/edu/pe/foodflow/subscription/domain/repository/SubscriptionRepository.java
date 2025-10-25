package upc.edu.pe.foodflow.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import upc.edu.pe.foodflow.subscription.domain.model.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    //buscar suscripcion activa
    @Query("SELECT s FROM Subscription s WHERE s.userId = :userId AND s.status = 'ACTIVE'")
    Optional<Subscription> findActiveByUserId(@Param("userId") Long userId);

    //Buscar la ultima suscripción
    @Query("SELECT s FROM Subscription s WHERE s.userId = :userId ORDER BY s.startDate DESC")
    List<Subscription> findLatestByUserId(@Param("userId") Long userId);

    //historial de todas las suscripciones del usuario
    List<Subscription> findAllByUserId(Long userId);
}
