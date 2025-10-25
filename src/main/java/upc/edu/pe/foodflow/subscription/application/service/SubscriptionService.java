package upc.edu.pe.foodflow.subscription.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentData;
import upc.edu.pe.foodflow.subscription.domain.model.Subscription;
import upc.edu.pe.foodflow.subscription.domain.model.SubscriptionPlan;
import upc.edu.pe.foodflow.subscription.domain.repository.PlanRepository;
import upc.edu.pe.foodflow.subscription.domain.repository.SubscriptionRepository;
import upc.edu.pe.foodflow.subscription.domain.service.PaymentClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final PaymentClient paymentClient;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, PlanRepository planRepository, PaymentClient paymentClient) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.paymentClient = paymentClient;
    }

    @Transactional
    public Optional<Subscription> subscribeUser(Long userId, String planName, PaymentData paymentData) {
        Optional<SubscriptionPlan> planOpt = planRepository.findByName(planName);
        if (planOpt.isEmpty()) return Optional.empty();

        SubscriptionPlan plan = planOpt.get();

        var result = paymentClient.processPayment(paymentData, plan.getPrice());
        if (!result.success()) return Optional.empty();

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(1);

        Subscription subscription = new Subscription(userId, plan, startDate, endDate);
        subscription.activate(UUID.randomUUID().toString());

        return Optional.of(subscriptionRepository.save(subscription));
    }

    public Optional<Subscription> getLatestSubscription(Long userId) {
        List<Subscription> subs = subscriptionRepository.findLatestByUserId(userId);
        if (subs.isEmpty()) return Optional.empty();
        return Optional.of(subs.get(0));
    }

    @Transactional
    public boolean cancelSubscription(Long userId) {
        Optional<Subscription> subOpt = subscriptionRepository.findActiveByUserId(userId);
        if (subOpt.isEmpty()) return false;

        Subscription sub = subOpt.get();
        sub.cancel();
        subscriptionRepository.save(sub);
        return true;
    }

    public List<Subscription> getAllSubscriptions(Long userId) {
        return subscriptionRepository.findAllByUserId(userId);
    }
}
