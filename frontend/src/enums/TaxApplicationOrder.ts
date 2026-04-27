export enum TaxApplicationOrder {
  BEFORE_DISCOUNT = "BEFORE_DISCOUNT",
  AFTER_DISCOUNT = "AFTER_DISCOUNT",
}

export const TaxApplicationOrderLabels: Record<TaxApplicationOrder, string> = {
  [TaxApplicationOrder.BEFORE_DISCOUNT]: "Apply tax before discount",
  [TaxApplicationOrder.AFTER_DISCOUNT]: "Apply tax after discount",
};
