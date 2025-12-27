import { defineStore } from "pinia";
import type { Category } from "@/types/Category";
import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "@/services/categoryService";

export const useCategoryStore = defineStore("category", {
  state: () => ({
    categories: [] as Category[],
    loading: false,
  }),

  actions: {
    async fetchCategories() {
      this.loading = true;
      try {
        const res = await getCategories();
        this.categories = res.data;
      } catch (err) {
        console.error("Failed to fetch categories:", err);
      } finally {
        this.loading = false;
      }
    },

    async addCategory(category: Omit<Category, "id">) {
      try {
        const res = await createCategory(category);
        this.categories.push(res.data);
      } catch (err) {
        console.error("Failed to add category:", err);
      }
    },

    async updateCategory(id: number, category: Omit<Category, "id">) {
      try {
        const res = await updateCategory(id, category);
        const index = this.categories.findIndex((c) => c.id === id);
        if (index !== -1) {
          this.categories[index] = res.data;
        }
      } catch (err) {
        console.error("Failed to update category:", err);
      }
    },

    async removeCategory(id: number) {
      try {
        await deleteCategory(id);
        this.categories = this.categories.filter((c) => c.id !== id);
      } catch (err) {
        console.error("Failed to delete category:", err);
      }
    },
  },
});
