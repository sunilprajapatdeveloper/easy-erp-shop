<template>
  <div class="card-body">
    <h6 class="fs-18 mb-35 text-title fw-semibold">Selected Products</h6>
    <div class="table-responsive">
      <table class="table text-nowrap align-middle mb-0">
        <thead>
          <tr>
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">PRODUCT</th>
            <!-- <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">CODE</th> -->
            <!-- <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">STOCK</th> -->
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">QUANTITY</th>
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">UNIT PRICE</th>
            <!-- <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">DISCOUNT</th> -->
            <!-- <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">TAX</th> -->
            <!-- <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">SUBTOTAL</th> -->
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1 text-end pe-0">DELETE</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(product, index) in localProducts" :key="product.productId">
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">{{ product.productName }}</td>
            <!-- <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">{{ product.code }}</td> -->
            <!-- <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0"><span class="badge bg-success">{{ product.stock }}</span></td> -->
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
              <QuantityCounter v-model="product.saleQty" @update:modelValue="() => recalculate(index)" />
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">{{ product.price }}</td>

            <!-- <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">{{ product.discount }}</td> -->
            <!-- <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">{{ product.tax }}</td> -->
            <!-- <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">{{ product.subTotal }}</td> -->
            <td class="shadow-none lh-1 text-end pe-0">
              <div class="button-group style-two ms-auto d-flex flex-wrap align-items-center">
                <a @click.prevent="remove(index)" data-bs-toggle="offcanvas" href="#deletePopup">
                  <img src="../../../assets/img/icons/close.svg" alt="Delete" />
                </a>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import QuantityCounter from "./QuantityCounter.vue";
import { defineProps, defineEmits, reactive, watch } from "vue";
import type { SelectedPosProduct } from "@/types/Pos";
import { calculateSubTotal } from "@/utils/posUtils";

const props = defineProps<{ products: SelectedPosProduct[] }>();
const emit = defineEmits<{ (e: "update:products", value: SelectedPosProduct[]): void }>();

const localProducts = reactive<SelectedPosProduct[]>([]);

const recalculate = (index: number) => {
  const product = localProducts[index];
  product.subTotal = calculateSubTotal(product);
  emit("update:products", [...localProducts]);
};

const remove = (index: number) => {
  localProducts.splice(index, 1);
  emit("update:products", [...localProducts]);
};

// Sync localProducts with props
watch(
  () => props.products,
  (newVal) => {
    localProducts.splice(
      0,
      localProducts.length,
      ...newVal.map((p) => ({
        ...p,
        saleQty: p.saleQty ?? 1,
        subTotal: calculateSubTotal(p),
      }))
    );
  },
  { immediate: true, deep: true }
);
</script>