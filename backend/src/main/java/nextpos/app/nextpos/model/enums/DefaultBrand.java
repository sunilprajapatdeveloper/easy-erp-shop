package nextpos.app.nextpos.model.enums;

public enum DefaultBrand {
    GENERIC("Generic"),
    NO_BRAND("No Brand"),
    UNBRANDED("Unbranded"),
    HOUSE_BRAND("House Brand");

    private final String displayName;

    DefaultBrand(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}