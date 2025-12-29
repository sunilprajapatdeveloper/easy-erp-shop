package nextpos.app.nextpos.model.enums;

public enum DefaultBrand {
    GENERAL("General", "General products without specific brand", null);

    private final String name;
    private final String description;
    private final String image;

    DefaultBrand(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}