<template>
  <div class="pos-right">
    <form class="pb-60">
      <!-- Top Inputs -->
      <div class="row gx-xxl-6">
        <!-- Warehouse -->
        <div class="col-lg-6">
          <div class="form-group mb-20">
            <label class="d-block fs-14 text-black mb-2">Warehouse</label>
            <select class="bg-white border-0 rounded-1 fs-14 text-optional w-100" v-model="posStore.warehouseId">
              <option :value="null" disabled>Choose Warehouse</option>
              <option v-for="wh in warehouseStore.warehouses" :key="wh.id" :value="wh.id">
                {{ wh.name }}
              </option>
            </select>
          </div>
        </div>

        <!-- Customer -->
        <div class="col-lg-6">
          <div class="form-group mb-20">
            <label class="d-block fs-14 text-black mb-2">Customer</label>
            <select class="bg-white border-0 rounded-1 fs-14 text-optional w-100" v-model="posStore.customerId">
              <option :value="null" disabled>Select Customer</option>
              <option v-for="customer in customerStore.customers" :key="customer.id" :value="customer.id">
                {{ customer.name }}
              </option>
            </select>
          </div>
        </div>

        <!-- Product Search -->
        <div class="col-12">
          <div class="form-group position-relative mb-20">
            <label class="d-block fs-14 text-black mb-2">Choose Product</label>
            <div class="search-area style-two position-relative w-100">
              <input type="text" placeholder="Scan / Search product by code"
                class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" v-model="searchQuery"
                @input="updateFilteredProducts" @keydown.enter.prevent="handleSearch" />
              <button type="button" @click="handleSearch"
                class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
                <img src="../../../assets/img/icons/search.svg" alt="Search" />
              </button>
            </div>

            <!-- Product Dropdown -->
            <div v-if="searchQuery.trim()" class="position-absolute w-100 mt-1 bg-white border rounded shadow"
              style="z-index: 1050; max-height: 180px; overflow-y: auto;">

              <!-- Case 1: No warehouse selected -->
              <div v-if="!posStore.warehouseId" class="text-center fw-semibold text-danger small py-3">
                Please select a warehouse
              </div>

              <!-- Case 2: Products found -->
              <ul v-else-if="filteredProducts.length" class="list-group list-group-flush m-0">
                <li v-for="product in filteredProducts" :key="product.id" @mousedown.prevent="selectProduct(product)"
                  class="list-group-item list-group-item-action px-3 py-2" style="cursor: pointer;">
                  <div class="fw-semibold small text-dark">
                    {{ product.name }}
                    <span class="text-muted ms-1">({{ product.code }})</span>
                  </div>
                </li>
              </ul>

              <!-- Case 3: No products found -->
              <div v-else class="text-center fw-semibold text-muted small py-3">
                No products found.
              </div>
            </div>
          </div>
        </div>

        <!-- <div class="scanner-section mb-3"> -->
          <!-- <div class="d-flex justify-content-between align-items-center mb-2">
            <label class="d-block fs-14 text-black mb-0">Barcode Scanner</label>
            <button type="button" class="btn btn-outline-primary btn-sm" @click="toggleScannerModal">
              {{ showScannerModal ? 'Close Scanner' : 'Open Scanner' }}
            </button>
          </div> -->

          <!-- Scanner Modal -->
          <!-- <div v-if="showScannerModal" class="scanner-modal-overlay">
            <div class="scanner-modal-content">
              <div class="scanner-modal-header">
                <h5>Barcode Scanner</h5>
                <button type="button" class="btn-close" @click="toggleScannerModal"></button>
              </div>
              <div class="scanner-modal-body">
                <PosScannerIntegration :company-id="userStore.currentUser?.companyId || 1"
                  :warehouse-id="posStore.warehouseId || 1" :user-id="userStore.currentUser?.id || 1"
                  @close="toggleScannerModal" />
              </div>
            </div>
          </div> -->

          <!-- Real-time scan notification -->
          <!-- <div v-if="lastScan" class="scan-notification alert alert-success py-2 mb-2">
            <div class="d-flex justify-content-between align-items-center">
              <span>
                <strong>Scanned:</strong> {{ lastScan.productName }} - ${{ lastScan.price }}
              </span>
              <button type="button" class="btn-close" @click="clearLastScan"></button>
            </div>
          </div> -->
        <!-- </div> -->
      </div>

      <!-- Totals Section -->
      <div class="total-cost">
        <div class="row pb-10 mb-10 px-3 gx-xxl-7">
          <div class="col-xl-4" v-for="(label, i) in ['Total Item', 'Total Cost', 'Discount']" :key="i">
            <div class="d-flex justify-content-between align-items-center">
              <span class="fs-14 text-title">{{ label }}</span>
              <div class="fs-14 fw-bold text-title">
                <span>
                  {{ label === 'Total Item' ? posStore.selectedProducts.length : '00.00' }}
                </span>
                <button v-if="label === 'Discount'" class="border-0 bg-transparent p-0 ms-2" data-bs-toggle="modal"
                  data-bs-target="#filterModal">
                  <img src="../../../assets/img/icons/edit.svg" alt="Edit" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <hr />

        <div class="row mb-20 px-3 gx-5">
          <div class="col-xl-4" v-for="(label, i) in ['Coupon', 'Tax', 'Shipping']" :key="i">
            <div class="d-flex justify-content-between align-items-center">
              <span class="fs-14 text-title">{{ label }}</span>
              <div class="fs-14 fw-bold text-title">
                <span>00.00</span>
                <button class="border-0 bg-transparent p-0 ms-2"
                  :data-bs-target="label === 'Shipping' ? '#detailsModal' : '#filterModal'" data-bs-toggle="modal">
                  <img src="../../../assets/img/icons/edit.svg" alt="Edit" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Grand Total -->
        <div class="grand-total">Grand Total: <span>{{ totalAmount.toFixed(2) }}</span></div>

        <!-- Payment Buttons -->
        <div class="btn-wrapper d-flex align-items-center flex-wrap gap-2">
          <button type="button" class="btn custom-theme-btn text-uppercase w-100" @click="openPaymentSectionModal">
            CONTINUE
          </button>
        </div>

        <!-- Payment Modal -->
        <div class="modal fade" id="paymentModal" tabindex="-1" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content p-4">
              <PaymentGateway v-if="selectedMethod" :method="selectedMethod" :totalAmount="totalAmount"
                @payment-completed="handlePayment" :key="selectedMethod + '-' + paymentModalKey" />
            </div>
          </div>
        </div>

        <!-- Success Popup -->
        <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
          <div class="offcanvas-body p-0">
            <div class="create-success">
              <img src="@/assets/img/icons/tick-circle.svg" alt="Image" />
              <span class="text-white fw-medium">{{ successMessage }}</span>
            </div>
          </div>
        </div>
        <a id="triggerSuccessPopup" class="d-none" data-bs-toggle="offcanvas" href="#successPopup" role="button"
          aria-controls="successPopup"></a>

        <!-- Error Popup -->
        <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
          <div class="offcanvas-body p-0">
            <div class="create-error">
              <img src="@/assets/img/icons/close-circle-2.svg" alt="Image" style="filter: brightness(0) invert(1);" />
              <span class="text-white fw-medium">{{ errorMessage }}</span>
            </div>
          </div>
        </div>
        <a id="triggerErrorPopup" class="d-none" data-bs-toggle="offcanvas" href="#errorPopup" role="button"
          aria-controls="errorPopup"></a>
      </div>

      <!-- Payment Section Modal -->
      <div class="modal fade" id="paymentSectionModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered width-75">
          <div class="modal-content p-4">
            <PaymentSection :grandTotal="totalAmount" @payment-completed="handlePayment"
              @open-payment-modal="openPaymentModal" @confirm-sale="handleConfirmSale" />
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, onUnmounted } from "vue";
import { usePosStore } from "@/stores/posStore";
import { usePOSSettingsStore } from "@/stores/posSettingsStore";
import { useProductStore } from "@/stores/productStore";
import { useWarehouseStore } from '@/stores/warehouseStore';
import { useCustomerStore } from "@/stores/customerStore";
import { useUserStore } from "@/stores/userStore";
import { useWarehouseCurrencyStore } from "@/stores/warehouseCurrencyStore";

import PaymentGateway from "@/components/PaymentMethods/PaymentGateway.vue";
import PaymentSection from "@/components/PaymentMethods/PaymentSection.vue";

import type { Product } from "@/types/Product";
import type { SelectedPosProduct, CreatePosRequest } from "@/types/Pos";
import { ShipmentStatus } from "@/enums/shipmentStatus";
import { SaleStatus } from "@/enums/saleStatus";
import { PaymentMethodKey } from "@/enums/paymentMethods";
import { TaxType } from "@/types/TaxTypes";
import { showPopup } from "@/utils/showPopup";
import type { BarcodeScanResponse } from '@/types/barcodeScanner';
import PosScannerIntegration from "@/components/Pos/PosScannerIntegration.vue";
import { useBarcodeScannerStore } from '@/stores/barcodeScannerStore';

// ----------------- STORES -----------------
const posStore = usePosStore();
const posSettingsStore = usePOSSettingsStore();
const productStore = useProductStore();
const warehouseStore = useWarehouseStore();
const customerStore = useCustomerStore();
const userStore = useUserStore();
const warehouseCurrencyStore = useWarehouseCurrencyStore();

// ----------------- STATE -----------------
const searchQuery = ref("");
const filteredProducts = ref<Product[]>([]);
const allProducts = ref<Product[]>([]);
const selectedMethod = ref<PaymentMethodKey | null>(null);

const successMessage = ref("");
const errorMessage = ref("");

const grandTotal = ref(0);
const remainingAmount = ref(0);
const paymentModalKey = ref(0);

// ----------------- COMPUTED -----------------
const totalAmount = computed(() =>
  posStore.selectedProducts.reduce(
    (sum, p) => sum + (p.subTotal || 0),
    0
  )
);

const showScannerModal = ref(false);
const scannerStore = useBarcodeScannerStore();
const lastScan = computed(() => scannerStore.lastScan);

const toggleScannerModal = () => {
  showScannerModal.value = !showScannerModal.value;
};

const clearLastScan = () => {
  scannerStore.clearLastScan();
};

// ----------------- WATCHERS -----------------
watch(totalAmount, (newTotal) => {
  grandTotal.value = newTotal;
  remainingAmount.value = newTotal;
}, { immediate: true });

watch(() => posStore.warehouseId, async (newWh, oldWh) => {
  if (newWh && newWh !== oldWh) {
    await loadProducts(newWh);
  }
});

// ----------------- LIFECYCLE -----------------
onMounted(async () => {
  try {
    if (!warehouseStore.warehouses.length) await warehouseStore.fetchWarehouses();
    if (!customerStore.customers.length) await customerStore.fetchCustomers();

    if (userStore.currentUser?.defaultWarehouseId) {
      posStore.warehouseId = userStore.currentUser.defaultWarehouseId;
    } else if (warehouseStore.warehouses.length) {
      posStore.warehouseId = warehouseStore.warehouses[0].id; // fallback
    }

    if (posStore.warehouseId) await loadProducts(posStore.warehouseId);

    if (posStore.warehouseId) {
      const defaults = await posSettingsStore.loadPOSSettings(posStore.warehouseId);

      // Set default customer
      if (defaults.defaultCustomerId) {
        posStore.customerId = defaults.defaultCustomerId;
      }

      // Set default currency in warehouseCurrencyStore
      if (defaults.defaultCurrencyId) {
        warehouseCurrencyStore.setSelectedCurrency(defaults.defaultCurrencyId);
      }
    }

    allProducts.value = productStore.products;

  } catch (err) {
    console.error("Failed to initialize POS:", err);
  }
});

// ----------------- METHODS -----------------
const loadProducts = async (warehouseId: number) => {
  if (!warehouseId) return;
  await productStore.fetchProducts({
    warehouseId,
    userId: userStore.currentUser?.id,
    includePrice: true,
    includeStock: true,
    includeTax: true,
  });
  allProducts.value = productStore.products;
};

const updateFilteredProducts = () => {
  const query = searchQuery.value.trim().toLowerCase();
  filteredProducts.value = query
    ? allProducts.value.filter(p =>
      p.code.toLowerCase().includes(query) ||
      p.name.toLowerCase().includes(query)
    )
    : [];
};

const selectProduct = (product: Product) => {
  if (!product?.id) return;
  const existing = posStore.selectedProducts.find(p => p.productId === product.id);
  const price = product.price?.price ?? 0;

  if (existing) {
    existing.saleQty++;
    existing.subTotal = parseFloat((price * existing.saleQty).toFixed(2));
  } else {
    posStore.selectedProducts.push({
      productId: product.id,
      productName: product.name,
      code: product.code,
      price: parseFloat(price.toFixed(2)),
      saleQty: 1,
      discount: 0,
      tax: 0,
      taxType: product.tax?.taxType ?? TaxType.INCLUSIVE,
      subTotal: parseFloat(price.toFixed(2)),
    });
  }

  searchQuery.value = "";
  filteredProducts.value = [];
};

const handleSearch = () => {
  if (filteredProducts.value.length) selectProduct(filteredProducts.value[0]);
};

const openPaymentSectionModal = async () => {
  if (!posStore.warehouseId) return showPopup("error", "Please select a warehouse", errorMessage);
  if (!posStore.selectedProducts.length) return showPopup("error", "Please select at least one product", errorMessage);

  if (!posStore.temporarySale) await createTemporarySale();

  const modalEl = document.getElementById("paymentSectionModal");
  if (modalEl && window.bootstrap?.Modal) {
    window.bootstrap.Modal.getOrCreateInstance(modalEl).show();
  }
};

const createTemporarySale = async () => {
  if (posStore.warehouseId == null) {
    alert("Please select a warehouse");
    return;
  }

  // Fetch the default currency for the selected warehouse
  const warehouseCurrency = await warehouseCurrencyStore.fetchDefault(
    userStore.currentUser!.companyId,
    posStore.warehouseId
  );

  if (!warehouseCurrency || warehouseCurrency.status !== "ACTIVE") {
    alert("No active default currency found for this warehouse");
    return;
  }

  const posPayload: CreatePosRequest = {
    date: new Date().toISOString().split("T")[0],
    warehouseId: posStore.warehouseId,

    // Currency info from warehouseCurrency
    currencyId: warehouseCurrency.currencyId,
    exchangeRate: 1, // default (you can extend later if multi-currency is needed)

    products: posStore.selectedProducts.map((p) => ({
      productId: p.productId,
      productUnitPrice: Number(p.price),
      saleQty: Number(p.saleQty),
      productDiscount: Number(p.discount),
      productTax: Number(p.tax),
      subTotal: Number(p.subTotal),
    })),

    orderTax: posStore.orderTax,
    discount: posStore.discount,
    shippingCost: posStore.shippingCost,
    status: ShipmentStatus.PENDING,
    note: posStore.note,

    ...(posStore.customerId && { customerId: posStore.customerId }),
  };

  try {
    const res = await posStore.addPos(posPayload, false);
    posStore.setTemporarySale(res);
  } catch (err: any) {
    showPopup(
      "error",
      err?.response?.data?.error || "Failed to initiate payment.",
      errorMessage
    );
  }
};

const openPaymentModal = (method: PaymentMethodKey) => {
  paymentModalKey.value++;
  selectedMethod.value = method;
  handlePaymentModal();
};

const handlePaymentModal = () => {
  const modalEl = document.getElementById("paymentModal");
  if (!(modalEl && window.bootstrap?.Modal)) return;

  // Hide any other open modals
  document.querySelectorAll(".modal.show").forEach(m => {
    if (m.id !== "paymentModal") m.classList.add("modal-hidden");
  });

  const modalInstance = window.bootstrap.Modal.getOrCreateInstance(modalEl);
  modalInstance.show();

  modalEl.addEventListener("hidden.bs.modal", () => {
    selectedMethod.value = null;
    document.querySelectorAll(".modal-hidden").forEach(m => m.classList.remove("modal-hidden"));
  }, { once: true });
};

interface PaymentCompletedEvent {
  method: string;
  amountPaid?: number;
  status: "paid" | "failed" | "pending";
  transactionReference: string;
}

const handlePayment = (eventData: PaymentCompletedEvent) => {
  const modal = document.getElementById("paymentModal");
  modal && window.bootstrap?.Modal.getInstance(modal)?.hide();

  if (eventData.status === "paid" && eventData.amountPaid) {
    remainingAmount.value = Math.max(remainingAmount.value - eventData.amountPaid, 0);
  }

  if (remainingAmount.value === 0) handleConfirmSale();
};

const handleConfirmSale = async () => {
  if (!posStore.temporarySale) return;

  try {
    await posStore.updatePosStatus(posStore.temporarySale.id, SaleStatus.COMPLETED);

    const modal = document.getElementById("paymentSectionModal");
    modal && window.bootstrap?.Modal.getInstance(modal)?.hide();

    showPopup("success", "Sale completed successfully!", successMessage);
    posStore.clearTemporarySale();
  } catch (err: any) {
    showPopup("error", err?.response?.data?.error || "Failed to confirm sale.", errorMessage);
  }
};

// Handle barcode scanned events
const handleBarcodeScanned = (event: CustomEvent<BarcodeScanResponse>) => {
  const scanResponse = event.detail;

  if (scanResponse.success) {
    // Auto-add the scanned product to the POS
    addScannedProduct(scanResponse);

    // Auto-close scanner modal after successful scan
    if (showScannerModal.value) {
      showScannerModal.value = false;
    }
  }
};

const addScannedProduct = (scanResponse: BarcodeScanResponse) => {
  // Use your existing selectProduct function
  // Create a product-like object from the scan response
  const scannedProduct = {
    id: scanResponse.productId,
    name: scanResponse.productName,
    code: scanResponse.productSku,
    price: {
      price: scanResponse.price
    },
    tax: {
      taxType: 'INCLUSIVE' // Default tax type
    }
  };

  // Call your existing selectProduct method
  selectProduct(scannedProduct as any);
};

// Add event listeners
onMounted(() => {
  window.addEventListener("barcode-scanned", (event) => handleBarcodeScanned(event as CustomEvent));
});

onUnmounted(() => {
  window.removeEventListener("barcode-scanned", (event) => handleBarcodeScanned(event as CustomEvent));
});
</script>

<style lang="scss" scoped>
.pos-right {
  width: calc(50% - 10px);

  .grand-total {
    background-color: #1e293b;
    font-size: 16px;
    color: var(--whiteColor);
    font-weight: 600;
    text-align: center;
    padding: 17px 20px;
    border-radius: 4px;
    margin-bottom: 30px;

    span {
      font-size: 20px;
      font-weight: 800;
    }
  }

  .btn-wrapper {
    button {
      width: calc(20% - 10px);
      padding: 14.5px 8px;
      font-size: 14px;
      text-align: center;
    }
  }
}

@media only screen and (max-width: 575px) {
  .pos-right .btn-wrapper button {
    width: 100%;
    margin-bottom: 10px;
  }
}

@media only screen and (max-width: 991px) {
  .pos-right {
    width: 100%;
  }
}

@media only screen and (min-width: 1600px) {
  .pos-right {
    width: calc(50% - 10px);

    .btn-wrapper button {
      width: calc(20% - 20px);
    }
  }
}

.custom-theme-btn {
  color: #fff;
  background: var(--Purple-Gradient,
      linear-gradient(132deg, #4f46e5 4.27%, #6366f1 100%));
  transition: 0.3s;

  &:hover {
    color: #fff;
    background: var(--Purple-Gradient,
        linear-gradient(132deg, #6366f1 4.27%, #4f46e5 100%));
  }
}

.modal-hidden {
  opacity: 0 !important;
  pointer-events: none !important;
  z-index: -1
}

.scanner-section {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 12px;
  background: #f8f9fa;
}

.scanner-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050;
}

.scanner-modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow: auto;
}

.scanner-modal-header {
  padding: 16px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.scanner-modal-body {
  padding: 16px;
}

.scan-notification {
  margin-bottom: 0;
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    transform: translateY(-10px);
    opacity: 0;
  }

  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.scanner-section {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 12px;
  background: #f8f9fa;
}

.scanner-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050;
}

.scanner-modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow: auto;
}

.scanner-modal-header {
  padding: 16px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.scanner-modal-body {
  padding: 16px;
}

.scan-notification {
  margin-bottom: 0;
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    transform: translateY(-10px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>