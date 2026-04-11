import { defineStore } from "pinia";
import type { Product } from "@/types/Product";
import {
  getProducts,
  getProductById,
  getProductByCode,
  getProductByBarcode,
  createProduct,
  updateProduct,
  deleteProduct,
  searchProducts,
  getProductsPaginated,
  bulkDeleteProducts,
  type CreateProductRequest,
  type UpdateProductRequest,
} from "@/services/productService";
import type { PaginationRequest, PaginationResponse } from "@/types/pagination";

export const useProductStore = defineStore("product", {
  state: () => ({
    products: [] as Product[],
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
    currentSort: "" as string,
    currentSearch: "" as string,
  }),

  actions: {
    async fetchProducts(filters?: {
      warehouseId?: number;
      userId?: number;
      includePrice?: boolean;
      includeStock?: boolean;
      includeTax?: boolean;
    }) {
      this.loading = true;
      this.error = null;
      try {
        const res = await getProducts(filters);
        this.products = res.data;
      } catch (err: any) {
        console.error("Fetch failed:", err);
        this.error = err.message ?? "Failed to fetch products";
      } finally {
        this.loading = false;
      }
    },

    async fetchProductById(id: number): Promise<Product | null> {
      try {
        const res = await getProductById(id);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch product by ID:", err);
        return null;
      }
    },

    async fetchProductByCode(code: string): Promise<Product | null> {
      try {
        const res = await getProductByCode(code);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch product by Code:", err);
        return null;
      }
    },

    async fetchProductByBarcode(barcode: string): Promise<Product | null> {
      try {
        const res = await getProductByBarcode(barcode);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch product by Barcode:", err);
        return null;
      }
    },

    async addProduct(product: CreateProductRequest) {
      const res = await createProduct(product);
      this.products.push(res.data);
      return res.data;
    },

    async updateProduct(id: number, data: UpdateProductRequest) {
      const res = await updateProduct(id, data);
      const index = this.products.findIndex((p) => p.id === id);
      if (index !== -1) this.products[index] = res.data;
      return res.data;
    },

    async removeProduct(id: number) {
      await deleteProduct(id);
      this.products = this.products.filter((p) => p.id !== id);
    },

    async searchProducts(query: string, page = 0, size = 20) {
      this.loading = true;
      this.error = null;
      try {
        const res = await searchProducts(query, page, size);
        this.products = res.data;
      } catch (err: any) {
        console.error("Search failed:", err);
        this.error = err.message ?? "Failed to search products";
      } finally {
        this.loading = false;
      }
    },

    async fetchProductsPaginated(params: PaginationRequest) {
      this.loading = true;
      this.error = null;
      try {
        const res = await getProductsPaginated(params);
        const paginatedData = res.data.data;
        this.products = paginatedData.data;
        this.pagination = paginatedData.pagination;
        this.currentSort = params.sort || "";
        this.currentSearch = params.search || "";
      } catch (err: any) {
        console.error("Paginated fetch failed:", err);
        this.error = err.message ?? "Failed to fetch products";
      } finally {
        this.loading = false;
      }
    },

    async resetAndFetch() {
      await this.fetchProductsPaginated({
        page: 0,
        size: this.pagination.size,
        sort: this.currentSort,
        search: this.currentSearch,
      });
    },

    async bulkDelete(ids: number[]) {
      this.loading = true;
      try {
        await bulkDeleteProducts(ids);
        // Remove from local state
        this.products = this.products.filter((p) => !ids.includes(p.id));
        // Optionally refresh pagination counts
        if (this.pagination.totalElements) {
          this.pagination.totalElements -= ids.length;
          this.pagination.totalPages = Math.ceil(
            this.pagination.totalElements / this.pagination.size,
          );
        }
      } catch (err) {
        console.error("Bulk delete error", err);
        throw err;
      } finally {
        this.loading = false;
      }
    },
  },
});
