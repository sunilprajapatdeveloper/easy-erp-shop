<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Sales List" />
    <FilterContent btnText="Sales" btnLink="/create-sales" />

    <SalesList @delete-sale="onDeleteSale" @view-sale="onViewSale" />

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

  <SalesDetails :sale="selectedSale" :productMap="productMap" />
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { Offcanvas } from "bootstrap";

import { useSaleStore } from "@/stores/saleStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useProductStore } from "@/stores/productStore";
import type { Sale } from "@/types/Sale";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import FilterContent from "@/components/Common/FilterContent.vue";
import SalesList from "@/components/Sales/SalesList/SalesList.vue";
import SalesDetails from "@/components/Sales/SalesList/SalesDetails.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

// Stores
const saleStore = useSaleStore();
const warehouseStore = useWarehouseStore();
const productStore = useProductStore();

// State
const selectedSale = ref<Sale | null>(null);
const successMessage = ref("Sale deleted successfully.");
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
const onViewSale = (sale: Sale) => {
  selectedSale.value = sale;
};

const onDeleteSale = async (id: number) => {
  try {
    await saleStore.removeSale(id);
    await saleStore.fetchSales();

    successMessage.value = `Sale #${id} has been successfully deleted.`;
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
      `Failed to delete sale: #${id}`;

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
  saleStore.fetchSales();
  if (!productStore.products.length) productStore.fetchProducts();
  if (!warehouseStore.warehouses.length) warehouseStore.fetchWarehouses();
});
</script>