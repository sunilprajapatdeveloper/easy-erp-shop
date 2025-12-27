import { defineStore } from "pinia";
import type {
  ProductStock,
  CreateProductStockRequest,
  UpdateProductStockRequest,
} from "@/types/ProductStock";
import {
  getProductStocks,
  getProductStockById,
  getByProductAndWarehouse,
  createProductStock,
  updateProductStock,
  adjustProductStock,
  deleteProductStock,
} from "@/services/productStockService";

export const useProductStockStore = defineStore("productStock", {
  state: () => ({
    stocks: [] as ProductStock[],
    loading: false,
  }),
  actions: {
    async fetchStocks() {
      this.loading = true;
      try {
        const res = await getProductStocks();
        this.stocks = res.data;
      } catch (err) {
        console.error("Failed to fetch stocks:", err);
      } finally {
        this.loading = false;
      }
    },

    async fetchStockById(id: number): Promise<ProductStock | null> {
      try {
        const res = await getProductStockById(id);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch stock by ID:", err);
        return null;
      }
    },

    async fetchByProductAndWarehouse(
      productId: number,
      warehouseId: number
    ): Promise<ProductStock | null> {
      try {
        const res = await getByProductAndWarehouse(productId, warehouseId);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch stock by product+warehouse:", err);
        return null;
      }
    },

    async addStock(stock: CreateProductStockRequest) {
      const res = await createProductStock(stock);
      this.stocks.push(res.data);
    },

    async updateStock(id: number, data: UpdateProductStockRequest) {
      const res = await updateProductStock(id, data);
      const index = this.stocks.findIndex((s) => s.id === id);
      if (index !== -1) {
        this.stocks[index] = res.data;
      }
    },

    async adjustStock(productId: number, warehouseId: number, delta: number) {
      const res = await adjustProductStock(productId, warehouseId, delta);
      const index = this.stocks.findIndex(
        (s) => s.productId === productId && s.warehouseId === warehouseId
      );
      if (index !== -1) {
        this.stocks[index] = res.data;
      } else {
        this.stocks.push(res.data);
      }
    },

    async removeStock(id: number) {
      await deleteProductStock(id);
      this.stocks = this.stocks.filter((s) => s.id !== id);
    },
  },
});
