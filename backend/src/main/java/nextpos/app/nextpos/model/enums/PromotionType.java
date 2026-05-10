package nextpos.app.nextpos.model.enums;

public enum PromotionType {
    COUPON, // requires code, manually applied
    AUTO, // automatically applied
    BUY_X_GET_Y, // buy X get Y free or discounted
    FREE_SHIPPING // free shipping override
}