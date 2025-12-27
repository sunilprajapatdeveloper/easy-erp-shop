<template>
  <div class="row mb-40">
    <div class="col-xxl-10 col-lg-8 pe-xxl-8">
      <div class="row">
        <!-- Order Tax -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Order Tax</label>
            <input type="number" v-model="orderTaxModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center">
              %
            </span>
          </div>
        </div>

        <!-- Discount -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Discount</label>
            <input type="number" v-model="discountModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center">
              %
            </span>
          </div>
        </div>

        <!-- Shipping -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Shipping Cost</label>
            <input type="number" v-model="shippingCostModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">
              $
            </span>
          </div>
        </div>

        <!-- Purchase Status -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Status</label>
            <select v-model="statusModel" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option v-for="(label, key) in purchaseStatusOptions" :key="key" :value="key">
                {{ label }}
              </option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <!-- Summary -->
    <div class="col-xxl-2 col-lg-4">
      <div class="card border-0 rounded-1 w-xxl-5 pt-12 pb-12 mb-md-25">
        <table class="table style-two">
          <tbody>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">ORDER TAX :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ orderTax }}% ({{ orderTaxFormatted }})
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">DISCOUNT :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ discountFormatted }}
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">SHIPPING :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ shippingCostFormatted }}
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-semibold">GRAND TOTAL :</th>
              <td class="fs-14 fw-bold lh-1 text-purple text-end">
                {{ grandTotalFormatted }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Notes -->
    <div class="col-12">
      <div class="form-group mb-25">
        <label class="d-block fs-14 text-black mb-2">Notes</label>
        <textarea v-model="noteModel" cols="30" rows="5" placeholder="Add a note"
          class="d-block w-100 bg-white border-0 rounded-1 resize-none fs-14 text-title"></textarea>
      </div>
    </div>

    <!-- Submit -->
    <div class="col-xl-4">
      <button
        class="btn style-one d-inline-block transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16 mb-20"
        type="button" @click="submit">
        Submit Purchase
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, defineProps, defineEmits, toRefs } from "vue";
import type { SelectedPurchaseProduct } from "@/types/Purchase";
import { PurchaseStatus, PurchaseStatusLabels } from "@/enums/purchaseStatus";

const purchaseStatusOptions = PurchaseStatusLabels;

const props = defineProps<{
  products: SelectedPurchaseProduct[];
  orderTax: string;
  discount: string;
  shippingCost: string;
  status: PurchaseStatus;
  note: string;
}>();

const emit = defineEmits<{
  (e: "update:orderTax", value: string): void;
  (e: "update:discount", value: string): void;
  (e: "update:shippingCost", value: string): void;
  (e: "update:status", value: PurchaseStatus): void;
  (e: "update:note", value: string): void;
  (e: "submit"): void;
}>();

const { products, orderTax, discount, shippingCost, status, note } = toRefs(props);

const safeNumber = (v: unknown, fallback = 0) => {
  const n = typeof v === "number" ? v : parseFloat((v as string) || "0");
  return Number.isFinite(n) ? n : fallback;
};

// Products total
const productsTotal = computed(() =>
  products.value.reduce((sum, p) => {
    const qty = safeNumber(p.purchaseQty, 1);
    const cost = safeNumber(p.cost);
    const disc = safeNumber(p.discount);
    const tax = safeNumber(p.tax);
    const row =
      p.subTotal != null ? safeNumber(p.subTotal) : (cost - disc + tax) * qty;
    return sum + row;
  }, 0)
);

// Tax & discount amounts
const orderTaxAmount = computed(
  () => (productsTotal.value * safeNumber(orderTax.value)) / 100
);

const discountAmount = computed(
  () => (productsTotal.value * safeNumber(discount.value)) / 100
);

// Grand total
const grandTotal = computed(
  () =>
    productsTotal.value +
    orderTaxAmount.value +
    safeNumber(shippingCost.value) -
    discountAmount.value
);

// Models
const orderTaxModel = computed({
  get: () => orderTax.value,
  set: (val: string) => emit("update:orderTax", val),
});

const discountModel = computed({
  get: () => discount.value,
  set: (val: string) => emit("update:discount", val),
});

const shippingCostModel = computed({
  get: () => shippingCost.value,
  set: (val: string) => emit("update:shippingCost", val),
});

const statusModel = computed({
  get: () => status.value,
  set: (val: PurchaseStatus) => emit("update:status", val),
});

const noteModel = computed({
  get: () => note.value,
  set: (val: string) => emit("update:note", val),
});

// Formatters
const currency = (n: number) => `$${n.toFixed(2)}`;
const orderTaxFormatted = computed(() => currency(orderTaxAmount.value));
const discountFormatted = computed(() => currency(discountAmount.value));
const shippingCostFormatted = computed(() =>
  currency(safeNumber(shippingCost.value))
);
const grandTotalFormatted = computed(() => currency(grandTotal.value));

function submit() {
  emit("submit");
}
</script>

<style scoped>
.percent-sign {
  position: absolute;
  top: 35px;
  right: 15px;
  width: 30px;
  height: 30px;
  background: #f4f4f4;
  color: #555;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
}
</style>
