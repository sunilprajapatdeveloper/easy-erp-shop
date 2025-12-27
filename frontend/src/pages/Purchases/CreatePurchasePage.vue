<template>
  <MainHeader />
  <MainSidebar />

  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Create Purchase" />

    <ChooseForm :warehouses="warehouses" :suppliers="suppliers" v-model:warehouseId="warehouseId"
      v-model:supplierId="supplierId" v-model:date="date" @add-product="addProduct" />

    <SelectedProducts v-if="products.length > 0" v-model:products="products" />

    <SubmitPurchase v-model:orderTax="orderTax" v-model:discount="discount" v-model:shippingCost="shippingCost"
      v-model:purchaseStatus="purchaseStatusValue" v-model:shippingStatus="shippingStatusValue" :products="products"
      v-model:note="note" @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- Success Popup -->
  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">Purchase has been successfully created</span>
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
import type { SelectedPurchaseProduct, CreatePurchaseRequest } from "@/types/Purchase";
import type { Warehouse } from "@/types/Warehouse";
import type { Supplier } from "@/types/Supplier";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useSupplierStore } from "@/stores/supplierStore";
import { usePurchaseStore } from "@/stores/purchaseStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/Purchases/CreatePurchase/ChooseForm.vue";
import SelectedProducts from "@/components/Purchases/CreatePurchase/SelectedProducts.vue";
import SubmitPurchase from "@/components/Purchases/CreatePurchase/SubmitPurchase.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

import { PurchaseStatus } from "@/enums/purchaseStatus";
import { ShipmentStatus } from "@/enums/shipmentStatus";

const warehouseStore = useWarehouseStore();
const supplierStore = useSupplierStore();
const purchaseStore = usePurchaseStore();

const warehouses = ref<Warehouse[]>([]);
const suppliers = ref<Supplier[]>([]);

const date = ref<string>(new Date().toISOString().split("T")[0]);
const warehouseId = ref<number | null>(null);
const supplierId = ref<number | null>(null);
const products = ref<SelectedPurchaseProduct[]>([]);

const orderTax = ref("0");
const discount = ref("0");
const shippingCost = ref("0");
const note = ref("");

const purchaseStatusValue = ref<PurchaseStatus>(PurchaseStatus.PENDING);
const shippingStatusValue = ref<ShipmentStatus>(ShipmentStatus.PENDING);

const errorMessage = ref("Something went wrong. Please try again.");

onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  suppliers.value = await supplierStore.fetchSuppliers();
});

const addProduct = (product: SelectedPurchaseProduct) => {
  const existing = products.value.find(p => p.productId === product.productId);

  if (existing) {
    existing.purchaseQty = (existing.purchaseQty ?? 1) + (product.purchaseQty ?? 1);

    const cost = parseFloat(existing.cost ?? "0");
    const discount = parseFloat(existing.discount ?? "0");
    const tax = parseFloat(existing.tax ?? "0");

    existing.subTotal = ((cost - discount + tax) * existing.purchaseQty).toFixed(2);
  } else {
    const qty = product.purchaseQty ?? 1;
    const cost = parseFloat(product.cost ?? "0");
    const discount = parseFloat(product.discount ?? "0");
    const tax = parseFloat(product.tax ?? "0");

    const subTotal = ((cost - discount + tax) * qty).toFixed(2);

    products.value.push({
      ...product,
      purchaseQty: qty,
      subTotal,
    });
  }
};

const handleSubmit = async () => {
  if (!date.value || !warehouseId.value || !supplierId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : !supplierId.value
        ? "Please select a supplier."
        : "Please add at least one product.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(
      () => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(),
      3000
    );
    return;
  }

  const payload: CreatePurchaseRequest = {
    date: date.value,
    warehouseId: warehouseId.value!,
    supplierId: supplierId.value!,
    products: products.value.map(p => ({
      productId: p.productId,
      productUnitCost: p.cost,
      purchaseQty: p.purchaseQty,
      productDiscount: p.discount,
      productTax: p.tax,
      subTotal: p.subTotal,
    })),
    orderTax: orderTax.value,
    discount: discount.value,
    shippingCost: shippingCost.value,
    totalAmount: products.value.reduce(
      (sum, p) => sum + parseFloat(p.subTotal ?? "0"),
      0
    ).toFixed(2),
    purchaseStatus: purchaseStatusValue.value,
    shippingStatus: shippingStatusValue.value,
    note: note.value,
  };

  try {
    await purchaseStore.addPurchase(payload);

    resetForm();
    products.value = [];
    note.value = "";

    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(
      () => Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!).hide(),
      3000
    );
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message ?? "Failed to create purchase.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(
      () => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(),
      3000
    );
  }
};

const resetForm = () => {
  date.value = new Date().toISOString().split("T")[0];
  warehouseId.value = null;
  supplierId.value = null;
  products.value = [];

  orderTax.value = "0";
  discount.value = "0";
  shippingCost.value = "0";
  note.value = "";

  purchaseStatusValue.value = PurchaseStatus.PENDING;
  shippingStatusValue.value = ShipmentStatus.PENDING;
};
</script>
