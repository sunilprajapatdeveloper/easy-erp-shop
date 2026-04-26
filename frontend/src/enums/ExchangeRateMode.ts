export enum ExchangeRateMode {
  SYSTEM = "SYSTEM",
  MANUAL = "MANUAL",
}

export const ExchangeRateModeLabels: Record<ExchangeRateMode, string> = {
  [ExchangeRateMode.SYSTEM]: "SYSTEM",
  [ExchangeRateMode.MANUAL]: "MANUAL",
};
