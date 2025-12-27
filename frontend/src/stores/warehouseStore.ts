import { defineStore } from "pinia";
import type {
  WarehouseListItem,
  WarehouseDetail,
  CreateWarehouseRequest,
  UpdateWarehouseRequest,
} from "@/types/Warehouse";
import {
  getWarehouses,
  getWarehouseById,
  createWarehouse,
  updateWarehouse,
  deleteWarehouse,
} from "@/services/warehouseService";

// Strongly typed state interface
interface WarehouseState {
  warehouses: WarehouseListItem[];
  warehouseDetails: Map<number, WarehouseDetail>;
  loading: boolean;
  error: string | null;
}

export const useWarehouseStore = defineStore("warehouse", {
  state: (): WarehouseState => ({
    warehouses: [],
    warehouseDetails: new Map<number, WarehouseDetail>(),
    loading: false,
    error: null,
  }),

  getters: {
    warehouseMap: (state): Record<number, string> =>
      state.warehouses.reduce<Record<number, string>>((map, w) => {
        map[w.id] = w.name;
        return map;
      }, {}),
  },

  actions: {
    async fetchWarehouses(activeOnly = true) {
      this.loading = true;
      this.error = null;
      try {
        const res = await getWarehouses({ active: activeOnly });
        this.warehouses = res.data;
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to fetch warehouses";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async fetchWarehouseDetail(id: number) {
      if (this.warehouseDetails.has(id)) {
        return this.warehouseDetails.get(id)!; // return cached
      }
      this.error = null;
      try {
        const res = await getWarehouseById(id);
        this.warehouseDetails.set(id, res.data);
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to fetch warehouse detail";
        throw err;
      }
    },

    async addWarehouse(data: CreateWarehouseRequest) {
      this.loading = true;
      this.error = null;
      try {
        const res = await createWarehouse(data);
        const newWarehouse: WarehouseListItem = {
          id: res.data.id,
          name: res.data.name,
          city: res.data.city,
          country: res.data.country,
          currencyId: res.data.currencyId,
          headquarter: res.data.headquarter,
          isDefault: res.data.isDefault,
        };
        this.warehouses.push(newWarehouse);
        this.warehouseDetails.set(res.data.id, res.data);
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to add warehouse";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updateWarehouse(id: number, data: UpdateWarehouseRequest) {
      this.loading = true;
      this.error = null;
      try {
        const res = await updateWarehouse(id, data);
        const updatedWarehouse: WarehouseListItem = {
          id: res.data.id,
          name: res.data.name,
          city: res.data.city,
          country: res.data.country,
          currencyId: res.data.currencyId,
          headquarter: res.data.headquarter,
          isDefault: res.data.isDefault,
        };

        const index = this.warehouses.findIndex((w) => w.id === id);
        if (index !== -1) {
          this.warehouses[index] = updatedWarehouse;
        }
        this.warehouseDetails.set(res.data.id, res.data);
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to update warehouse";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async removeWarehouse(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await deleteWarehouse(id);
        this.warehouses = this.warehouses.filter((w) => w.id !== id);
        this.warehouseDetails.delete(id);
      } catch (err: any) {
        this.error = err.message ?? "Failed to delete warehouse";
        throw err;
      } finally {
        this.loading = false;
      }
    },
  },
});
