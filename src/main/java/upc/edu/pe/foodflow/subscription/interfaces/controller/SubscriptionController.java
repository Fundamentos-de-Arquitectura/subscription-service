package upc.edu.pe.foodflow.subscription.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentData;
import upc.edu.pe.foodflow.subscription.application.service.SubscriptionService;
import upc.edu.pe.foodflow.subscription.domain.model.Subscription;
import upc.edu.pe.foodflow.subscription.domain.model.SubscriptionPlan;
import upc.edu.pe.foodflow.subscription.domain.repository.PlanRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final PlanRepository planRepository;

    public SubscriptionController(SubscriptionService subscriptionService, PlanRepository planRepository) {
        this.subscriptionService = subscriptionService;
        this.planRepository = planRepository;
    }

    //listar todos los planes
    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlan>> getPlans() {
        List<SubscriptionPlan> plans = planRepository.findAllActive();
        return ResponseEntity.ok(plans);
    }

    //suscribir usuario a un plan
    @PostMapping("/subscribe/{userId}")
    public ResponseEntity<?> subscribe(
            @PathVariable Long userId,
            @RequestParam String planName,
            @RequestBody PaymentData paymentData
    ) {
        Optional<Subscription> subscriptionOpt = subscriptionService.subscribeUser(userId, planName, paymentData);

        if (subscriptionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Payment could no be processed or plan not found.");
        }

        return ResponseEntity.ok(subscriptionOpt.get());
    }

    //consultar la ultima suscripcion
    @GetMapping("/{userId}")
    public ResponseEntity<?> getLatestSubscription(@PathVariable Long userId) {
        Optional<Subscription> subscriptionOpt = subscriptionService.getLatestSubscription(userId);
        return subscriptionOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //cancelar suscripcion activa
    @DeleteMapping("/cancel/{userId}")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long userId) {
        boolean cancelled = subscriptionService.cancelSubscription(userId);
        if (!cancelled) {
            return ResponseEntity.badRequest().body("There is no active subscription for this user.");
        }
        return ResponseEntity.ok("Subscription cancelled successfully.");
    }

    //historial completo de suscripciones
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Subscription>> getUserSubscriptions(@PathVariable Long userId) {
        List<Subscription> subs = subscriptionService.getAllSubscriptions(userId);
        if (subs.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(subs);
    }
}
