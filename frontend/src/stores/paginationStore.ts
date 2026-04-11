import { defineStore } from "pinia";
import type { Product } from "@/types/Product";
import type { Sale } from "@/types/Sale";
import type { PaginationRequest, PaginationResponse } from "@/types/pagination";
import { getProductsPaginated } from "@/services/productService";

export const usePaginationStore = defineStore("pagination", {
  state: () => ({
    products: {
      data: [] as Product[],
      loading: false,
      pagination: null as PaginationResponse<any>["pagination"] | null,
    },
    sales: {
      data: [] as Sale[],
      loading: false,
      pagination: null as PaginationResponse<any>["pagination"] | null,
    },
  }),
  actions: {
    /**
     * Fetch paginated products
     */
    async fetchProducts(
      params: PaginationRequest,
    ): Promise<PaginationResponse<Product>> {
      this.products.loading = true;
      try {
        const response = await getProductsPaginated(params);
        const paginatedData = response.data.data;
        this.products.data = paginatedData.data;
        this.products.pagination = paginatedData.pagination;
        return paginatedData;
      } catch (error) {
        console.error("Failed to fetch products:", error);
        throw error;
      } finally {
        this.products.loading = false;
      }
    },
  },
});
