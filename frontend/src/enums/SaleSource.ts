export enum SaleSource {
  MANUAL = "MANUAL",
  IMPORT = "IMPORT",
  WEB = "WEB",
  MOBILE = "MOBILE",
  POS = "POS",
  OTHER = "OTHER",
}

export const SaleSourceLabels: Record<SaleSource, string> = {
  [SaleSource.MANUAL]: "Manual",
  [SaleSource.IMPORT]: "Import",
  [SaleSource.WEB]: "Web",
  [SaleSource.MOBILE]: "Mobile",
  [SaleSource.POS]: "POS",
  [SaleSource.OTHER]: "Other",
};
