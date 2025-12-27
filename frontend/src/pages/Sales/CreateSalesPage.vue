<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Create Sales" />

    <ChooseForm :warehouses="warehouses" :customers="customers" v-model:warehouseId="warehouseId"
      v-model:customerId="customerId" v-model:date="date" @add-product="addProduct" />

    <SelectedProducts v-if="products.length > 0" v-model:products="products" />

    <SubmitPurchase v-model:orderTax="orderTax" v-model:discount="discount" v-model:shippingCost="shippingCost"
      v-model:saleStatus="saleStatusValue" v-model:paymentStatus="paymentStatus" v-model:paidAmount="paidAmount"
      v-model:paymentMethod="paymentMethod" :products="products" v-model:note="note" @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- Success Popup -->
  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">Sale has been successfully created</span>
      </div>
    </div>
  </div>
  <a id="triggerSuccessPopup" class="d-none" data-bs-toggle="offcanvas" href="#successPopup" role="button"></a>

  <!-- Error Popup -->
  <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
    <div class="offcanvas-body p-0">
      <div class="create-error">
        <img src="../../assets/img/icons/close-circle-2.svg" alt="Image" style="filter: brightness(0) invert(1);" />
        <span class="text-white fw-medium">{{ errorMessage }}</span>
      </div>
    </div>
  </div>
  <a id="triggerErrorPopup" class="d-none" data-bs-toggle="offcanvas" href="#errorPopup" role="button"></a>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import { Offcanvas } from "bootstrap";
import type { SelectedSaleProduct, CreateSaleRequest } from "@/types/Sale";
import type { CreatePaymentRequest } from "@/types/Payment";
import type { Customer } from "@/types/Customer";
import type { WarehouseListItem } from "@/types/Warehouse";

import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCustomerStore } from "@/stores/customerStore";
import { useSaleStore } from "@/stores/saleStore";
import { usePOSSettingsStore } from "@/stores/posSettingsStore";
import { useWarehouseCurrencyStore } from "@/stores/warehouseCurrencyStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/Sales/CreateSales/ChooseForm.vue";
import SelectedProducts from "@/components/Sales/CreateSales/SelectedProducts.vue";
import SubmitPurchase from "@/components/Sales/CreateSales/SubmitPurchase.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";
import { SaleStatus } from "@/enums/saleStatus";
import { PaymentStatus } from "@/enums/paymentStatus";
import { ShipmentStatus } from "@/enums/shipmentStatus";

const warehouseStore = useWarehouseStore();
const customerStore = useCustomerStore();
const saleStore = useSaleStore();
const posSettingsStore = usePOSSettingsStore();
const warehouseCurrencyStore = useWarehouseCurrencyStore();

// UI state
const warehouses = ref<WarehouseListItem[]>([]);
const customers = ref<Customer[]>([]);
const products = ref<SelectedSaleProduct[]>([]);

const date = ref<string>(new Date().toISOString().split("T")[0]);
const warehouseId = ref<number | null>(null);
const customerId = ref<number | null>(null);

const orderTax = ref("0");
const discount = ref("0");
const shippingCost = ref("0");
const saleStatusValue = ref<SaleStatus>(SaleStatus.PENDING);
const shipmentStatusValue = ref<ShipmentStatus>(ShipmentStatus.PENDING);
const note = ref("");
const errorMessage = ref("Something went wrong. Please try again.");

const paymentStatus = ref<PaymentStatus>(PaymentStatus.PENDING);
const paidAmount = ref("0");
const paymentMethod = ref("CASH");

// Currency state (from POS)
const currencyId = ref<number>(1); // fallback
const exchangeRate = ref<string>("1.0");

onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  customers.value = await customerStore.fetchCustomers();
});

watch(warehouseId, async (newWarehouseId) => {
  if (!newWarehouseId) return;

  try {
    // Load POS settings for this warehouse
    const posSettings = await posSettingsStore.loadPOSSettings(newWarehouseId);

    // Set default customer
    if (posSettings.defaultCustomerId) customerId.value = posSettings.defaultCustomerId;

    // Set currency from POS settings
    if (posSettings.defaultCurrencyId) {
      currencyId.value = posSettings.defaultCurrencyId;

      // Fetch exchange rate using POS currencyId
      await warehouseCurrencyStore.fetchOne(currencyId.value, newWarehouseId);

      // Safely access current.value
      const currentCurrency = warehouseCurrencyStore.current.value;
      if (currentCurrency) {
        exchangeRate.value = currentCurrency.exchangeRate ?? "1.0";
      } else {
        exchangeRate.value = "1.0"; // fallback
      }
    } else {
      currencyId.value = 1;
      exchangeRate.value = "1.0";
    }
  } catch (err) {
    console.error("Failed to load POS currency or exchange rate", err);
    currencyId.value = 1;
    exchangeRate.value = "1.0";
  }
});

// -------------------
// Add product to sale
// -------------------
const addProduct = (product: SelectedSaleProduct) => {
  const existing = products.value.find(p => p.productId === product.productId);

  if (existing) {
    existing.saleQty = (existing.saleQty ?? 1) + (product.saleQty ?? 1);
    const price = parseFloat(existing.price ?? "0");
    const discount = parseFloat(existing.discount ?? "0");
    const tax = parseFloat(existing.tax ?? "0");
    existing.subTotal = ((price - discount + tax) * existing.saleQty).toFixed(2);
  } else {
    const qty = product.saleQty ?? 1;
    const price = parseFloat(product.price ?? "0");
    const discount = parseFloat(product.discount ?? "0");
    const tax = parseFloat(product.tax ?? "0");
    const subTotal = ((price - discount + tax) * qty).toFixed(2);
    products.value.push({ ...product, saleQty: qty, subTotal });
  }
};

// -------------------
// Submit sale
// -------------------
const handleSubmit = async (payment?: CreatePaymentRequest) => {
  if (!date.value || !warehouseId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : "Please add at least one product.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(
      () => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(),
      3000
    );
    return;
  }

  const payload: CreateSaleRequest = {
    date: date.value,
    warehouseId: warehouseId.value,
    customerId: customerId.value ?? null,
    products: products.value.map(p => ({
      productId: p.productId,
      productUnitPrice: p.price,
      saleQty: p.saleQty,
      productDiscount: p.discount,
      productTax: p.tax,
    })),
    orderTax: orderTax.value,
    discount: discount.value,
    shippingCost: shippingCost.value,
    saleStatus: saleStatusValue.value,
    shipmentStatus: shipmentStatusValue.value,
    note: note.value,
    currencyId: currencyId.value,       // ✅ from POS settings
    exchangeRate: exchangeRate.value,   // ✅ loaded from warehouse currency store
    payments: payment ? [payment] : [],
    source: "WEB",
  };

  try {
    await saleStore.addSale(payload);
    resetForm();
    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(
      () => Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!).hide(),
      3000
    );
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message ?? "Failed to create sale.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(
      () => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(),
      3000
    );
  }
};

// -------------------
// Reset form
// -------------------
const resetForm = () => {
  date.value = new Date().toISOString().split("T")[0];
  warehouseId.value = null;
  customerId.value = null;
  products.value = [];
  orderTax.value = "0";
  discount.value = "0";
  shippingCost.value = "0";
  saleStatusValue.value = SaleStatus.PENDING;
  shipmentStatusValue.value = ShipmentStatus.PENDING;
  note.value = "";
  paymentStatus.value = PaymentStatus.PENDING;
  paidAmount.value = "0";
  paymentMethod.value = "CASH";

  currencyId.value = 1;
  exchangeRate.value = "1.0";
};
</script>