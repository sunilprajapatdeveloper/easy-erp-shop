<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Edit Adjustment" />
    <ChooseForm :warehouses="warehouses" v-model:warehouseId="warehouseId" v-model:date="date"
      @add-product="addProduct" />
    <SelectedProducts v-if="products.length > 0" v-model:products="products" />
    <NotesContent v-model:note="note" @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Product removed from the list
        </span>
      </div>
    </div>
  </div> -->

  <!-- Success Popup -->
  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Adjustment has been successfully updated
        </span>
      </div>
    </div>
  </div>

  <a id="triggerSuccessPopup" class="d-none" data-bs-toggle="offcanvas" href="#successPopup" role="button"
    aria-controls="successPopup"></a>

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
    aria-controls="errorPopup"></a>

  <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Your product is deleted from the list.
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAdjustmentStore } from "@/stores/adjustmentStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import type { SelectedProduct, CreateAdjustmentRequest } from "@/types/Adjustment";
import type { Warehouse } from "@/types/Warehouse";
import { Offcanvas } from "bootstrap";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import ChooseForm from "../../components/Adjustments/CreateAdjustments/ChooseForm.vue";
import SelectedProducts from "../../components/Adjustments/CreateAdjustments/SelectedProducts.vue";
import NotesContent from "../../components/Adjustments/CreateAdjustments/NotesContent.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

const route = useRoute();
const router = useRouter();
const adjustmentStore = useAdjustmentStore();
const warehouseStore = useWarehouseStore();

const adjustmentId = Number(route.params.id);
const warehouseId = ref<number | null>(null);
const date = ref<string>("");
const note = ref("");
const products = ref<SelectedProduct[]>([]);
const warehouses = ref<Warehouse[]>([]);
const errorMessage = ref("Something went wrong.");

onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  const adj = adjustmentStore.adjustments.find(a => a.id === adjustmentId);

  if (!adj) {
    // If not found in store, fetch from backend
    await adjustmentStore.fetchAdjustments();
  }

  const adjustment = adjustmentStore.adjustments.find(a => a.id === adjustmentId);
  if (adjustment) {
    warehouseId.value = adjustment.warehouse?.id ?? null;
    date.value = adjustment.date;
    note.value = adjustment.note || "";
    products.value = adjustment.products.map(p => ({
      productId: p.id,
      productName: p.name,
      code: p.code,
      stock: p.currentQty,
      adjustedQty: p.adjustedQty,
      stockEffect: p.stockEffect,
    }));
  } else {
    router.push("/not-found");
  }

  const successPopupEl = document.getElementById("successPopup");
  if (successPopupEl) {
    successPopupEl.addEventListener("hidden.bs.offcanvas", () => {
      router.push("/adjustment-list");
    });
  }
});

const addProduct = (product: SelectedProduct) => {
  if (!products.value.some(p => p.productId === product.productId)) {
    products.value.push(product);
  }
};

const handleSubmit = async () => {
  if (!warehouseId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse before submitting."
      : "Please add at least one product to continue.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => {
      Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide();
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

  console.log("Final JSON Payload:", JSON.stringify(payload, null, 2));

  try {
    await adjustmentStore.editAdjustment(adjustmentId, payload);

    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(() => {
      const el = document.getElementById("successPopup");
      if (el) {
        const instance = Offcanvas.getOrCreateInstance(el);
        instance.hide();
      }
    }, 3000);

  } catch (error: any) {
    console.error("Failed to update adjustment:", error);
    errorMessage.value = error?.response?.data?.message || "Failed to update adjustment.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => {
      Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide();
    }, 3000);
  }
};
</script>