export enum TaxCategory {
  VAT = "VAT",
  GST = "GST",
  TDS = "TDS",
  SERVICE_TAX = "SERVICE_TAX",
  CUSTOM = "CUSTOM",
}

export const TaxCategoryLabels: Record<TaxCategory, string> = {
  [TaxCategory.VAT]: "VAT",
  [TaxCategory.GST]: "GST",
  [TaxCategory.TDS]: "TDS",
  [TaxCategory.SERVICE_TAX]: "Service Tax",
  [TaxCategory.CUSTOM]: "Custom",
};
