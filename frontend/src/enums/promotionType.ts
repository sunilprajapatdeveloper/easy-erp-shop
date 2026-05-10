export enum PromotionType {
  COUPON = "COUPON",
  AUTO = "AUTO",
  BUY_X_GET_Y = "BUY_X_GET_Y",
  FREE_SHIPPING = "FREE_SHIPPING",
}

export const PromotionTypeLabels: Record<PromotionType, string> = {
  [PromotionType.COUPON]: "Coupon (manual apply)",
  [PromotionType.AUTO]: "Auto‑apply",
  [PromotionType.BUY_X_GET_Y]: "Buy X Get Y",
  [PromotionType.FREE_SHIPPING]: "Free Shipping",
};