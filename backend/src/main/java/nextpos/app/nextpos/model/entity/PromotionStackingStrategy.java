package nextpos.app.nextpos.model.entity;

public enum PromotionStackingStrategy {
    BEST_DISCOUNT, // pick the single best discount
    PRIORITY, // apply in priority order
    COMBINE // combine all compatible promotions
}