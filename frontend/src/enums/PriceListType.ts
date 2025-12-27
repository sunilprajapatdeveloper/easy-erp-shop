export enum PriceListType {
  DEFAULT = "DEFAULT",
  WHOLESALE = "WHOLESALE",
  RETAIL = "RETAIL",
  SEASONAL = "SEASONAL",
  PROMOTIONAL = "PROMOTIONAL",
  CLEARANCE = "CLEARANCE",
  CUSTOM = "CUSTOM",
}

export const PriceListTypeLabels: Record<PriceListType, string> = {
  [PriceListType.DEFAULT]: "Default",
  [PriceListType.WHOLESALE]: "Wholesale",
  [PriceListType.RETAIL]: "Retail",
  [PriceListType.SEASONAL]: "Seasonal",
  [PriceListType.PROMOTIONAL]: "Promotional",
  [PriceListType.CLEARANCE]: "Clearance",
  [PriceListType.CUSTOM]: "Custom",
};
