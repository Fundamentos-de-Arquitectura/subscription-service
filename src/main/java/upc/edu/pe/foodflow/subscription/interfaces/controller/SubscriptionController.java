package upc.edu.pe.foodflow.subscription.interfaces.controller;

import upc.edu.pe.foodflow.subscription.application.dto.CreatePlanRequest;
import upc.edu.pe.foodflow.subscription.application.dto.PaymentData;
import upc.edu.pe.foodflow.subscription.application.dto.SubscriptionRequest;
import upc.edu.pe.foodflow.subscription.application.service.SubscriptionService;
import upc.edu.pe.foodflow.subscription.domain.model.Subscription;
import upc.edu.pe.foodflow.subscription.domain.model.SubscriptionPlan;
import upc.edu.pe.foodflow.subscription.domain.repository.PlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // GET: Listar todos los planes disponibles
    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlan>> getPlans() {
        List<SubscriptionPlan> plans = planRepository.findAllActive();
        return ResponseEntity.ok(plans);
    }

    // POST: Crear un nuevo plan (ADMIN)
    @PostMapping("/plans")
    public ResponseEntity<SubscriptionPlan> createPlan(@RequestBody CreatePlanRequest request) {
        SubscriptionPlan plan = new SubscriptionPlan(
                request.name(),
                request.price(),
                request.billingPeriod(),
                request.description()
        );
        SubscriptionPlan savedPlan = planRepository.save(plan);
        return ResponseEntity.ok(savedPlan);
    }

    // POST: Suscribir usuario a un plan
    @PostMapping("/subscribe/{userId}")
    public ResponseEntity<?> subscribe(
            @PathVariable Long userId,
            @RequestBody SubscriptionRequest request
    ) {
        try {
            Optional<Subscription> subscriptionOpt = subscriptionService.subscribeUser(
                    userId,
                    request.planName(),
                    request.paymentData()
            );

            if (subscriptionOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Payment could not be processed or invalid plan.");
            }

            return ResponseEntity.ok(subscriptionOpt.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: Consultar la suscripción activa del usuario
    @GetMapping("/{userId}")
    public ResponseEntity<?> getActiveSubscription(@PathVariable Long userId) {
        Optional<Subscription> subscriptionOpt = subscriptionService.getActiveSubscription(userId);
        return subscriptionOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: Cancelar suscripción activa
    @DeleteMapping("/cancel/{userId}")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long userId) {
        boolean cancelled = subscriptionService.cancelSubscription(userId);
        if (!cancelled) {
            return ResponseEntity.badRequest().body("No active subscription found to cancel.");
        }
        return ResponseEntity.ok("Subscription cancelled successfully.");
    }
}
