import { defineStore } from "pinia";
import type {
  CompanyDetail,
  CreateCompanyRequest,
  UpdateCompanyRequest,
} from "@/types/Company";
import {
  createCompany,
  updateCompany,
  getCompanyById,
  deleteCompany,
} from "@/services/companyService";

export const useCompanyStore = defineStore("company", {
  state: () => ({
    companies: [] as CompanyDetail[], // optional list
    companyDetails: new Map<number, CompanyDetail>(), // cache by id
    loading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchCompanyDetail(id: number) {
      if (this.companyDetails.has(id)) {
        return this.companyDetails.get(id)!;
      }

      this.loading = true;
      this.error = null;
      try {
        const res = await getCompanyById(id);
        this.companyDetails.set(id, res.data);
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to fetch company details";
        return null;
      } finally {
        this.loading = false;
      }
    },

    async addCompany(data: CreateCompanyRequest) {
      this.loading = true;
      this.error = null;
      try {
        const res = await createCompany(data);
        this.companyDetails.set(res.data.id, res.data);
        this.companies.push(res.data);
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to create company";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updateCompany(id: number, data: UpdateCompanyRequest) {
      this.loading = true;
      this.error = null;
      try {
        const res = await updateCompany(id, data);
        this.companyDetails.set(id, res.data);

        const idx = this.companies.findIndex((c) => c.id === id);
        if (idx !== -1) {
          this.companies[idx] = res.data;
        }
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to update company";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async removeCompany(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await deleteCompany(id);
        this.companyDetails.delete(id);
        this.companies = this.companies.filter((c) => c.id !== id);
      } catch (err: any) {
        this.error = err.message ?? "Failed to delete company";
        throw err;
      } finally {
        this.loading = false;
      }
    },
  },
});
