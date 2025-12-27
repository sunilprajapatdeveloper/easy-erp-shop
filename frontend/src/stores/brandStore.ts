import { defineStore } from "pinia";
import type { Brand } from "@/types/Brand";
import {
  getBrands,
  createBrand,
  updateBrand,
  deleteBrand,
} from "@/services/brandService";

export const useBrandStore = defineStore("brand", {
  state: () => ({
    brands: [] as Brand[],
    loading: false,
  }),

  actions: {
    async fetchBrands() {
      this.loading = true;
      try {
        const res = await getBrands();
        this.brands = res.data;
      } catch (err) {
        console.error("Failed to fetch brands:", err);
      } finally {
        this.loading = false;
      }
    },

    async addBrand(brand: Omit<Brand, "id">) {
      try {
        const res = await createBrand(brand);
        this.brands.push(res.data);
      } catch (err) {
        console.error("Failed to add brand:", err);
      }
    },

    async updateBrand(id: number, brand: Omit<Brand, "id">) {
      try {
        const res = await updateBrand(id, brand);
        const index = this.brands.findIndex((b) => b.id === id);
        if (index !== -1) {
          this.brands[index] = res.data;
        }
      } catch (err) {
        console.error("Failed to update brand:", err);
      }
    },

    async removeBrand(id: number) {
      try {
        await deleteBrand(id);
        this.brands = this.brands.filter((b) => b.id !== id);
      } catch (err) {
        console.error("Failed to delete brand:", err);
      }
    },
  },
});
