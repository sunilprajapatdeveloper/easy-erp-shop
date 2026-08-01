export enum PurchaseSource {
  MANUAL = "MANUAL",
  IMPORT = "IMPORT",
  WEB = "WEB",
  MOBILE = "MOBILE",
  POS = "POS",
  OTHER = "OTHER",
}

export const PurchaseSourceLabels: Record<PurchaseSource, string> = {
  [PurchaseSource.MANUAL]: "Manual",
  [PurchaseSource.IMPORT]: "Import",
  [PurchaseSource.WEB]: "Web",
  [PurchaseSource.MOBILE]: "Mobile",
  [PurchaseSource.POS]: "POS",
  [PurchaseSource.OTHER]: "Other",
};
