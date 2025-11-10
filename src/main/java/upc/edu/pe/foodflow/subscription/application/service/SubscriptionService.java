package upc.edu.pe.foodflow.subscription.application.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import upc.edu.pe.foodflow.subscription.application.dto.PaymentData;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentResult;
import upc.edu.pe.foodflow.subscription.domain.model.BillingPeriod;
import upc.edu.pe.foodflow.subscription.domain.model.Subscription;
import upc.edu.pe.foodflow.subscription.domain.model.SubscriptionPlan;
import upc.edu.pe.foodflow.subscription.domain.repository.PlanRepository;
import upc.edu.pe.foodflow.subscription.domain.repository.SubscriptionRepository;
import upc.edu.pe.foodflow.subscription.domain.service.PaymentClient;

@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final PaymentClient paymentClient;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            PlanRepository planRepository,
            PaymentClient paymentClient) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.paymentClient = paymentClient;
    }

    public Optional<Subscription> subscribeUser(Long userId, String planName, PaymentData paymentData) {
        Optional<SubscriptionPlan> planOpt = planRepository.findByName(planName);
        if (planOpt.isEmpty()) {
            throw new IllegalArgumentException("El plan solicitado no existe: " + planName);
        }

        SubscriptionPlan plan = planOpt.get();
        paymentData = new PaymentData(
                paymentData.cardNumber(),
                paymentData.cardHolder(),
                paymentData.expirationDate(),
                paymentData.cvv(),
                plan.getPrice().doubleValue()
        );

        PaymentResult result = paymentClient.processPayment(paymentData);

        if (!result.success()) {
            // podrías loggear el fallo aquí
            return Optional.empty();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = switch (plan.getBillingPeriod()) {
            case MONTHLY -> now.plusMonths(1);
            case QUARTERLY -> now.plusMonths(3);
            case YEARLY -> now.plusYears(1);
        };

        Subscription subscription = new Subscription(userId, plan, now, endDate);
        subscription.activate(result.transactionId());

        return Optional.of(subscriptionRepository.save(subscription));
    }

    public Optional<Subscription> getActiveSubscription(Long userId) {
        return subscriptionRepository.findActiveByUserId(userId);
    }

    public boolean cancelSubscription(Long userId) {
        var subscriptionOpt = subscriptionRepository.findActiveByUserId(userId);
        if (subscriptionOpt.isEmpty()) return false;

        Subscription subscription = subscriptionOpt.get();
        subscription.cancel();
        subscriptionRepository.save(subscription);
        return true;
    }

    /**
     * Crea una suscripción por defecto (plan BASIC) para un nuevo usuario sin requerir pago
     * @param userId ID del usuario
     * @return Optional con la suscripción creada o vacío si falla
     */
    public Optional<Subscription> createDefaultSubscription(Long userId) {
        // Buscar el plan BASIC
        Optional<SubscriptionPlan> planOpt = planRepository.findByName("BASIC");
        if (planOpt.isEmpty()) {
            // Si no existe el plan BASIC, intentar crear uno por defecto
            SubscriptionPlan basicPlan = new SubscriptionPlan(
                    "BASIC",
                    java.math.BigDecimal.ZERO,
                    BillingPeriod.MONTHLY,
                    "Plan básico gratuito"
            );
            planOpt = Optional.of(planRepository.save(basicPlan));
        }

        SubscriptionPlan plan = planOpt.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusMonths(1); // Plan básico mensual

        Subscription subscription = new Subscription(userId, plan, now, endDate);
        // Activar sin pago para plan básico
        subscription.activate("DEFAULT-" + userId);

        return Optional.of(subscriptionRepository.save(subscription));
    }

    /**
     * Obtiene una suscripción por ID
     * @param subscriptionId ID de la suscripción
     * @return Optional con la suscripción o vacío si no existe
     */
    public Optional<Subscription> getSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }
}