<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Create Transfer" />
    <ChooseForm :warehouses="warehouses" v-model:fromWarehouseId="fromWarehouseId" v-model:toWarehouseId="toWarehouseId"
      v-model:date="date" @add-product="addProduct" />
    <SelectedProducts v-if="products.length > 0" v-model:products="products" />
    <CreateTransferFrom v-model:orderTax="orderTax" v-model:discount="discount" v-model:shippingCost="shippingCost"
      v-model:note="note" v-model:status="status" :shipmentStatusOptions="shipmentStatusOptions" :products="products"
      @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Product removed from the list
        </span>
      </div>
    </div>
  </div>

  <!-- Success Popup -->
  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">Transfer has been successfully created</span>
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
import {
  ShipmentStatus,
  type CreateTransferRequest,
  type SelectedTransferProduct
} from "@/types/Transfer";
import type { Warehouse } from "@/types/Warehouse";

import { useTransferStore } from "@/stores/transferStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { calculateSubTotal } from "@/utils/transferUtils";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import ChooseForm from "../../components/Transfer/CreateTransfer/ChooseForm.vue";
import SelectedProducts from "../../components/Transfer/CreateTransfer/SelectedProducts.vue";
import CreateTransferFrom from "../../components/Transfer/CreateTransfer/CreateTransferFrom.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

// Reactive form fields
const fromWarehouseId = ref<number | null>(null);
const toWarehouseId = ref<number | null>(null);
const date = ref<string>(new Date().toISOString().split("T")[0]);
const note = ref("");
const orderTax = ref("0");
const discount = ref("0");
const shippingCost = ref("0");
const status = ref<ShipmentStatus>(ShipmentStatus.PENDING);
const products = ref<SelectedTransferProduct[]>([]);
const warehouses = ref<Warehouse[]>([]);
const errorMessage = ref("Something went wrong. Please try again.");

const shipmentStatusOptions = [
  { label: "Pending", value: ShipmentStatus.PENDING },
  { label: "Processing", value: ShipmentStatus.PROCESSING },
  { label: "Shipped", value: ShipmentStatus.SHIPPED },
  { label: "In Transit", value: ShipmentStatus.IN_TRANSIT },
  { label: "Delivered", value: ShipmentStatus.DELIVERED },
  { label: "Returned", value: ShipmentStatus.RETURNED }
];

const transferStore = useTransferStore();
const warehouseStore = useWarehouseStore();

onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
});

const addProduct = (product: SelectedTransferProduct) => {
  if (!products.value.some(p => p.productId === product.productId)) {
    const productWithSubtotal = {
      ...product,
      transferredQty: product.transferredQty ?? 1,
      subTotal: calculateSubTotal(product),
    };
    products.value.push(productWithSubtotal);
  }
};

const handleSubmit = async () => {
  if (!fromWarehouseId.value || !toWarehouseId.value || products.value.length === 0) {
    errorMessage.value = !fromWarehouseId.value
      ? "Please select a source warehouse."
      : !toWarehouseId.value
        ? "Please select a destination warehouse."
        : "Please add at least one product.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(), 3000);
    return;
  }

  const payload: CreateTransferRequest = {
    fromWarehouse: fromWarehouseId.value!,
    toWarehouse: toWarehouseId.value!,
    date: date.value,
    orderTax: orderTax.value,
    discount: discount.value,
    shippingCost: shippingCost.value,
    status: status.value,
    note: note.value,
    products: products.value.map(p => ({
      productId: p.productId,
      productCode: p.code,
      productUnitCost: p.cost,
      productStock: p.stock,
      transferredQty: p.transferredQty,
      productDiscount: p.discount,
      productTax: p.tax,
      subTotal: p.subTotal
    }))
  };

  console.log("Transfer payload:", JSON.stringify(payload, null, 2));

  try {
    await transferStore.addTransfer(payload);
    products.value = [];
    note.value = "";
    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!).hide(), 3000);
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.error ?? error?.response?.data?.message ?? "Failed to create transfer.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(), 3000);
  }
};
</script>