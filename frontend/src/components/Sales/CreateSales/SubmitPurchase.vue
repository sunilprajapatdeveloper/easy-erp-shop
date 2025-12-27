<template>
  <div class="row mb-40">
    <div class="col-xxl-10 col-lg-8 pe-xxl-8">
      <div class="row">
        <!-- Order Tax -->
        <div class="col-md-6">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Order Tax</label>
            <input type="number" v-model="orderTaxModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">
              %
            </span>
          </div>
        </div>

        <!-- Discount -->
        <div class="col-md-6">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Discount</label>
            <input type="number" v-model="discountModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">
              %
            </span>
          </div>
        </div>

        <!-- Shipping Cost -->
        <div class="col-md-6">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Shipping Cost</label>
            <input type="number" v-model="shippingCostModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">
              $
            </span>
          </div>
        </div>

        <!-- Sale Status -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Sale Status</label>
            <select v-model="saleStatusModel" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option v-for="(label, key) in saleStatusOptions" :key="key" :value="key">
                {{ label }}
              </option>
            </select>
          </div>
        </div>

        <!-- Payment Status -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Payment Status</label>
            <select v-model="paymentStatusModel" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option v-for="(label, key) in paymentOptions" :key="key" :value="key">
                {{ label }}
              </option>
            </select>
          </div>
        </div>

        <!-- Amount Paid -->
        <div class="col-md-6" v-if="isPaidOrPartial">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Amount Paid</label>
            <input type="number" v-model="paidAmountModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">
              $
            </span>
          </div>
        </div>

        <!-- Payment Method -->
        <div class="col-md-6" v-if="isPaidOrPartial">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Payment Method</label>
            <select v-model="paymentMethodModel" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option v-for="(label, key) in PaymentMethodOptions" :key="key" :value="key">
                {{ label }}
              </option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <!-- Summary Section -->
    <div class="col-xxl-2 col-lg-4">
      <div class="card border-0 rounded-1 w-xxl-5 pt-12 pb-12 mb-md-25">
        <table class="table style-two">
          <tbody>
            <tr>
              <th class="fs-14 text-title lh-1 ls-1 fw-normal">ORDER TAX :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ orderTax }}% ({{ orderTaxFormatted }})
              </td>
            </tr>
            <tr>
              <th class="fs-14 text-title lh-1 ls-1 fw-normal">DISCOUNT :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ discount }}% ({{ discountFormatted }})
              </td>
            </tr>
            <tr>
              <th class="fs-14 text-title lh-1 ls-1 fw-normal">SHIPPING :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ shippingCostFormatted }}
              </td>
            </tr>
            <tr>
              <th class="fs-14 text-title lh-1 ls-1 fw-semibold">GRAND TOTAL :</th>
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
        Submit Sales
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, defineEmits, defineProps, toRefs } from "vue";
import { PaymentStatusLabels } from "@/enums/paymentStatus";
import { PaymentMethodLabels } from "@/enums/paymentMethods";
import type { SelectedSaleProduct } from "@/types/Sale";
import { SaleStatus, SaleStatusLabels } from "@/enums/saleStatus";

const saleStatusOptions = SaleStatusLabels;
const paymentOptions = PaymentStatusLabels;
const PaymentMethodOptions = PaymentMethodLabels;

const props = defineProps<{
  products: SelectedSaleProduct[];
  orderTax: string;
  discount: string;
  shippingCost: string;
  paymentStatus: string;
  paidAmount: string;
  paymentMethod: string;
  saleStatus: SaleStatus;
  note: string;
}>();

const emit = defineEmits<{
  (e: "update:orderTax", value: string): void;
  (e: "update:discount", value: string): void;
  (e: "update:shippingCost", value: string): void;
  (e: "update:paymentStatus", value: string): void;
  (e: "update:paidAmount", value: string): void;
  (e: "update:paymentMethod", value: string): void;
  (e: "update:saleStatus", value: SaleStatus): void;
  (e: "update:note", value: string): void;
  (e: "submit"): void;
}>();

const { products, orderTax, discount, shippingCost, paymentStatus, paidAmount, paymentMethod, saleStatus, note } =
  toRefs(props);

const safeNumber = (v: unknown, fallback = 0) => {
  const n = typeof v === "number" ? v : parseFloat((v as string) || "0");
  return Number.isFinite(n) ? n : fallback;
};

// Products total
const productsTotal = computed(() =>
  products.value.reduce((sum, p) => {
    const qty = safeNumber(p.saleQty, 1);
    const price = safeNumber(p.price);
    const disc = safeNumber(p.discount);
    const tax = safeNumber(p.tax);
    const row =
      p.subTotal != null ? safeNumber(p.subTotal) : (price - disc + tax) * qty;
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

const saleStatusModel = computed({
  get: () => saleStatus.value,
  set: (val: SaleStatus) => emit("update:saleStatus", val),
});

const paymentStatusModel = computed({
  get: () => paymentStatus.value,
  set: (val: string) => emit("update:paymentStatus", val),
});

const paidAmountModel = computed({
  get: () => paidAmount.value,
  set: (val: string) => emit("update:paidAmount", val),
});

const paymentMethodModel = computed({
  get: () => paymentMethod.value,
  set: (val: string) => emit("update:paymentMethod", val),
});

const noteModel = computed({
  get: () => note.value,
  set: (val: string) => emit("update:note", val),
});

// Formatted
const currency = (n: number) => `$${n.toFixed(2)}`;
const orderTaxFormatted = computed(() => currency(orderTaxAmount.value));
const discountFormatted = computed(() => currency(discountAmount.value));
const shippingCostFormatted = computed(() =>
  currency(safeNumber(shippingCost.value))
);
const grandTotalFormatted = computed(() => currency(grandTotal.value));

const isPaidOrPartial = computed(() =>
  ["PAID", "PARTIALLY_PAID"].includes(paymentStatus.value)
);

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
