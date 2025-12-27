<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Purchase List" />
    <FilterContent btnText="Purchase" btnLink="/create-purchase" />

    <PurchaseList @delete-purchase="onDeletePurchase" @view-purchase="onViewPurchase" />

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

  <PurchaseDetails :purchase="selectedPurchase" :productMap="productMap" />
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { Offcanvas } from "bootstrap";

import { usePurchaseStore } from "@/stores/purchaseStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useProductStore } from "@/stores/productStore";
import type { Purchase } from "@/types/Purchase";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import FilterContent from "@/components/Common/FilterContent.vue";
import PurchaseList from "@/components/Purchases/PurchaseList/PurchaseList.vue";
import PurchaseDetails from "@/components/Purchases/PurchaseList/PurchaseDetails.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

// Stores
const purchaseStore = usePurchaseStore();
const warehouseStore = useWarehouseStore();
const productStore = useProductStore();

// State
const selectedPurchase = ref<Purchase | null>(null);
const successMessage = ref("Purchase deleted successfully.");
const errorMessage = ref("Something went wrong.");

// Product map
const productMap = computed(() => {
  const map: Record<number, string> = {};
  productStore.products.forEach(p => {
    if (p.id !== undefined) {
      map[p.id] = p.name;
    }
  });
  return map;
});

// Handlers
const onViewPurchase = (purchase: Purchase) => {
  selectedPurchase.value = purchase;
};

const onDeletePurchase = async (id: number) => {
  try {
    await purchaseStore.removePurchase(id);
    await purchaseStore.fetchPurchases();

    successMessage.value = `Purchase #${id} has been successfully deleted.`;
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
      `Failed to delete purchase: #${id}`;

    const el = document.getElementById("errorPopup");
    if (el) {
      const instance = Offcanvas.getOrCreateInstance(el);
      instance.show();
      setTimeout(() => instance.hide(), 3000);
    }
  }
};

// Initial data
onMounted(() => {
  purchaseStore.fetchPurchases();
  if (!productStore.products.length) productStore.fetchProducts();
  if (!warehouseStore.warehouses.length) warehouseStore.fetchWarehouses();
});
</script>
