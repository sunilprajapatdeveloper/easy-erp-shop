import { defineStore } from "pinia";
import type {
  ProductPrice,
  CreateProductPriceRequest,
  UpdateProductPriceRequest,
  ProductPriceResponse,
} from "@/types/ProductPrice";
import {
  getProductPrices,
  getProductPriceById,
  getPricesByProduct,
  getPricesByWarehouse,
  createProductPrice,
  updateProductPrice,
  deleteProductPrice,
  getByProductAndWarehouse,
} from "@/services/productPriceService";

export const useProductPriceStore = defineStore("productPrice", {
  state: () => ({
    prices: [] as ProductPrice[],
    loading: false,
  }),
  actions: {
    async fetchPrices() {
      this.loading = true;
      try {
        const res = await getProductPrices();
        this.prices = res.data;
      } finally {
        this.loading = false;
      }
    },

    async fetchPriceById(id: number): Promise<ProductPrice | null> {
      try {
        const res = await getProductPriceById(id);
        return res.data;
      } catch {
        return null;
      }
    },

    async fetchByProduct(
      productId: number
    ): Promise<ProductPriceResponse | null> {
      this.loading = true;
      try {
        const res = await getPricesByProduct(productId);
        if (res.data && res.data.length > 0) {
          return res.data[0];
        }
        return null;
      } catch (err) {
        console.error("Failed to fetch product price by productId:", err);
        return null;
      } finally {
        this.loading = false;
      }
    },

    async fetchByWarehouse(warehouseId: number) {
      this.loading = true;
      try {
        const res = await getPricesByWarehouse(warehouseId);
        this.prices = res.data;
      } finally {
        this.loading = false;
      }
    },

    async fetchByProductAndWarehouse(
      productId: number,
      warehouseId: number
    ): Promise<ProductPrice | null> {
      try {
        const res = await getByProductAndWarehouse(productId, warehouseId);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch price by product+warehouse:", err);
        return null;
      }
    },

    async addPrice(data: CreateProductPriceRequest) {
      const res = await createProductPrice(data);
      this.prices.push(res.data);
      return res.data;
    },

    async updatePrice(id: number, data: UpdateProductPriceRequest) {
      const res = await updateProductPrice(id, data);
      const idx = this.prices.findIndex((p) => p.id === id);
      if (idx !== -1) this.prices[idx] = res.data;
      return res.data;
    },

    async removePrice(id: number) {
      await deleteProductPrice(id);
      this.prices = this.prices.filter((p) => p.id !== id);
    },
  },
});
