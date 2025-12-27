package nextpos.app.nextpos.model.dto.request;

import nextpos.app.nextpos.model.enums.TaxType;

import java.math.BigDecimal;

public interface ProductRequest {

    String getName();

    String getCode();

    String getSku();

    String getBarcode();

    Long getCategoryId();

    Long getBrandId();

    Long getProductUnitId();

    Long getSalesUnitId();

    Long getPurchaseUnitId();

    Long getCurrencyId();

    BigDecimal getCost();

    BigDecimal getPrice();

    BigDecimal getExchangeRate();

    Integer getQuantity();

    Integer getStockAlert();

    Integer getMinStockLevel();

    Integer getMaxStockLevel();

    Integer getReorderLevel();

    Boolean getIsBatchManaged();

    Boolean getIsSerialized();

    BigDecimal getOrderTax();

    TaxType getTaxType();

    BigDecimal getDiscount();

    String getDescription();

    String getProductImage();

    String getImageUrls();

    Boolean getIsActive();
}
