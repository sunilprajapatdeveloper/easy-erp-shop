<template>
  <div class="card border-0 shadow-none rounded-1 mb-30">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-35 text-title fw-semibold">
        Selected Products For Purchase Return
      </h6>
      <div class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">PRODUCT</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">CODE</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">UNIT COST</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">STOCK</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">RETURN QTY</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">DISCOUNT</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">TAX</th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">SUBTOTAL</th>
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
                {{ product.unitCost }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fw-semibold fs-14">
                  {{ product.stock }}
                </span>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <QuantityCounter v-model="product.returnQty" :min="1" :max="product.stock"
                  @update:modelValue="newQty => updateQuantity(index, newQty)" />
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ product.discount }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ product.tax }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ product.subTotal }}
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

<script setup lang="ts">
import { defineProps, defineEmits } from "vue";
import QuantityCounter from "./QuantityCounter.vue";
import type { SelectedPurchaseReturnProduct } from "@/types/PurchaseReturn";

const props = defineProps<{ products: SelectedPurchaseReturnProduct[] }>();

const emit = defineEmits<{
  (e: "update:products", value: SelectedPurchaseReturnProduct[]): void;
}>();

function updateQuantity(index: number, newQty: number) {
  const updated = props.products.map((p, i) =>
    i === index
      ? {
        ...p,
        returnQty: newQty,
        subTotal: calculateSubtotal(p.unitCost, p.discount, p.tax, newQty),
      }
      : p
  );
  emit("update:products", updated);
}

function removeProduct(index: number) {
  const updated = [...props.products];
  updated.splice(index, 1);
  emit("update:products", updated);
}

function calculateSubtotal(
  unitCost: string,
  discount: string,
  tax: string,
  qty: number
): string {
  const cost = parseFloat(unitCost || "0");
  const dis = parseFloat(discount || "0");
  const tx = parseFloat(tax || "0");
  const subtotal = (cost - dis + tx) * qty;
  return subtotal.toFixed(2);
}
</script>
