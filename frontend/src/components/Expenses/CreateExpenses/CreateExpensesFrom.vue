<template>
  <form @submit.prevent="handleSubmit" class="row mb-40">
    <!-- Date -->
    <div class="col-md-6">
      <div class="form-group mb-25">
        <label class="d-block fs-14 text-black mb-2">Date</label>
        <input type="date" v-model="date" class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
      </div>
    </div>

    <!-- Warehouse -->
    <div class="col-md-6">
      <div class="form-group mb-25">
        <label class="d-block fs-14 text-black mb-2">Warehouse</label>
        <select v-model="warehouseId" class="bg-white border-0 rounded-1 fs-14 text-optional w-100 h-55">
          <option disabled value="">Select Warehouse</option>
          <option v-for="w in warehouses" :key="w.id" :value="w.id">
            {{ w.name }}
          </option>
        </select>
      </div>
    </div>

    <!-- Category -->
    <div class="col-md-6">
      <div class="form-group mb-25">
        <label class="d-block fs-14 text-black mb-2">Expense Category</label>
        <select v-model="categoryId" class="bg-white border-0 rounded-1 fs-14 text-optional w-100 h-55">
          <option disabled value="">Select Category</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">
            {{ c.name }}
          </option>
        </select>
      </div>
    </div>

    <!-- Amount -->
    <div class="col-md-6">
      <div class="form-group mb-25 position-relative">
        <label class="d-block fs-14 text-black mb-2">Amount</label>
        <input type="number" v-model="amount" class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
          placeholder="0" />
        <span
          class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold text-optional">
          $
        </span>
      </div>
    </div>

    <!-- Details -->
    <div class="col-lg-12">
      <div class="form-group mb-25">
        <label class="d-block fs-14 text-black mb-2">Details</label>
        <textarea v-model="details" cols="30" rows="5" placeholder="A few words"
          class="d-block w-100 bg-white border-0 rounded-1 resize-none fs-14 text-title"></textarea>
      </div>
    </div>

    <!-- Submit -->
    <div class="col-12">
      <button class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16" type="submit"
        :disabled="isSubmitting">
        {{ isSubmitting ? "Creating..." : "Create Expense" }}
      </button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { Offcanvas } from "bootstrap";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCategoryStore } from "@/stores/categoryStore";
import { useExpensesStore } from "@/stores/expensesStore";
import type { Warehouse } from "@/types/Warehouse";
import type { Category } from "@/types/Category";
import type { CreateExpensesRequest } from "@/types/Expenses";

const warehouseStore = useWarehouseStore();
const categoryStore = useCategoryStore();
const expenseStore = useExpensesStore();

const warehouses = ref<Warehouse[]>([]);
const categories = ref<Category[]>([]);

const date = ref<string>(new Date().toISOString().split("T")[0]);
const warehouseId = ref<number | null>(null);
const categoryId = ref<number | null>(null);
const amount = ref<string>("0");
const details = ref<string>("");

const isSubmitting = ref(false);
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
        : "Please enter a valid amount.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(
      () =>
        Offcanvas.getOrCreateInstance(
          document.getElementById("errorPopup")!
        ).hide(),
      3000
    );
    return;
  }

  const payload: CreateExpensesRequest = {
    date: date.value,
    warehouseId: warehouseId.value!,
    categoryId: categoryId.value!,
    amount: parseFloat(amount.value),
    details: details.value,
  };

  try {
    isSubmitting.value = true;
    await expenseStore.addExpense(payload);

    resetForm();
    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(
      () =>
        Offcanvas.getOrCreateInstance(
          document.getElementById("successPopup")!
        ).hide(),
      3000
    );
  } catch (error: any) {
    errorMessage.value =
      error?.response?.data?.message ?? "Failed to create expense.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(
      () =>
        Offcanvas.getOrCreateInstance(
          document.getElementById("errorPopup")!
        ).hide(),
      3000
    );
  } finally {
    isSubmitting.value = false;
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
