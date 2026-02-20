import api from "./api";
import type {
  CompanyListItem,
  CompanyDetail,
  CreateCompanyRequest,
  UpdateCompanyRequest,
} from "@/types/Company";

export const createCompany = (data: CreateCompanyRequest) =>
  api.post<CompanyDetail>("/companies", data);

export const updateCompany = (id: number, data: UpdateCompanyRequest) =>
  api.put<CompanyDetail>(`/companies/${id}`, data);

export const getCompanyById = (id: number) =>
  api.get<CompanyDetail>(`/companies/${id}`);

export const deleteCompany = (id: number) =>
  api.delete<void>(`/companies/${id}`);
