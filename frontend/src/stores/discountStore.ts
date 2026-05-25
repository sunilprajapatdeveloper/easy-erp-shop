import { defineStore } from "pinia";
import {
  getDiscountsPaginated,
  getDiscountById,
  createDiscount,
  updateDiscount,
  deleteDiscount,
  toggleDiscount,
  getActiveOrderDiscounts,
} from "@/services/discountService";
import type { PaginationRequest, PaginationResponse } from "@/types/pagination";
import {
  CreateDiscountRequest,
  DiscountItem,
  DiscountResponse,
  UpdateDiscountRequest,
} from "@/types/Discount";

export const useDiscountStore = defineStore("discount", {
  state: () => ({
    discounts: [] as DiscountResponse[],
    currentDiscount: null as DiscountResponse | null,
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
    currentFilters: {} as Record<string, any>,
  }),

  actions: {
    /** Fetch paginated discounts, optionally with filters */
    async fetchDiscounts(
      params: PaginationRequest & {
        scope?: string;
        active?: boolean;
        warehouseId?: number;
      },
    ) {
      this.loading = true;
      this.error = null;
      try {
        const res = await getDiscountsPaginated({
          ...params,
          sort: this.currentSort,
        });
        const paginatedData = res.data.data;
        this.discounts = paginatedData.data;
        this.pagination = paginatedData.pagination;
        this.currentFilters = {
          scope: params.scope,
          active: params.active,
          warehouseId: params.warehouseId,
        };
      } catch (err: any) {
        console.error("Fetch discounts failed:", err);
        this.error = err.message ?? "Failed to fetch discounts";
      } finally {
        this.loading = false;
      }
    },

    /** Fetch single discount by ID */
    async fetchDiscountById(id: number): Promise<DiscountResponse | null> {
      this.loading = true;
      try {
        const res = await getDiscountById(id);
        this.currentDiscount = res.data.data;
        return this.currentDiscount;
      } catch (err) {
        console.error("Fetch discount by ID failed:", err);
        return null;
      } finally {
        this.loading = false;
      }
    },

    /** Create a new discount */
    async addDiscount(data: CreateDiscountRequest): Promise<DiscountResponse> {
      this.loading = true;
      try {
        const res = await createDiscount(data);
        const newDiscount = res.data.data;
        // Add to list if we are on the first page (and no filters that would exclude it)
        if (
          this.pagination.page === 0 &&
          (!this.currentFilters.scope ||
            this.currentFilters.scope === newDiscount.scope) &&
          (!this.currentFilters.active ||
            newDiscount.isActive === this.currentFilters.active) &&
          (!this.currentFilters.warehouseId ||
            newDiscount.warehouseId === this.currentFilters.warehouseId)
        ) {
          this.discounts.unshift(newDiscount);
          this.pagination.totalElements += 1;
        }
        return newDiscount;
      } catch (err) {
        console.error("Create discount failed:", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /** Update an existing discount */
    async updateDiscount(
      id: number,
      data: UpdateDiscountRequest,
    ): Promise<DiscountResponse> {
      this.loading = true;
      try {
        const res = await updateDiscount(id, data);
        const updated = res.data.data;
        const index = this.discounts.findIndex((d) => d.id === id);
        if (index !== -1) this.discounts[index] = updated;
        if (this.currentDiscount?.id === id) this.currentDiscount = updated;
        return updated;
      } catch (err) {
        console.error("Update discount failed:", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /** Delete a discount */
    async removeDiscount(id: number): Promise<void> {
      this.loading = true;
      try {
        await deleteDiscount(id);
        this.discounts = this.discounts.filter((d) => d.id !== id);
        if (this.currentDiscount?.id === id) this.currentDiscount = null;
        if (this.pagination.totalElements) {
          this.pagination.totalElements -= 1;
          this.pagination.totalPages = Math.ceil(
            this.pagination.totalElements / this.pagination.size,
          );
        }
      } catch (err) {
        console.error("Delete discount failed:", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /** Toggle active status of a discount */
    async toggleDiscountStatus(id: number): Promise<void> {
      this.loading = true;
      try {
        await toggleDiscount(id);
        const discount = this.discounts.find((d) => d.id === id);
        if (discount) discount.isActive = !discount.isActive;
        if (this.currentDiscount?.id === id)
          this.currentDiscount.isActive = !this.currentDiscount.isActive;
      } catch (err) {
        console.error("Toggle discount status failed:", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Fetch active order‑level discounts for a warehouse.
     * Used in sale create/edit forms to populate the dropdown.
     */
    async fetchActiveOrderDiscounts(
      warehouseId: number,
    ): Promise<DiscountItem[]> {
      this.loading = true;
      try {
        // Call the API that returns only active ORDER discounts for the warehouse
        const res = await getActiveOrderDiscounts(warehouseId);
        const discounts = res.data.data.data; // Paginated response data array

        // Map to simplified DiscountItem
        const items: DiscountItem[] = discounts.map((d) => ({
          id: d.id,
          name: d.name,
          code: d.code ?? "",
          discountType: d.discountType,
          discountValue: d.discountValue,
        }));

        return items;
      } catch (err: any) {
        console.error("Fetch active order discounts failed:", err);
        return [];
      } finally {
        this.loading = false;
      }
    },
  },
});
