package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.entity.SubscriptionPlan;
import nextpos.app.nextpos.model.enums.BillingCycle;
import nextpos.app.nextpos.repository.SubscriptionPlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanSeeder implements CommandLineRunner {

    private final SubscriptionPlanRepository planRepository;

    // System user ID (adjust according to your system – this user should exist)
    private static final long SYSTEM_USER_ID = 1L;

    @Override
    public void run(String... args) {
        // Seed only if no plans exist
        if (planRepository.count() == 0) {
            log.info("No subscription plans found. Seeding initial data...");
            seedPlans();
            log.info("Subscription plans seeded successfully.");
        } else {
            log.info("Subscription plans already exist. Skipping seeding.");
        }
    }

    private void seedPlans() {
        // Starter Plan – Monthly
        SubscriptionPlan starterMonthly = SubscriptionPlan.builder()
                .name("Starter")
                .description("For small businesses getting started")
                .price(new BigDecimal("29.00"))
                .currency("USD")
                .billingCycle(BillingCycle.MONTHLY)
                .trialAvailable(true)
                .trialDays(14)
                .maxUsers(3)
                .maxBranches(1)
                .features(Map.of(
                        "products", "10,000",
                        "pos", "Basic",
                        "reports", "Basic",
                        "support", "Email"))
                .availableRegions(List.of("US", "GB", "IN", "AE", "CA", "AU"))
                .createdBy(SYSTEM_USER_ID)
                .build();

        // Starter Plan – Annual (with 20% discount)
        SubscriptionPlan starterAnnual = SubscriptionPlan.builder()
                .name("Starter")
                .description("For small businesses getting started")
                .price(new BigDecimal("279.00")) // 29*12*0.8 ≈ 278.4 → rounded
                .currency("USD")
                .billingCycle(BillingCycle.YEARLY)
                .trialAvailable(true)
                .trialDays(14)
                .maxUsers(3)
                .maxBranches(1)
                .features(Map.of(
                        "products", "10,000",
                        "pos", "Basic",
                        "reports", "Basic",
                        "support", "Email"))
                .availableRegions(List.of("US", "GB", "IN", "AE", "CA", "AU"))
                .createdBy(SYSTEM_USER_ID)
                .build();

        // Professional Plan – Monthly
        SubscriptionPlan professionalMonthly = SubscriptionPlan.builder()
                .name("Professional")
                .description("For growing businesses with multiple locations")
                .price(new BigDecimal("79.00"))
                .currency("USD")
                .billingCycle(BillingCycle.MONTHLY)
                .trialAvailable(true)
                .trialDays(14)
                .maxUsers(15)
                .maxBranches(5)
                .features(Map.of(
                        "products", "Unlimited",
                        "pos", "Advanced",
                        "reports", "Custom",
                        "support", "Priority email & chat",
                        "api", "Yes",
                        "multi_currency", "Yes"))
                .availableRegions(List.of("US", "GB", "IN", "AE", "CA", "AU"))
                .createdBy(SYSTEM_USER_ID)
                .build();

        // Professional Plan – Annual (with discount)
        SubscriptionPlan professionalAnnual = SubscriptionPlan.builder()
                .name("Professional")
                .description("For growing businesses with multiple locations")
                .price(new BigDecimal("759.00")) // 79*12*0.8 = 758.4
                .currency("USD")
                .billingCycle(BillingCycle.YEARLY)
                .trialAvailable(true)
                .trialDays(14)
                .maxUsers(15)
                .maxBranches(5)
                .features(Map.of(
                        "products", "Unlimited",
                        "pos", "Advanced",
                        "reports", "Custom",
                        "support", "Priority email & chat",
                        "api", "Yes",
                        "multi_currency", "Yes"))
                .availableRegions(List.of("US", "GB", "IN", "AE", "CA", "AU"))
                .createdBy(SYSTEM_USER_ID)
                .build();

        // Enterprise Plan – Monthly
        SubscriptionPlan enterpriseMonthly = SubscriptionPlan.builder()
                .name("Enterprise")
                .description("For large businesses with complex needs")
                .price(new BigDecimal("199.00"))
                .currency("USD")
                .billingCycle(BillingCycle.MONTHLY)
                .trialAvailable(false) // Enterprise may not have trial
                .trialDays(0)
                .maxUsers(null) // unlimited
                .maxBranches(null) // unlimited
                .features(Map.of(
                        "products", "Unlimited",
                        "pos", "Custom",
                        "reports", "Advanced",
                        "support", "24/7 phone",
                        "api", "Yes",
                        "multi_currency", "Yes",
                        "onboarding", "Custom",
                        "account_manager", "Dedicated",
                        "white_label", "Yes"))
                .availableRegions(List.of("US", "GB", "IN", "AE", "CA", "AU"))
                .createdBy(SYSTEM_USER_ID)
                .build();

        // Enterprise Plan – Annual (with discount)
        SubscriptionPlan enterpriseAnnual = SubscriptionPlan.builder()
                .name("Enterprise")
                .description("For large businesses with complex needs")
                .price(new BigDecimal("1909.00")) // 199*12*0.8 = 1910.4 ≈ 1909
                .currency("USD")
                .billingCycle(BillingCycle.YEARLY)
                .trialAvailable(false)
                .trialDays(0)
                .maxUsers(null)
                .maxBranches(null)
                .features(Map.of(
                        "products", "Unlimited",
                        "pos", "Custom",
                        "reports", "Advanced",
                        "support", "24/7 phone",
                        "api", "Yes",
                        "multi_currency", "Yes",
                        "onboarding", "Custom",
                        "account_manager", "Dedicated",
                        "white_label", "Yes"))
                .availableRegions(List.of("US", "GB", "IN", "AE", "CA", "AU"))
                .createdBy(SYSTEM_USER_ID)
                .build();

        // Save all plans
        planRepository.saveAll(List.of(
                starterMonthly, starterAnnual,
                professionalMonthly, professionalAnnual,
                enterpriseMonthly, enterpriseAnnual));
    }
}