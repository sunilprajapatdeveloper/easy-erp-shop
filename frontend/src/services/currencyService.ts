import api from "./api";
import type { Currency, CreateCurrencyRequest } from "@/types/Currency";

export const getCurrencies = () => api.get<Currency[]>("/currencies");

export const createCurrency = (data: CreateCurrencyRequest) =>
  api.post<Currency>("/currencies", data);

export const updateCurrency = (id: number, data: CreateCurrencyRequest) =>
  api.put<Currency>(`/currencies/${id}`, data);

export const deleteCurrency = (id: number) =>
  api.delete<void>(`/currencies/${id}`);
