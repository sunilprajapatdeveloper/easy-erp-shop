package nextpos.app.nextpos.model.enums;

public enum DefaultCategory {
    UNCATEGORIZED("Uncategorized"),
    GENERAL("General"),
    FOOD("Food & Beverage"),
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    STATIONERY("Stationery"),
    HOUSEHOLD("Household"),
    TOILETRIES("Toiletries"),
    BEVERAGES("Beverages"),
    SNACKS("Snacks");

    private final String displayName;

    DefaultCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}