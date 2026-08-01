<template>
  <div class="card border-0 shadow-none rounded-1 mb-30">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-35 text-title fw-semibold">Selected Products For Sales</h6>
      <div class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">PRODUCT</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">CODE</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">UNIT PRICE</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">STOCK</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">QUANTITY</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">TAX</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">NET</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">GROSS</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1 text-end pe-0">DELETE</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in products" :key="product.productId">
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                {{ product.productName }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ product.code }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                ${{ product.productUnitPrice.toFixed(2) }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fw-semibold fs-14">
                  {{ product.stock }}
                </span>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <QuantityCounter v-model="product.quantity" :min="1" :max="product.stock"
                  @update:modelValue="(newQty) => updateQuantity(index, newQty)" />
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                ${{ (product.lineTaxAmount ?? 0).toFixed(2) }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                ${{ (product.lineNetAmount ?? 0).toFixed(2) }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                ${{ (product.lineGrossAmount ?? 0).toFixed(2) }}
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group style-two ms-auto d-flex flex-wrap align-items-center">
                  <a class="delete-btn" role="button" @click="removeProduct(index)">
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

<script lang="ts" setup>
import QuantityCounter from "./QuantityCounter.vue";
import type { SelectedSaleProduct } from "@/types/Sale";
import { calculateSaleLine } from "@/utils/saleCalculations";

const props = defineProps<{ products: SelectedSaleProduct[] }>();
const emit = defineEmits<{
  (e: "update:products", value: SelectedSaleProduct[]): void;
}>();

function updateQuantity(index: number, newQty: number) {
  const updated = props.products.map((p, i) => {
    if (i !== index) return p;
    const calc = calculateSaleLine({ ...p, quantity: newQty });
    return {
      ...p,
      quantity: newQty,
      lineDiscountAmount: calc.lineDiscountAmount,
      lineNetAmount: calc.lineNetAmount,
      lineTaxAmount: calc.lineTaxAmount,
      lineGrossAmount: calc.lineGrossAmount,
    };
  });
  emit("update:products", updated);
}

function removeProduct(index: number) {
  const updated = [...props.products];
  updated.splice(index, 1);
  emit("update:products", updated);
}
</script>