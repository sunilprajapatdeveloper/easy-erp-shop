import { defineStore } from "pinia";
import type {
  ProductTax,
  CreateProductTaxRequest,
  UpdateProductTaxRequest,
} from "@/types/ProductTax";
import {
  getAllProductTaxes,
  getProductTaxById,
  getTaxesByProduct,
  getTaxesByWarehouse,
  createProductTax,
  updateProductTax,
  deleteProductTax,
  getEffectiveTax,
} from "@/services/productTaxService";

export const useProductTaxStore = defineStore("productTax", {
  state: () => ({
    taxes: [] as ProductTax[],
    loading: false,
  }),
  actions: {
    async fetchAllTaxes() {
      this.loading = true;
      try {
        const res = await getAllProductTaxes();
        this.taxes = res.data;
      } finally {
        this.loading = false;
      }
    },

    async fetchTaxById(id: number): Promise<ProductTax | null> {
      try {
        const res = await getProductTaxById(id);
        return res.data;
      } catch {
        return null;
      }
    },

    async fetchTaxesByProduct(productId: number): Promise<ProductTax[]> {
      this.loading = true;
      try {
        const res = await getTaxesByProduct(productId);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch taxes by product:", err);
        return [];
      } finally {
        this.loading = false;
      }
    },

    async fetchTaxesByWarehouse(warehouseId: number): Promise<ProductTax[]> {
      this.loading = true;
      try {
        const res = await getTaxesByWarehouse(warehouseId);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch taxes by warehouse:", err);
        return [];
      } finally {
        this.loading = false;
      }
    },

    async addTax(data: CreateProductTaxRequest): Promise<ProductTax> {
      const res = await createProductTax(data);
      this.taxes.push(res.data);
      return res.data;
    },

    async modifyTax(
      id: number,
      data: UpdateProductTaxRequest,
    ): Promise<ProductTax> {
      const res = await updateProductTax(id, data);
      const idx = this.taxes.findIndex((t) => t.id === id);
      if (idx !== -1) this.taxes[idx] = res.data;
      return res.data;
    },

    async removeTax(id: number): Promise<void> {
      await deleteProductTax(id);
      this.taxes = this.taxes.filter((t) => t.id !== id);
    },

    async fetchEffectiveTax(
      productId: number,
      taxCode: string,
      warehouseId?: number,
    ): Promise<ProductTax | null> {
      try {
        const res = await getEffectiveTax(productId, taxCode, warehouseId);
        return res.data;
      } catch (err) {
        console.error("Failed to fetch effective tax:", err);
        return null;
      }
    },
  },
});
