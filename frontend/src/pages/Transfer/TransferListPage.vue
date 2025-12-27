<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Transfer List" />
    <FilterContent btnText="Transfer" btnLink="/create-transfer" />
    <TransferList @delete-transfer="onDeleteTransfer" @view-transfer="onViewTransfer" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- Delete success popup -->
  <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">{{ successMessage }}</span>
      </div>
    </div>
  </div>

  <!-- Delete error popup -->
  <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
    <div class="offcanvas-body p-0">
      <div class="create-error">
        <img src="../../assets/img/icons/close-circle-2.svg" alt="Image" style="filter: brightness(0) invert(1);" />
        <span class="text-white fw-medium">{{ errorMessage }}</span>
      </div>
    </div>
  </div>

  <DetailsModal :transfer="selectedTransfer" :productMap="productMap" />
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { Offcanvas } from "bootstrap";
import { Transfer } from "@/types/Transfer";
import { useTransferStore } from "@/stores/transferStore";
import { useProductStore } from "@/stores/productStore";
import { useWarehouseStore } from "@/stores/warehouseStore";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import FilterContent from "../../components/Common/FilterContent.vue";
import TransferList from "../../components/Transfer/TransferList/TransferList.vue";
import DetailsModal from "../../components/Transfer/TransferList/DetailsModal.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

// Stores
const transferStore = useTransferStore();
const productStore = useProductStore();
const warehouseStore = useWarehouseStore();

// State
const successMessage = ref("Transfer deleted successfully.");
const errorMessage = ref("Something went wrong.");
const selectedTransfer = ref<Transfer | null>(null);

// Computed: map of productId → product name
const productMap = computed(() => {
  const map: Record<number, string> = {};
  productStore.products.forEach(p => {
    if (p.id !== undefined) {
      map[p.id] = p.name;
    }
  });
  return map;
});

// Get warehouse name by ID
const getWarehouseName = (id: number): string => {
  const warehouse = warehouseStore.warehouses.find(w => w.id === id);
  return warehouse?.name || `#${id}`;
};

// Handle view action
const onViewTransfer = (transfer: Transfer) => {
  selectedTransfer.value = transfer;
};

// Handle delete action
const onDeleteTransfer = async (id: number) => {
  try {
    await transferStore.removeTransfer(id);
    await transferStore.fetchTransfers();

    successMessage.value = `Transfer #${id} has been successfully deleted.`;

    const el = document.getElementById("deletePopup");
    if (el) {
      const instance = Offcanvas.getOrCreateInstance(el);
      instance.show();
      setTimeout(() => instance.hide(), 3000);
    }
  } catch (error: any) {
    errorMessage.value =
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      `Failed to delete transfer: #${id}`;

    const el = document.getElementById("errorPopup");
    if (el) {
      const instance = Offcanvas.getOrCreateInstance(el);
      instance.show();
      setTimeout(() => instance.hide(), 3000);
    }
  }
};

// Load warehouses and products on mount
onMounted(() => {
  if (!productStore.products.length) productStore.fetchProducts();
  if (!warehouseStore.warehouses.length) warehouseStore.fetchWarehouses();
});
</script>