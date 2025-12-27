<template>
  <div class="card border-0 shadow-none rounded-1 mb-30">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-40 text-title">Selected Products For Adjustment</h6>
      <div class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">
                PRODUCT
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">
                PRODUCT CODE
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">
                AVAILABLE STOCK
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">
                QUANTITY
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">
                TYPE
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1 text-end pe-0">
                DELETE
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in products" :key="product.productId">
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">{{ product.productName }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ product.code }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph"><span class="badge badge-success">{{
                product.stock }}</span></td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <QuantityCounter v-model="product.adjustedQty" />
              </td>
              <td class="shadow-none lh-1">
                <select v-model="product.stockEffect" class="select-cat fs-14 fw-medium border-0">
                  <option :value="AdjustmentType.ADD">Addition</option>
                  <option :value="AdjustmentType.DEDUCT">Deduction</option>
                </select>
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group style-two ms-auto d-flex flex-wrap align-items-center">
                  <a class="delete-btn" @click.prevent="remove(index)" data-bs-toggle="offcanvas" href="#deletePopup"
                    role="button" aria-controls="deletePopup">
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
import { defineProps, defineEmits } from "vue";
import QuantityCounter from "./QuantityCounter.vue";
import { AdjustmentType } from "@/types/Adjustment";
import type { SelectedProduct } from "@/types/Adjustment";

const props = defineProps<{ products: SelectedProduct[] }>();
const emit = defineEmits<{ (e: "update:products", value: SelectedProduct[]): void }>();

const remove = (index: number) => {
  const updated = [...props.products];
  updated.splice(index, 1);
  emit("update:products", updated);
};
</script>

<style lang="scss" scoped>
.select-cat {
  height: 36px;
  border-radius: 2px;
  background: #ececec;
  width: 226px;
  padding-left: 20px;
  background-image: url(../../../assets/img/icons/down-arrow-4.svg);
  background-repeat: no-repeat;
  background-size: 10px;
  background-position: calc(100% - 20px) 16px;
}
</style>