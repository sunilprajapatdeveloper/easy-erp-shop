<template>
  <MainHeader />
  <MainSidebar />

  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Create Expenses" />

    <CreateExpensesFrom :warehouses="warehouses" :categories="categories" v-model:warehouseId="warehouseId"
      v-model:categoryId="categoryId" v-model:date="date" v-model:amount="amount" v-model:details="details"
      @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- Success Popup -->
  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">Expense has been successfully created</span>
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
import type { CreateExpensesRequest } from "@/types/Expenses";
import type { Warehouse } from "@/types/Warehouse";
import type { Category } from "@/types/Category";

import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCategoryStore } from "@/stores/categoryStore";
import { useExpensesStore } from "@/stores/expensesStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import CreateExpensesFrom from "@/components/Expenses/CreateExpenses/CreateExpensesFrom.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

const warehouseStore = useWarehouseStore();
const categoryStore = useCategoryStore();
const expensesStore = useExpensesStore();

const warehouses = ref<Warehouse[]>([]);
const categories = ref<Category[]>([]);

const date = ref<string>(new Date().toISOString().split("T")[0]);
const warehouseId = ref<number | null>(null);
const categoryId = ref<number | null>(null);
const amount = ref<string>("0");
const details = ref<string>("");

const errorMessage = ref("Something went wrong. Please try again.");

onMounted(async () => {
  await warehouseStore.fetchWarehouses();
  await categoryStore.fetchCategories();

  warehouses.value = warehouseStore.warehouses;
  categories.value = categoryStore.categories;
});

const handleSubmit = async () => {
  if (!date.value || !warehouseId.value || !categoryId.value || !amount.value) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : !categoryId.value
        ? "Please select a category."
        : !amount.value || parseFloat(amount.value) <= 0
          ? "Please enter a valid amount."
          : "Please fill all required fields.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(
      () => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide(),
      3000
    );
    return;
  }

  const payload: CreateExpensesRequest = {
    warehouseId: warehouseId.value!,
    categoryId: categoryId.value!,
    date: date.value,
    amount: parseFloat(amount.value),
    details: details.value,
  };

  try {
    await expensesStore.addExpense(payload);

    resetForm();

    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(
      () => Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!).hide(),
      3000
    );
  } catch (error: any) {
    errorMessage.value =
      error?.response?.data?.message ?? "Failed to create expense.";
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
  categoryId.value = null;
  amount.value = "0";
  details.value = "";
};
</script>
