export enum PromotionStackingStrategy {
  BEST_DISCOUNT = "BEST_DISCOUNT",
  PRIORITY = "PRIORITY",
  COMBINE = "COMBINE",
}

export const PromotionStackingStrategyLabels: Record<PromotionStackingStrategy, string> = {
  [PromotionStackingStrategy.BEST_DISCOUNT]: "Pick Best Discount",
  [PromotionStackingStrategy.PRIORITY]: "Priority‑based",
  [PromotionStackingStrategy.COMBINE]: "Combine All",
};