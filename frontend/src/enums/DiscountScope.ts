export enum DiscountScope {
  PRODUCT = "PRODUCT",
  ORDER = "ORDER",
  CATEGORY = "CATEGORY",
  CUSTOMER_GROUP = "CUSTOMER_GROUP",
}

export const DiscountScopeLabels: Record<DiscountScope, string> = {
  [DiscountScope.PRODUCT]: "Product",
  [DiscountScope.ORDER]: "Order",
  [DiscountScope.CATEGORY]: "Category",
  [DiscountScope.CUSTOMER_GROUP]: "Customer Group",
};
