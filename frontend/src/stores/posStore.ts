import { defineStore } from "pinia";
import { ref, watch } from "vue";
import type {
  Pos,
  CreatePosRequest,
  SelectedPosProduct,
  UpdatePosRequest,
} from "@/types/Pos";
import {
  getAllPos,
  getPosById,
  createPos,
  updatePos,
  deletePos,
} from "@/services/posService";
import { calculateSubTotal } from "@/utils/posUtils";
import { SaleStatus } from "@/enums/saleStatus";

const TEMP_SALE_KEY = "temporarySale";
const TEMP_SALE_EXPIRY_KEY = "temporarySaleExpiry";
const TEMP_SALE_DURATION = 30_000; // 30 seconds

export const usePosStore = defineStore("pos", () => {
  // POS list and selected item
  const posList = ref<Pos[]>([]);
  const selectedPos = ref<Pos | null>(null);

  // Temporary sale
  const temporarySale = ref<Pos | null>(null);
  const expiryTimer = ref<ReturnType<typeof setTimeout> | null>(null);

  // Form-related data
  const selectedProducts = ref<SelectedPosProduct[]>([]);
  const warehouseId = ref<number | null>(null);
  const customerId = ref<number | null>(null);
  const orderTax = ref<number>(0);
  const discount = ref<number>(0);
  const shippingCost = ref<number>(0);
  const note = ref("");

  // State flags
  const loading = ref(false);
  const isSubmitting = ref(false);

  // === Temporary sale helpers ===
  const startExpiryTimer = (ms?: number) => {
    if (expiryTimer.value) clearTimeout(expiryTimer.value);

    const timeout = ms ?? TEMP_SALE_DURATION;

    // Optional warning before expiry
    if (timeout > 5000) {
      setTimeout(() => {
        console.warn("Temporary sale will expire in 5 seconds!");
      }, timeout - 5000);
    }

    expiryTimer.value = setTimeout(() => {
      clearTemporarySale();
      console.log("Temporary sale expired after 30 seconds.");
    }, timeout);
  };

  const setTemporarySale = (sale: Pos) => {
    temporarySale.value = sale;
    const expiry = Date.now() + TEMP_SALE_DURATION;
    localStorage.setItem(TEMP_SALE_KEY, JSON.stringify(sale));
    localStorage.setItem(TEMP_SALE_EXPIRY_KEY, expiry.toString());
    // startExpiryTimer();
  };

  const loadTemporarySaleFromStorage = () => {
    const stored = localStorage.getItem(TEMP_SALE_KEY);
    const expiry = localStorage.getItem(TEMP_SALE_EXPIRY_KEY);

    if (stored && expiry) {
      const expiryTime = parseInt(expiry, 10);
      if (Date.now() < expiryTime) {
        temporarySale.value = JSON.parse(stored);
        startExpiryTimer(expiryTime - Date.now());
      } else {
        clearTemporarySale();
      }
    }
  };

  const clearTemporarySale = () => {
    temporarySale.value = null;
    localStorage.removeItem(TEMP_SALE_KEY);
    localStorage.removeItem(TEMP_SALE_EXPIRY_KEY);
    if (expiryTimer.value) {
      clearTimeout(expiryTimer.value);
      expiryTimer.value = null;
    }
  };

  // Run on store init
  loadTemporarySaleFromStorage();

  // === CRUD ===
  const fetchPos = async () => {
    loading.value = true;
    try {
      const res = await getAllPos();
      posList.value = res.data;
    } catch (error) {
      console.error("Failed to fetch POS records:", error);
    } finally {
      loading.value = false;
    }
  };

  const fetchPosById = async (id: number) => {
    loading.value = true;
    try {
      const res = await getPosById(id);
      selectedPos.value = res.data;
    } catch (error) {
      console.error("Failed to fetch POS by ID:", error);
    } finally {
      loading.value = false;
    }
  };

  const addPos = async (
    data: CreatePosRequest,
    fetchAfter = true
  ): Promise<Pos> => {
    isSubmitting.value = true;
    try {
      const res = await createPos(data);
      if (fetchAfter) {
        await fetchPos();
        reset();
      } else {
        setTemporarySale(res.data);
      }
      return res.data;
    } catch (error) {
      console.error("Failed to create POS:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const editPos = async (id: number, data: UpdatePosRequest) => {
    isSubmitting.value = true;
    try {
      const res = await updatePos(id, data);
      const index = posList.value.findIndex((p) => p.id === id);
      if (index !== -1) {
        posList.value[index] = res.data;
      }
    } catch (error) {
      console.error("Failed to update POS:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // Inside posStore
  const updatePosStatus = async (id: number, saleStatus: SaleStatus) => {
    isSubmitting.value = true;
    try {
      const res = await updatePos(id, { saleStatus } as any);

      const index = posList.value.findIndex((p) => p.id === id);
      if (index !== -1) {
        posList.value[index] = res.data;
      }

      reset();
      clearTemporarySale();

      return res.data;
    } catch (error) {
      console.error("Failed to update POS status:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const removePos = async (id: number) => {
    try {
      await deletePos(id);
      posList.value = posList.value.filter((p) => p.id !== id);
    } catch (error) {
      console.error("Failed to delete POS:", error);
      throw error;
    }
  };

  const addProduct = (product: SelectedPosProduct) => {
    const exists = selectedProducts.value.find(
      (p) => p.productId === product.productId
    );
    if (!exists) {
      selectedProducts.value.push({
        ...product,
        saleQty: product.saleQty ?? 1,
        // Ensure subtotal is a number
        subTotal: calculateSubTotal(product),
        price: product.price,
        discount: product.discount,
        tax: product.tax,
      });
    }
  };

  const updateProductQty = (productId: number, qty: number) => {
    const product = selectedProducts.value.find(
      (p) => p.productId === productId
    );
    if (product && qty >= 1) {
      product.saleQty = qty;
      product.subTotal = calculateSubTotal(product);
    }
  };

  const removeProduct = (productId: number) => {
    selectedProducts.value = selectedProducts.value.filter(
      (p) => p.productId !== productId
    );
  };

  const reset = () => {
    selectedProducts.value = [];
    warehouseId.value = null;
    customerId.value = 1;
    orderTax.value = 0;
    discount.value = 0;
    shippingCost.value = 0;
    note.value = "";
  };

  return {
    posList,
    selectedPos,
    selectedProducts,
    warehouseId,
    customerId,
    orderTax,
    discount,
    shippingCost,
    note,
    loading,
    isSubmitting,
    temporarySale,
    fetchPos,
    fetchPosById,
    addPos,
    editPos,
    removePos,
    addProduct,
    updateProductQty,
    removeProduct,
    reset,
    setTemporarySale,
    clearTemporarySale,
    updatePosStatus,
  };
});
