package upc.edu.pe.foodflow.subscription.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String paymentTransactionId;

    protected Subscription() {} // JPA

    public Subscription(Long userId, SubscriptionPlan plan, LocalDateTime startDate, LocalDateTime endDate) {
        this.userId = userId;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = SubscriptionStatus.PENDING_PAYMENT;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public SubscriptionPlan getPlan() { return plan; }
    public SubscriptionStatus getStatus() { return status; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public String getPaymentTransactionId() { return paymentTransactionId; }

    // Domain behaviors
    public void activate(String transactionId) {
        this.status = SubscriptionStatus.ACTIVE;
        this.paymentTransactionId = transactionId;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
    }
}