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
  type CreateProductRequest,
  type UpdateProductRequest,
} from "@/services/productService";

export const useProductStore = defineStore("product", {
  state: () => ({
    products: [] as Product[],
    loading: false,
    error: null as string | null,
  }),

  actions: {
    /**
     * Fetch products with optional filters (warehouse, stock, tax, etc.)
     */
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
      if (index !== -1) {
        this.products[index] = res.data;
      }
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
  },
});
