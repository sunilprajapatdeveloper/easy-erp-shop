import { defineStore } from "pinia";
import type {
  PromotionResponse,
  CreatePromotionRequest,
  UpdatePromotionRequest,
} from "@/types/promotion";
import {
  getPromotionsPaginated,
  getPromotionById,
  createPromotion,
  updatePromotion,
  deletePromotion,
  togglePromotion,
} from "@/services/promotionService";
import type { PaginationRequest, PaginationResponse } from "@/types/pagination";

export const usePromotionStore = defineStore("promotion", {
  state: () => ({
    promotions: [] as PromotionResponse[],
    currentPromotion: null as PromotionResponse | null,
    loading: false,
    error: null as string | null,
    pagination: {
      page: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0,
      hasNext: false,
      hasPrevious: false,
    } as PaginationResponse<any>["pagination"],
    currentSort: "",
    currentSearch: "",
  }),

  actions: {
    async fetchPromotions(params: PaginationRequest) {
      this.loading = true;
      this.error = null;
      try {
        // Ensure we send sort and search if present
        const requestParams: any = { ...params };
        if (this.currentSort) requestParams.sort = this.currentSort;
        if (this.currentSearch) requestParams.search = this.currentSearch;
        const res = await getPromotionsPaginated(requestParams);
        const paginatedData = res.data.data;
        this.promotions = paginatedData.data;
        this.pagination = paginatedData.pagination;
        this.currentSort = params.sort || "";
        this.currentSearch = params.search || "";
      } catch (err: any) {
        console.error("Fetch promotions failed:", err);
        this.error = err.message ?? "Failed to fetch promotions";
      } finally {
        this.loading = false;
      }
    },

    async fetchPromotionById(id: number): Promise<PromotionResponse | null> {
      this.loading = true;
      try {
        const res = await getPromotionById(id);
        this.currentPromotion = res.data.data;
        return this.currentPromotion;
      } catch (err) {
        console.error("Fetch promotion by ID failed:", err);
        return null;
      } finally {
        this.loading = false;
      }
    },

    async addPromotion(
      data: CreatePromotionRequest,
    ): Promise<PromotionResponse> {
      this.loading = true;
      try {
        const res = await createPromotion(data);
        const newPromo = res.data.data;
        // Optionally add to list if on first page
        if (this.pagination.page === 0) {
          this.promotions.unshift(newPromo);
        }
        return newPromo;
      } catch (err) {
        console.error("Create promotion failed:", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updatePromotion(
      id: number,
      data: UpdatePromotionRequest,
    ): Promise<PromotionResponse> {
      this.loading = true;
      try {
        const res = await updatePromotion(id, data);
        const updated = res.data.data;
        const index = this.promotions.findIndex((p) => p.id === id);
        if (index !== -1) this.promotions[index] = updated;
        if (this.currentPromotion?.id === id) this.currentPromotion = updated;
        return updated;
      } catch (err) {
        console.error("Update promotion failed:", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async removePromotion(id: number): Promise<void> {
      this.loading = true;
      try {
        await deletePromotion(id);
        this.promotions = this.promotions.filter((p) => p.id !== id);
        if (this.currentPromotion?.id === id) this.currentPromotion = null;
        // Update pagination counts
        if (this.pagination.totalElements) {
          this.pagination.totalElements -= 1;
          this.pagination.totalPages = Math.ceil(
            this.pagination.totalElements / this.pagination.size,
          );
        }
      } catch (err) {
        console.error("Delete promotion failed:", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async togglePromotionStatus(id: number): Promise<void> {
      this.loading = true;
      try {
        await togglePromotion(id);
        const promo = this.promotions.find((p) => p.id === id);
        if (promo) promo.isActive = !promo.isActive;
        if (this.currentPromotion?.id === id)
          this.currentPromotion.isActive = !this.currentPromotion.isActive;
      } catch (err) {
        console.error("Toggle promotion status failed:", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async resetAndFetch() {
      await this.fetchPromotions({
        page: 0,
        size: this.pagination.size,
        sort: this.currentSort,
        search: this.currentSearch,
      });
    },
  },
});
