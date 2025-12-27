export interface Currency {
  id: number;
  name: string;
  code: string;
  symbol: string;
}

export type CreateCurrencyRequest = Omit<Currency, "id">;
