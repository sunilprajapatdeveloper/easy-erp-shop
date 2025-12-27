<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Create Sales Return" />

    <!-- Form -->
    <ChooseForm :warehouses="warehouses" :customers="customers" v-model:warehouseId="warehouseId"
      v-model:customerId="customerId" v-model:date="date" v-model:originalSaleId="originalSaleId" :isCreateMode="true"
      @add-product="addProduct" @load-sale="loadSale" />

    <!-- Selected Products -->
    <SelectedProducts v-if="products.length > 0" v-model:products="products" />

    <!-- Submit Section -->
    <SubmitPurchase v-model:returnTax="returnTax" v-model:returnDiscount="returnDiscount"
      v-model:shippingCost="shippingCost" v-model:returnStatus="returnStatus" v-model:shipmentStatus="shipmentStatus"
      :products="products" v-model:note="note" @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- Success Popup -->
  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">Sale return has been successfully created</span>
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
import { ref, onMounted } from "vue";
import { Offcanvas } from "bootstrap";
import type { Customer } from "@/types/Customer";
import type { Warehouse } from "@/types/Warehouse";
import type { Sale, SaleProduct } from "@/types/Sale";
import type { SelectedSaleReturnProduct } from "@/types/saleReturn";

import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCustomerStore } from "@/stores/customerStore";
import { useSaleReturnStore } from "@/stores/saleReturnStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/SalesReturn/CreateSalesReturn/ChooseForm.vue";
import SelectedProducts from "@/components/SalesReturn/CreateSalesReturn/SelectedProducts.vue";
import SubmitPurchase from "@/components/SalesReturn/CreateSalesReturn/SubmitPurchase.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

import { SaleStatus } from "@/enums/saleStatus";
import { ShipmentStatus } from "@/enums/shipmentStatus";

// Stores
const warehouseStore = useWarehouseStore();
const customerStore = useCustomerStore();
const saleReturnStore = useSaleReturnStore();

// Data
const warehouses = ref<Warehouse[]>([]);
const customers = ref<Customer[]>([]);
const date = ref<string>(new Date().toISOString().split("T")[0]);
const warehouseId = ref<number | null>(null);
const customerId = ref<number | null>(null);
const originalSaleId = ref<number | null>(null);
const products = ref<SelectedSaleReturnProduct[]>([]);

// Return-specific fields
const returnTax = ref("0");
const returnDiscount = ref("0");
const shippingCost = ref("0");
const returnStatus = ref<SaleStatus>(SaleStatus.PENDING);
const shipmentStatus = ref<ShipmentStatus>(ShipmentStatus.PENDING);
const note = ref("");
const errorMessage = ref("Something went wrong. Please try again.");

// Fetch data
onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  customers.value = await customerStore.fetchCustomers();
});

// Load Original Sale into Form
const loadSale = (sale: Sale) => {
  originalSaleId.value = sale.id;
  date.value = new Date().toISOString().split("T")[0]; // return date is today
  warehouseId.value = sale.warehouseId;
  customerId.value = sale.customerId;

  // Load extra fields from original sale
  returnTax.value = sale.orderTax ?? "0";
  returnDiscount.value = sale.discount ?? "0";
  shippingCost.value = sale.shippingCost ?? "0";

  // convert sale.products into SelectedSaleReturnProduct[]
  products.value = sale.products.map((p: SaleProduct) => {
    const price = parseFloat(p.productUnitPrice ?? "0");
    const discount = parseFloat(p.productDiscount ?? "0");
    const tax = parseFloat(p.productTax ?? "0");

    const subTotal = ((price - discount + tax) * p.saleQty).toFixed(2);

    return {
      productId: p.productId,
      productName: p.productName,
      code: p.productCode,
      price: p.productUnitPrice,
      discount: p.productDiscount,
      tax: p.productTax,
      stock: p.saleQty, // stock here = originally sold quantity
      subTotal,
      returnQty: p.saleQty, // default full return (user can adjust later)
      taxType: "EXCLUSIVE", // default, update if needed
    } as SelectedSaleReturnProduct;
  });
};

// Add product manually
const addProduct = (product: SelectedSaleReturnProduct) => {
  const existing = products.value.find(p => p.productId === product.productId);

  if (existing) {
    existing.returnQty += product.returnQty;
    const price = parseFloat(existing.price ?? "0");
    const discount = parseFloat(existing.discount ?? "0");
    const tax = parseFloat(existing.tax ?? "0");

    existing.subTotal = ((price - discount + tax) * existing.returnQty).toFixed(2);
  } else {
    const qty = product.returnQty ?? 1;
    const price = parseFloat(product.price ?? "0");
    const discount = parseFloat(product.discount ?? "0");
    const tax = parseFloat(product.tax ?? "0");

    const subTotal = ((price - discount + tax) * qty).toFixed(2);

    products.value.push({
      ...product,
      returnQty: qty,
      subTotal,
    });
  }
};

// Submit
const handleSubmit = async () => {
  if (!date.value || !warehouseId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : "Please add at least one product.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(), 3000);
    return;
  }

  const payload = {
    date: date.value,
    warehouseId: warehouseId.value,
    customerId: customerId.value,
    originalSaleId: originalSaleId.value ?? 0,
    products: products.value.map(p => ({
      productId: p.productId,
      productUnitPrice: p.price,
      returnQty: p.returnQty,
      returnDiscount: p.discount,
      returnTax: p.tax,
    })),
    returnTax: returnTax.value,
    returnDiscount: returnDiscount.value,
    shippingCost: shippingCost.value,
    returnStatus: returnStatus.value,
    shipmentStatus: shipmentStatus.value,
    note: note.value,
  };

  try {
    await saleReturnStore.addSaleReturn(payload);

    resetForm();
    products.value = [];
    note.value = "";

    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!).hide(), 3000);
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message ?? "Failed to create sale return.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(), 3000);
  }
};

// Reset form
const resetForm = () => {
  date.value = new Date().toISOString().split("T")[0];
  warehouseId.value = null;
  customerId.value = null;
  originalSaleId.value = null;
  products.value = [];

  returnTax.value = "0";
  returnDiscount.value = "0";
  shippingCost.value = "0";
  returnStatus.value = SaleStatus.PENDING;
  shipmentStatus.value = ShipmentStatus.PENDING;
  note.value = "";
};
</script>
