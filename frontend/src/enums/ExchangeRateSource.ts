export enum ExchangeRateSource {
  API = "API",
  MANUAL = "MANUAL",
  COMPANY = "COMPANY",
}

export const ExchangeRateSourceLabels: Record<ExchangeRateSource, string> = {
  [ExchangeRateSource.API]: "API",
  [ExchangeRateSource.MANUAL]: "Manual",
  [ExchangeRateSource.COMPANY]: "Company",
};