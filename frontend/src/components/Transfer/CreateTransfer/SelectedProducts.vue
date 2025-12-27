<template>
  <div class="card border-0 shadow-none rounded-1 mb-30">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-35 text-title fw-semibold">
        Selected Products For Transfer
      </h6>
      <div class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">PRODUCT</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">CODE</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">UNIT COST</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">STOCK</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">QUANTITY</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">DISCOUNT</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">TAX</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">SUBTOTAL</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1 text-end pe-0">DELETE</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in localProducts" :key="product.productId">
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">{{ product.productName }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ product.code }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ product.cost }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fw-semibold fs-14">{{ product.stock }}</span>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <QuantityCounter v-model="product.transferredQty" @update:modelValue="() => recalculate(index)" />
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ product.discount }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ product.tax }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ product.subTotal }}</td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group style-two ms-auto d-flex flex-wrap align-items-center">
                  <a class="delete-btn" @click.prevent="remove(index)" data-bs-toggle="offcanvas" href="#deletePopup">
                    <img src="../../../assets/img/icons/close.svg" alt="Delete" />
                  </a>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import QuantityCounter from "./QuantityCounter.vue";
import { reactive, watch } from "vue";
import { defineProps, defineEmits } from "vue";
import type { SelectedTransferProduct } from "@/types/Transfer";
import { calculateSubTotal } from "@/utils/transferUtils";

const props = defineProps<{ products: SelectedTransferProduct[] }>();
const emit = defineEmits<{ (e: "update:products", value: SelectedTransferProduct[]): void }>();

const localProducts = reactive<SelectedTransferProduct[]>([]);

const recalculate = (index: number) => {
  const product = localProducts[index];
  product.subTotal = calculateSubTotal(product);
  emit("update:products", [...localProducts]);
};

const remove = (index: number) => {
  localProducts.splice(index, 1);
  emit("update:products", [...localProducts]);
};

// Sync localProducts when props.products change
watch(
  () => props.products,
  (newVal) => {
    localProducts.splice(
      0,
      localProducts.length,
      ...newVal.map((p) => ({
        ...p,
        transferredQty: p.transferredQty ?? 1,
        subTotal: calculateSubTotal(p),
      }))
    );
  },
  { immediate: true, deep: true }
);
</script>