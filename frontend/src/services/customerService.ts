import api from "./api";
import type { Customer, CreateCustomerRequest } from "@/types/Customer";

export const getCustomers = () => api.get<Customer[]>("/customers");

export const createCustomer = (data: CreateCustomerRequest) =>
  api.post<Customer>("/customers", data);

export const updateCustomer = (id: number, data: CreateCustomerRequest) =>
  api.put<Customer>(`/customers/${id}`, data);

export const deleteCustomer = (id: number) => api.delete(`/customers/${id}`);
