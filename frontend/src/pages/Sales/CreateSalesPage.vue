<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Create Sales" />

    <ChooseForm :warehouses="warehouses" :customers="customers" v-model:warehouseId="warehouseId"
      v-model:customerId="customerId" v-model:date="date" @add-product="addProduct" />

    <SelectedProducts v-if="products.length > 0" v-model:products="products" />

    <SubmitSale v-model:manualDiscountValue="manualDiscountValue" v-model:manualDiscountType="manualDiscountType"
      v-model:manualDiscountReason="manualDiscountReason" v-model:appliedDiscountId="appliedDiscountId"
      v-model:couponCode="couponCode" v-model:shippingCost="shippingCost" v-model:paidAmount="paidAmount"
      v-model:paymentStatus="paymentStatus" v-model:saleStatus="saleStatusValue" v-model:note="note"
      :products="products" :availableDiscounts="availableDiscounts" @submit="handleSubmit" />

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
import type { Customer } from "@/types/Customer";
import type { WarehouseListItem } from "@/types/Warehouse";
import { DiscountType } from "@/enums/discountType";
import { SaleStatus } from "@/enums/saleStatus";
import { PaymentStatus } from "@/enums/paymentStatus";
import { ShipmentStatus } from "@/enums/shipmentStatus";
import { SaleSource } from "@/enums/SaleSource";
import { calculateSaleLine } from "@/utils/saleCalculations";

import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCustomerStore } from "@/stores/customerStore";
import { useSaleStore } from "@/stores/saleStore";
import { usePOSSettingsStore } from "@/stores/posSettingsStore";
import { useWarehouseCurrencyStore } from "@/stores/warehouseCurrencyStore";
import { useDiscountStore } from "@/stores/discountStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/Sales/CreateSales/ChooseForm.vue";
import SelectedProducts from "@/components/Sales/CreateSales/SelectedProducts.vue";
import SubmitSale from "@/components/Sales/CreateSales/SubmitSale.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";
import { DiscountItem } from "@/types/Discount";

const warehouseStore = useWarehouseStore();
const customerStore = useCustomerStore();
const saleStore = useSaleStore();
const posSettingsStore = usePOSSettingsStore();
const warehouseCurrencyStore = useWarehouseCurrencyStore();
const discountStore = useDiscountStore();

// UI state
const warehouses = ref<WarehouseListItem[]>([]);
const customers = ref<Customer[]>([]);
const products = ref<SelectedSaleProduct[]>([]);
const availableDiscounts = ref<DiscountItem[]>([]);

const date = ref<string>(new Date().toISOString().split("T")[0]);
const warehouseId = ref<number | null>(null);
const customerId = ref<number | null>(null);

// New discount fields
const manualDiscountValue = ref("0");
const manualDiscountType = ref<DiscountType | null>(null);
const manualDiscountReason = ref("");
const appliedDiscountId = ref<number | null>(null);
const couponCode = ref("");

const shippingCost = ref("0");
const paidAmount = ref("0");
const paymentStatus = ref<PaymentStatus>(PaymentStatus.PENDING);
const saleStatusValue = ref<SaleStatus>(SaleStatus.PENDING);
const note = ref("");
const errorMessage = ref("Something went wrong. Please try again.");

// Currency state
const currencyId = ref<number>(1);
const exchangeRate = ref<string>("1.0");

onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  customers.value = await customerStore.fetchCustomers();
});

watch(warehouseId, async (newWarehouseId) => {
  if (!newWarehouseId) return;

  try {
    const posSettings = await posSettingsStore.loadPOSSettings(newWarehouseId);
    if (posSettings.defaultCustomerId) customerId.value = posSettings.defaultCustomerId;

    if (posSettings.defaultCurrencyId) {
      currencyId.value = posSettings.defaultCurrencyId;
      await warehouseCurrencyStore.fetchOne(currencyId.value, newWarehouseId);

      const currentCurrency = warehouseCurrencyStore.current;
      if (currentCurrency) {
        exchangeRate.value = (currentCurrency as any)?.exchangeRate ?? "1.0";
      } else {
        exchangeRate.value = "1.0";
      }

      exchangeRate.value = (currentCurrency as any)?.exchangeRate ?? "1.0";
    } else {
      currencyId.value = 1;
      exchangeRate.value = "1.0";
    }

    // Fetch available system discounts for this warehouse
    availableDiscounts.value = await discountStore.fetchActiveOrderDiscounts(newWarehouseId);
  } catch (err) {
    console.error("Failed to load POS settings", err);
    currencyId.value = 1;
    exchangeRate.value = "1.0";
  }
});

// Add product – compute preview locally, but only raw data is sent later
const addProduct = (product: SelectedSaleProduct) => {
  const existing = products.value.find((p) => p.productId === product.productId);
  if (existing) {
    existing.quantity += 1;
    const calc = calculateSaleLine(existing);
    existing.lineNetAmount = calc.lineNetAmount;
    existing.lineTaxAmount = calc.lineTaxAmount;
    existing.lineGrossAmount = calc.lineGrossAmount;
    return;
  }

  const calc = calculateSaleLine(product);
  product.lineNetAmount = calc.lineNetAmount;
  product.lineTaxAmount = calc.lineTaxAmount;
  product.lineGrossAmount = calc.lineGrossAmount;
  products.value.push(product);
};

// Submit – build request with ONLY user‑entered data
const handleSubmit = async () => {
  if (!date.value || !warehouseId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : "Please add at least one product.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(), 3000);
    return;
  }

  const payload: CreateSaleRequest = {
    date: date.value,
    warehouseId: warehouseId.value,
    customerId: customerId.value ?? null,
    products: products.value.map((p) => ({
      productId: p.productId,
      quantity: p.quantity,
      // Send unitPriceOverride only if the user explicitly changed the price
      unitPriceOverride: undefined, // or p.productUnitPrice if overridden
    })),
    currencyId: currencyId.value,
    exchangeRate: parseFloat(exchangeRate.value || "1"),
    // Manual discount
    manualDiscountValue: parseFloat(manualDiscountValue.value || "0") || undefined,
    manualDiscountType: manualDiscountType.value ?? undefined,
    manualDiscountReason: manualDiscountReason.value || undefined,
    // System discount
    appliedDiscountId: appliedDiscountId.value ?? undefined,
    // Coupon
    couponCode: couponCode.value || undefined,
    shippingCost: parseFloat(shippingCost.value || "0"),
    roundingAmount: 0,
    paidAmountTxnCurrency: parseFloat(paidAmount.value || "0"),
    shipmentStatus: ShipmentStatus.PENDING,
    saleStatus: saleStatusValue.value,
    paymentStatus: paymentStatus.value,
    source: SaleSource.WEB,
    note: note.value || undefined,
  };

  try {
    await saleStore.addSale(payload);
    resetForm();
    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!).hide(), 3000);
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message ?? "Failed to create sale.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(), 3000);
  }
};

const resetForm = () => {
  date.value = new Date().toISOString().split("T")[0];
  warehouseId.value = null;
  customerId.value = null;
  products.value = [];
  manualDiscountValue.value = "0";
  manualDiscountType.value = null;
  manualDiscountReason.value = "";
  appliedDiscountId.value = null;
  couponCode.value = "";
  shippingCost.value = "0";
  paidAmount.value = "0";
  paymentStatus.value = PaymentStatus.PENDING;
  saleStatusValue.value = SaleStatus.PENDING;
  note.value = "";
  currencyId.value = 1;
  exchangeRate.value = "1.0";
};
</script>