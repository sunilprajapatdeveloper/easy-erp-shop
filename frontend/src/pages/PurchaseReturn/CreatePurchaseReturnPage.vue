<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Create Purchase Return" />

    <!-- Form -->
    <ChooseForm :warehouses="warehouses" :suppliers="suppliers" v-model:warehouseId="warehouseId"
      v-model:supplierId="supplierId" v-model:date="date" v-model:originalPurchaseId="originalPurchaseId"
      :isCreateMode="true" @add-product="addProduct" @load-purchase="loadPurchase" />

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
        <span class="text-white fw-medium">Purchase return has been successfully created</span>
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
import type { Supplier } from "@/types/Supplier";
import type { Warehouse } from "@/types/Warehouse";
import type { Purchase, PurchaseProduct } from "@/types/Purchase";
import type { SelectedPurchaseReturnProduct } from "@/types/PurchaseReturn";

import { useWarehouseStore } from "@/stores/warehouseStore";
import { useSupplierStore } from "@/stores/supplierStore";
import { usePurchaseReturnStore } from "@/stores/purchaseReturnStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/PurchaseReturn/CreatePurchaseReturn/ChooseForm.vue";
import SelectedProducts from "@/components/PurchaseReturn/CreatePurchaseReturn/SelectedProducts.vue";
import SubmitPurchase from "@/components/PurchaseReturn/CreatePurchaseReturn/SubmitPurchase.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

import { PurchaseStatus } from "@/enums/purchaseStatus";
import { ShipmentStatus } from "@/enums/shipmentStatus";

// Stores
const warehouseStore = useWarehouseStore();
const supplierStore = useSupplierStore();
const purchaseReturnStore = usePurchaseReturnStore();

// Data
const warehouses = ref<Warehouse[]>([]);
const suppliers = ref<Supplier[]>([]);
const date = ref<string>(new Date().toISOString().split("T")[0]);
const warehouseId = ref<number | null>(null);
const supplierId = ref<number | null>(null);
const originalPurchaseId = ref<number | null>(null);
const products = ref<SelectedPurchaseReturnProduct[]>([]);

// Return-specific fields
const returnTax = ref("0");
const returnDiscount = ref("0");
const shippingCost = ref("0");
const returnStatus = ref<PurchaseStatus>(PurchaseStatus.PENDING);
const shipmentStatus = ref<ShipmentStatus>(ShipmentStatus.PENDING);
const note = ref("");
const errorMessage = ref("Something went wrong. Please try again.");

// Fetch data
onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  suppliers.value = await supplierStore.fetchSuppliers();
});

// Load Original Purchase into Form
const loadPurchase = (purchase: Purchase) => {
  originalPurchaseId.value = purchase.id;
  date.value = new Date().toISOString().split("T")[0]; // return date is today
  warehouseId.value = purchase.warehouseId;
  supplierId.value = purchase.supplierId;

  // Load extra fields from original purchase
  returnTax.value = purchase.orderTax ?? "0";
  returnDiscount.value = purchase.discount ?? "0";
  shippingCost.value = purchase.shippingCost ?? "0";

  // convert purchase.products into SelectedPurchaseReturnProduct[]
  products.value = purchase.products.map((p: PurchaseProduct) => {
    const cost = parseFloat(p.productUnitCost ?? "0");
    const discount = parseFloat(p.productDiscount ?? "0");
    const tax = parseFloat(p.productTax ?? "0");

    const subTotal = ((cost - discount + tax) * p.purchaseQty).toFixed(2);

    return {
      productId: p.productId,
      productName: p.productName,
      code: p.productCode,
      unitCost: p.productUnitCost,
      discount: p.productDiscount,
      tax: p.productTax,
      stock: p.purchaseQty,
      subTotal,
      returnQty: p.purchaseQty,
      taxType: "EXCLUSIVE",
    } as SelectedPurchaseReturnProduct;
  });
};

// Add product manually
const addProduct = (product: SelectedPurchaseReturnProduct) => {
  const existing = products.value.find(p => p.productId === product.productId);

  if (existing) {
    existing.returnQty += product.returnQty;
    const price = parseFloat(existing.unitCost ?? "0");
    const discount = parseFloat(existing.discount ?? "0");
    const tax = parseFloat(existing.tax ?? "0");

    existing.subTotal = ((price - discount + tax) * existing.returnQty).toFixed(2);
  } else {
    const qty = product.returnQty ?? 1;
    const price = parseFloat(product.unitCost ?? "0");
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
  if (!date.value || !warehouseId.value || !supplierId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : !supplierId.value
        ? "Please select a supplier."
        : "Please add at least one product.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(), 3000);
    return;
  }

  const payload = {
    date: date.value,
    warehouseId: warehouseId.value,
    supplierId: supplierId.value,
    originalPurchaseId: originalPurchaseId.value ?? 0,
    products: products.value.map(p => ({
      productId: p.productId,
      productUnitCost: p.unitCost,
      returnQty: p.returnQty,
      productDiscount: p.discount,
      productTax: p.tax,
      subTotal: p.subTotal,
    })),
    orderTax: returnTax.value,
    discount: returnDiscount.value,
    shippingCost: shippingCost.value,
    totalAmount: products.value.reduce((sum, p) => sum + parseFloat(p.subTotal ?? "0"), 0).toFixed(2),
    returnStatus: returnStatus.value,
    shipmentStatus: shipmentStatus.value,
    note: note.value,
  };

  try {
    await purchaseReturnStore.addPurchaseReturn(payload);

    resetForm();
    products.value = [];
    note.value = "";

    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!).hide(), 3000);
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message ?? "Failed to create purchase return.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(), 3000);
  }
};

// Reset form
const resetForm = () => {
  date.value = new Date().toISOString().split("T")[0];
  warehouseId.value = null;
  supplierId.value = null;
  originalPurchaseId.value = null;
  products.value = [];

  returnTax.value = "0";
  returnDiscount.value = "0";
  shippingCost.value = "0";
  returnStatus.value = PurchaseStatus.PENDING;
  shipmentStatus.value = ShipmentStatus.PENDING;
  note.value = "";
};
</script>
