package nextpos.app.nextpos.model.enums;

public enum DefaultCategory {
    GENERAL("General", "GEN", "General products without specific category"),
    UNCATEGORIZED("Uncategorized", "UNCAT", "Products without category"),
    MISC("Miscellaneous", "MISC", "Miscellaneous items"),
    NEW_ARRIVALS("New Arrivals", "NEW", "Newly arrived products");

    private final String name;
    private final String code;
    private final String description;

    DefaultCategory(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}