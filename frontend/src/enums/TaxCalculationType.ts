export enum TaxCalculationType {
  BEFORE_DISCOUNT = "BEFORE_DISCOUNT",
  AFTER_DISCOUNT = "AFTER_DISCOUNT",
}

export const TaxCalculationTypeLabels: Record<TaxCalculationType, string> = {
  [TaxCalculationType.BEFORE_DISCOUNT]: "Before Discount",
  [TaxCalculationType.AFTER_DISCOUNT]: "After Discount",
};
