export enum ShippingProvider {
  FEDEX = "FEDEX",
  UPS = "UPS",
  DHL = "DHL",
  USPS = "USPS",
  ROYAL_MAIL = "ROYAL_MAIL",
  SHIPROCKET = "SHIPROCKET",
  BLUE_DART = "BLUE_DART",
  CUSTOM = "CUSTOM",
}

export const ShippingProviderLabels: Record<ShippingProvider, string> = {
  [ShippingProvider.FEDEX]: "FedEx",
  [ShippingProvider.UPS]: "UPS",
  [ShippingProvider.DHL]: "DHL",
  [ShippingProvider.USPS]: "USPS",
  [ShippingProvider.ROYAL_MAIL]: "Royal Mail",
  [ShippingProvider.SHIPROCKET]: "Shiprocket",
  [ShippingProvider.BLUE_DART]: "Blue Dart",
  [ShippingProvider.CUSTOM]: "Custom",
};
