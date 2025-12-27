package nextpos.app.nextpos.model.enums;

public enum PriceListType {
    DEFAULT, // Standard base price
    WHOLESALE, // Wholesale discounted price
    RETAIL, // Explicit retail pricing (if separate from default)
    SEASONAL, // Seasonal or festival price
    PROMOTIONAL, // Limited-time promotions
    CLEARANCE, // Stock clearance pricing
    CUSTOM // Catch-all for company-specific lists
}