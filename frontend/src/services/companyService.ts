import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  CompanyListItem,
  CompanyDetail,
  CreateCompanyRequest,
  UpdateCompanyRequest,
} from "@/types/Company";

const getHeaders = () => {
  const userStore = useUserStore();
  const userId = userStore.currentUser?.id;

  if (!userId) throw new Error("User info missing");
  return { "X-User-Id": userId };
};

export const createCompany = (data: CreateCompanyRequest) =>
  api.post<CompanyDetail>("/companies", data, { headers: getHeaders() });

export const updateCompany = (id: number, data: UpdateCompanyRequest) =>
  api.put<CompanyDetail>(`/companies/${id}`, data, { headers: getHeaders() });

export const getCompanyById = (id: number) =>
  api.get<CompanyDetail>(`/companies/${id}`, { headers: getHeaders() });

export const deleteCompany = (id: number) =>
  api.delete<void>(`/companies/${id}`, { headers: getHeaders() });
