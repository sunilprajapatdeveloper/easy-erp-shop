package nextpos.app.nextpos.model.enums;

import java.math.BigDecimal;

public enum DefaultUnit {
    PIECE("Piece", "pc", "piece", "*", BigDecimal.ONE),
    KILOGRAM("Kilogram", "kg", "gram", "*", new BigDecimal("1000")),
    GRAM("Gram", "g", "gram", "*", BigDecimal.ONE),
    LITER("Liter", "L", "milliliter", "*", new BigDecimal("1000")),
    MILLILITER("Milliliter", "ml", "milliliter", "*", BigDecimal.ONE),
    METER("Meter", "m", "centimeter", "*", new BigDecimal("100")),
    CENTIMETER("Centimeter", "cm", "centimeter", "*", BigDecimal.ONE),
    DOZEN("Dozen", "dz", "piece", "*", new BigDecimal("12"));

    private final String name;
    private final String shortName;
    private final String baseUnit;
    private final String operator;
    private final BigDecimal operatorValue;

    DefaultUnit(String name, String shortName, String baseUnit, String operator, BigDecimal operatorValue) {
        this.name = name;
        this.shortName = shortName;
        this.baseUnit = baseUnit;
        this.operator = operator;
        this.operatorValue = operatorValue;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public String getOperator() {
        return operator;
    }

    public BigDecimal getOperatorValue() {
        return operatorValue;
    }
}