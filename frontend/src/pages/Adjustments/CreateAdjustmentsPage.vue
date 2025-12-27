<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Create Adjustment" />
    <ChooseForm :warehouses="warehouses" v-model:warehouseId="warehouseId" v-model:date="date"
      @add-product="addProduct" />
    <SelectedProducts v-if="products.length > 0" v-model:products="products" />
    <NotesContent v-model:note="note" @submit="handleSubmit" />

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

  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Adjustment has been successfully created
        </span>
      </div>
    </div>
  </div>

  <a id="triggerSuccessPopup" class="d-none" data-bs-toggle="offcanvas" href="#successPopup" role="button"
    aria-controls="successPopup">
  </a>

  <!-- Error Popup -->
  <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
    <div class="offcanvas-body p-0">
      <div class="create-error">
        <img src="../../assets/img/icons/close-circle-2.svg" alt="Image" style="filter: brightness(0) invert(1);" />
        <span class="text-white fw-medium">
          {{ errorMessage }}
        </span>
      </div>
    </div>
  </div>

  <a id="triggerErrorPopup" class="d-none" data-bs-toggle="offcanvas" href="#errorPopup" role="button"
    aria-controls="errorPopup">
  </a>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useAdjustmentStore } from "@/stores/adjustmentStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import type { CreateAdjustmentRequest, SelectedProduct } from "@/types/Adjustment";
import type { Warehouse } from "@/types/Warehouse";
import { Offcanvas } from 'bootstrap';

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import ChooseForm from "../../components/Adjustments/CreateAdjustments/ChooseForm.vue";
import SelectedProducts from "../../components/Adjustments/CreateAdjustments/SelectedProducts.vue";
import NotesContent from "../../components/Adjustments/CreateAdjustments/NotesContent.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

const warehouseId = ref<number | null>(null);
const date = ref<string>(new Date().toISOString().split("T")[0]);
const note = ref("");
const products = ref<SelectedProduct[]>([]);

const adjustmentStore = useAdjustmentStore();
const warehouseStore = useWarehouseStore();
const warehouses = ref<Warehouse[]>([]);

onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
});

const addProduct = (product: SelectedProduct) => {
  if (!products.value.some(p => p.productId === product.productId)) {
    products.value.push(product);
  }
};

const errorMessage = ref("Something went wrong. Please try again.");

const handleSubmit = async () => {
  if (!warehouseId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse before submitting."
      : "Please add at least one product to continue.";

    const trigger = document.getElementById("triggerErrorPopup") as HTMLAnchorElement;
    trigger?.click();

    setTimeout(() => {
      const errorPopupEl = document.getElementById("errorPopup");
      if (errorPopupEl) {
        const instance = Offcanvas.getOrCreateInstance(errorPopupEl);
        instance.hide();
      }
    }, 3000);

    return;
  }

  const payload: CreateAdjustmentRequest = {
    warehouseId: warehouseId.value,
    date: date.value,
    note: note.value,
    products: products.value.map(p => ({
      productId: p.productId,
      adjustedQty: p.adjustedQty,
      stockEffect: p.stockEffect,
    })),
  };

  try {
    await adjustmentStore.addAdjustment(payload);

    // Reset form
    products.value = [];
    note.value = "";

    const trigger = document.getElementById("triggerSuccessPopup") as HTMLAnchorElement;
    trigger?.click();

    // Auto-hide after 3s without @ts-ignore
    setTimeout(() => {
      const successPopupEl = document.getElementById("successPopup");
      if (successPopupEl) {
        const instance = Offcanvas.getOrCreateInstance(successPopupEl);
        instance.hide();
      }
    }, 3000);
  } catch (error: any) {
    console.error("Failed to create adjustment: ", error);

    // Set dynamic error message if available
    errorMessage.value = error?.response?.data?.message || "Failed to create adjustment.";

    const trigger = document.getElementById("triggerErrorPopup") as HTMLAnchorElement;
    trigger?.click();

    // Auto-hide error popup after 3s
    setTimeout(() => {
      const errorPopupEl = document.getElementById("errorPopup");
      if (errorPopupEl) {
        const instance = Offcanvas.getOrCreateInstance(errorPopupEl);
        instance.hide();
      }
    }, 3000);
  }
};
</script>