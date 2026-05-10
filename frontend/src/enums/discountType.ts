export enum DiscountType {
  PERCENTAGE = "PERCENTAGE",
  FIXED = "FIXED",
  FREE_ITEM = "FREE_ITEM",
  FREE_SHIPPING = "FREE_SHIPPING",
}

export const DiscountTypeLabels: Record<DiscountType, string> = {
  [DiscountType.PERCENTAGE]: "Percentage (%)",
  [DiscountType.FIXED]: "Fixed Amount",
  [DiscountType.FREE_ITEM]: "Free Item",
  [DiscountType.FREE_SHIPPING]: "Free Shipping",
};