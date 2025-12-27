export enum CurrencyStatus {
  ACTIVE = "ACTIVE",
  INACTIVE = "INACTIVE",
}

export const CurrencyStatusLabels: Record<CurrencyStatus, string> = {
  [CurrencyStatus.ACTIVE]: "Active",
  [CurrencyStatus.INACTIVE]: "Inactive",
};
