export enum ProductType {
  INVENTORY = "INVENTORY",
  SERVICE = "SERVICE",
  COMBO = "COMBO",
  GIFT_CARD = "GIFT_CARD",
  STOCK = "STOCK"
}

export const ProductTypeLabels: Record<ProductType, string> = {
  [ProductType.INVENTORY]: "Inventory",
  [ProductType.SERVICE]: "Service",
  [ProductType.COMBO]: "Combo",
  [ProductType.GIFT_CARD]: "Gift Card",
  [ProductType.STOCK]: "Stock",
};
