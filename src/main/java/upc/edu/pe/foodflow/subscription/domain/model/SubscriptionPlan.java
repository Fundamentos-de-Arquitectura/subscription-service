package upc.edu.pe.foodflow.subscription.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plans")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingPeriod billingPeriod;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    protected SubscriptionPlan() {} // Constructor requerido por JPA

    public SubscriptionPlan(String name, BigDecimal price, BillingPeriod billingPeriod, String description) {
        this.name = name;
        this.price = price;
        this.billingPeriod = billingPeriod;
        this.description = description;
        this.active = true;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public BillingPeriod getBillingPeriod() { return billingPeriod; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }

    public void deactivate() { this.active = false; }
}
