import api from "./api";
import type {
  WarehouseCurrency,
  CreateWarehouseCurrencyRequest,
  UpdateWarehouseCurrencyRequest,
} from "@/types/WarehouseCurrency";

export const warehouseCurrencyService = {
  /**
   * List all warehouse currencies
   */
  list(companyId: number, warehouseId: number) {
    return api.get<WarehouseCurrency[]>("/warehouse-currencies", {
      headers: {
        "X-Company-Id": companyId,
        "X-Warehouse-Id": warehouseId,
      },
    });
  },

  /**
   * Get a single warehouse currency by ID
   */
  get(id: number, companyId: number, warehouseId: number) {
    return api.get<WarehouseCurrency>(`/warehouse-currencies/${id}`, {
      headers: {
        "X-Company-Id": companyId,
        "X-Warehouse-Id": warehouseId,
      },
    });
  },

  /**
   * Get default currency for a warehouse
   */
  getDefault(companyId: number, warehouseId: number) {
    return api.get<WarehouseCurrency>(`/warehouse-currencies/default`, {
      headers: {
        "X-Company-Id": companyId,
        "X-Warehouse-Id": warehouseId,
      },
    });
  },

  /**
   * Create a new warehouse currency
   */
  create(
    companyId: number,
    warehouseId: number,
    payload: CreateWarehouseCurrencyRequest
  ) {
    return api.post<WarehouseCurrency>("/warehouse-currencies", payload, {
      headers: {
        "X-Company-Id": companyId,
        "X-Warehouse-Id": warehouseId,
      },
    });
  },

  /**
   * Update an existing warehouse currency
   */
  update(
    id: number,
    companyId: number,
    warehouseId: number,
    payload: UpdateWarehouseCurrencyRequest
  ) {
    return api.put<WarehouseCurrency>(`/warehouse-currencies/${id}`, payload, {
      headers: {
        "X-Company-Id": companyId,
        "X-Warehouse-Id": warehouseId,
      },
    });
  },

  /**
   * Delete a warehouse currency
   */
  delete(id: number, companyId: number, warehouseId: number) {
    return api.delete(`/warehouse-currencies/${id}`, {
      headers: {
        "X-Company-Id": companyId,
        "X-Warehouse-Id": warehouseId,
      },
    });
  },
};
