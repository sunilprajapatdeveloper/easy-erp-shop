package nextpos.app.nextpos.security.authorization;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpMethod;

/** Central fail-closed permission mapping for authenticated business APIs. */
public final class BusinessPermissionRules {

    private record Rule(String prefix, String read, String create, String update, String delete) {
    }

    private static final List<Rule> RULES = List.of(
            rule("/api/v1/products/product-stocks", "PRODUCT_VIEW", "PRODUCT_EDIT", "PRODUCT_EDIT", "PRODUCT_EDIT"),
            rule("/api/v1/products/product-prices", "PRODUCT_VIEW", "PRODUCT_EDIT", "PRODUCT_EDIT", "PRODUCT_EDIT"),
            rule("/api/v1/products/product-taxes", "PRODUCT_VIEW", "PRODUCT_EDIT", "PRODUCT_EDIT", "PRODUCT_EDIT"),
            rule("/api/v1/products", "PRODUCT_LIST", "PRODUCT_CREATE", "PRODUCT_EDIT", "PRODUCT_DELETE"),
            rule("/api/v1/adjustment-types", "ADJUSTMENT_LIST", "ADJUSTMENT_CREATE", "ADJUSTMENT_EDIT", "ADJUSTMENT_DELETE"),
            rule("/api/v1/adjustments", "ADJUSTMENT_LIST", "ADJUSTMENT_CREATE", "ADJUSTMENT_EDIT", "ADJUSTMENT_DELETE"),
            rule("/api/v1/transfers", "TRANSFER_LIST", "TRANSFER_CREATE", "TRANSFER_EDIT", "TRANSFER_DELETE"),
            rule("/api/v1/purchase-returns", "PURCHASE_RETURN_LIST", "PURCHASE_RETURN_CREATE", "PURCHASE_RETURN_EDIT", "PURCHASE_RETURN_DELETE"),
            rule("/api/v1/purchases", "PURCHASE_LIST", "PURCHASE_CREATE", "PURCHASE_EDIT", "PURCHASE_DELETE"),
            rule("/api/v1/sales-return", "SALE_RETURN_LIST", "SALE_RETURN_CREATE", "SALE_RETURN_EDIT", "SALE_RETURN_DELETE"),
            rule("/api/v1/sales", "SALE_LIST", "SALE_CREATE", "SALE_EDIT", "SALE_DELETE"),
            rule("/api/v1/quotations", "QUOTATION_LIST", "QUOTATION_CREATE", "QUOTATION_EDIT", "QUOTATION_DELETE"),
            rule("/api/v1/pos/settings", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/pos", "SALE_POS", "SALE_POS", "SALE_POS", "SALE_POS"),
            rule("/api/v1/payments", "SALE_PAYMENT_REPORT", "SALE_CREATE", "SALE_EDIT", "SALE_DELETE"),
            rule("/api/v1/customers", "CUSTOMER_LIST", "CUSTOMER_CREATE", "CUSTOMER_EDIT", "CUSTOMER_DELETE"),
            rule("/api/v1/suppliers", "SUPPLIER_LIST", "SUPPLIER_CREATE", "SUPPLIER_EDIT", "SUPPLIER_DELETE"),
            rule("/api/v1/users", "USER_LIST", "USER_CREATE", "USER_EDIT", "USER_DELETE"),
            rule("/api/v1/roles", "PERMISSION_LIST", "PERMISSION_CREATE", "PERMISSION_EDIT", "PERMISSION_DELETE"),
            rule("/api/permissions", "PERMISSION_LIST", "PERMISSION_CREATE", "PERMISSION_EDIT", "PERMISSION_DELETE"),
            rule("/api/v1/warehouses", "WAREHOUSE_LIST", "WAREHOUSE_CREATE", "WAREHOUSE_EDIT", "WAREHOUSE_DELETE"),
            rule("/api/v1/categories", "CATEGORY_LIST", "CATEGORY_CREATE", "CATEGORY_EDIT", "CATEGORY_DELETE"),
            rule("/api/v1/brands", "BRAND_LIST", "BRAND_CREATE", "BRAND_EDIT", "BRAND_DELETE"),
            rule("/api/v1/units", "UNIT_LIST", "UNIT_CREATE", "UNIT_EDIT", "UNIT_DELETE"),
            rule("/api/v1/currencies", "CURRENCY_LIST", "CURRENCY_CREATE", "CURRENCY_EDIT", "CURRENCY_DELETE"),
            rule("/api/v1/expenses", "EXPENSE_LIST", "EXPENSE_CREATE", "EXPENSE_EDIT", "EXPENSE_DELETE"),
            rule("/api/v1/promotions", "SALE_VIEW", "SALE_EDIT", "SALE_EDIT", "SALE_EDIT"),
            rule("/api/v1/discounts", "SALE_VIEW", "SALE_EDIT", "SALE_EDIT", "SALE_EDIT"),
            rule("/api/v1/import-export", "PRODUCT_LIST", "PRODUCT_CREATE", "PRODUCT_EDIT", "PRODUCT_DELETE"),
            rule("/api/v1/scanner", "SALE_POS", "SALE_POS", "SALE_POS", "SALE_POS"),
            rule("/api/dashboard", "DASHBOARD", "DASHBOARD", "DASHBOARD", "DASHBOARD"),
            rule("/api/v1/media", "PRODUCT_VIEW", "PRODUCT_EDIT", "PRODUCT_EDIT", "PRODUCT_EDIT"),
            rule("/api/tax-settings", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/settings/shipping-provider", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/payment-gateway", "SYSTEM_SETTING_PAYMENT_GATEWAY", "SYSTEM_SETTING_PAYMENT_GATEWAY", "SYSTEM_SETTING_PAYMENT_GATEWAY", "SYSTEM_SETTING_PAYMENT_GATEWAY"),
            rule("/api/v1/company-subscriptions", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/subscription-plans", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/company-currencies", "CURRENCY_LIST", "CURRENCY_CREATE", "CURRENCY_EDIT", "CURRENCY_DELETE"),
            rule("/api/v1/warehouse-currencies", "CURRENCY_LIST", "CURRENCY_CREATE", "CURRENCY_EDIT", "CURRENCY_DELETE"),
            rule("/api/v1/exchange-rates", "CURRENCY_LIST", "CURRENCY_CREATE", "CURRENCY_EDIT", "CURRENCY_DELETE"),
            rule("/api/v1/companies", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/branding-settings", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/loyalty-settings", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/online-ordering-settings", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/settings/security", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/shipping-provider-settings", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/smtp", "SYSTEM_SETTING_SMTP_CONFIGURATION", "SYSTEM_SETTING_SMTP_CONFIGURATION", "SYSTEM_SETTING_SMTP_CONFIGURATION", "SYSTEM_SETTING_SMTP_CONFIGURATION"),
            rule("/api/v1/social-media-settings", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/v1/admin/verifications", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/ai/admin", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING", "SYSTEM_SETTING"),
            rule("/api/ai", "DASHBOARD", "DASHBOARD", "DASHBOARD", "DASHBOARD"),
            rule("/api/v1/health", "DASHBOARD", "DASHBOARD", "DASHBOARD", "DASHBOARD"));

    private BusinessPermissionRules() {
    }

    public static Set<String> requiredAuthorities(String path, String method) {
        return RULES.stream()
                .filter(rule -> path.equals(rule.prefix()) || path.startsWith(rule.prefix() + "/"))
                .findFirst()
                .map(rule -> Set.of(permissionFor(rule, method)))
                .orElse(Set.of());
    }

    private static String permissionFor(Rule rule, String method) {
        if (HttpMethod.GET.matches(method) || HttpMethod.HEAD.matches(method)) return rule.read();
        if (HttpMethod.POST.matches(method)) return rule.create();
        if (HttpMethod.PUT.matches(method) || HttpMethod.PATCH.matches(method)) return rule.update();
        if (HttpMethod.DELETE.matches(method)) return rule.delete();
        return "__DENY__";
    }

    private static Rule rule(String prefix, String read, String create, String update, String delete) {
        return new Rule(prefix, read, create, update, delete);
    }
}
