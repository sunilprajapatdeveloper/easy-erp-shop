export enum TaxType {
  VAT = "VAT",
  GST = "GST",
  TDS = "TDS",
  SERVICE_TAX = "SERVICE_TAX",
  CUSTOM = "CUSTOM",
}

export const TaxTypeLabels: Record<TaxType, string> = {
  [TaxType.VAT]: "VAT",
  [TaxType.GST]: "GST",
  [TaxType.TDS]: "TDS",
  [TaxType.SERVICE_TAX]: "Service Tax",
  [TaxType.CUSTOM]: "Custom",
};
