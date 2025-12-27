import api from "./api";
import type {
  Expenses,
  CreateExpensesRequest,
  UpdateExpensesRequest,
} from "@/types/Expenses";

export const getExpenses = () => api.get<Expenses[]>("/expenses");

export const getExpenseById = (id: number) =>
  api.get<Expenses>(`/expenses/${id}`);

export const getAllExpenses = () => api.get<Expenses[]>("/expenses/company");

export const createExpense = (data: CreateExpensesRequest) =>
  api.post<Expenses>("/expenses", data);

export const updateExpense = (id: number, data: UpdateExpensesRequest) =>
  api.put<Expenses>(`/expenses/${id}`, data);

export const deleteExpense = (id: number) =>
  api.delete<void>(`/expenses/${id}`);
