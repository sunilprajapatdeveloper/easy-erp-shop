<template>
  <div class="row mb-40">
    <div class="col-xxl-10 col-lg-8 pe-xxl-8">
      <div class="row">
        <!-- Return Tax -->
        <div class="col-md-6">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Return Tax</label>
            <input type="number" v-model="returnTaxModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span class="percent-sign position-absolute rounded-1 text-center fw-semibold fs-16">
              %
            </span>
          </div>
        </div>

        <!-- Discount -->
        <div class="col-md-6">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Return Discount</label>
            <input type="number" v-model="returnDiscountModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span class="percent-sign position-absolute rounded-1 text-center fw-semibold fs-16">
              $
            </span>
          </div>
        </div>

        <!-- Shipping Cost -->
        <div class="col-md-6">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Shipping Cost</label>
            <input type="number" v-model="shippingCostModel" placeholder="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span class="percent-sign position-absolute rounded-1 text-center fw-semibold fs-16">
              $
            </span>
          </div>
        </div>

        <!-- Return Status -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Return Status</label>
            <select v-model="returnStatusModel" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option value="PENDING">Pending</option>
              <option value="SENT">Sent</option>
              <option value="RECEIVED">Received</option>
            </select>
          </div>
        </div>

        <!-- Shipping Status -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Shipping Status</label>
            <select v-model="shipmentStatusModel" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option value="PENDING">Pending</option>
              <option value="SHIPPED">Shipped</option>
              <option value="DELIVERED">Delivered</option>
              <option value="CANCELED">Canceled</option>
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
              <th class="fs-14 text-title fw-normal">RETURN TAX :</th>
              <td class="fs-14 fw-semibold text-optional text-end">
                {{ returnTax }}% ({{ returnTaxFormatted }})
              </td>
            </tr>
            <tr>
              <th class="fs-14 text-title fw-normal">DISCOUNT :</th>
              <td class="fs-14 fw-semibold text-optional text-end">
                {{ returnDiscountFormatted }}
              </td>
            </tr>
            <tr>
              <th class="fs-14 text-title fw-normal">SHIPPING :</th>
              <td class="fs-14 fw-semibold text-optional text-end">
                {{ shippingCostFormatted }}
              </td>
            </tr>
            <tr>
              <th class="fs-14 text-title fw-semibold">GRAND TOTAL :</th>
              <td class="fs-14 fw-bold text-purple text-end">
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
      <button class="btn style-one d-inline-block transition border-0 fw-medium text-white rounded-1 fs-lg-16 mb-20"
        type="button" @click="submit">
        Submit Sales Return
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, defineEmits, defineProps, toRefs } from "vue";
import type { SelectedSaleReturnProduct } from "@/types/saleReturn";

const props = defineProps<{
  products: SelectedSaleReturnProduct[];
  returnTax: string;
  returnDiscount: string;
  shippingCost: string;
  returnStatus: string;
  shipmentStatus: string;
  note: string;
}>();

const emit = defineEmits<{
  (e: "update:returnTax", value: string): void;
  (e: "update:returnDiscount", value: string): void;
  (e: "update:shippingCost", value: string): void;
  (e: "update:returnStatus", value: string): void;
  (e: "update:shipmentStatus", value: string): void;
  (e: "update:note", value: string): void;
  (e: "submit"): void;
}>();

const { products, returnTax, returnDiscount, shippingCost, returnStatus, shipmentStatus, note } = toRefs(props);

const safeNumber = (v: unknown, fallback = 0) => {
  const n = typeof v === "number" ? v : parseFloat((v as string) || "0");
  return Number.isFinite(n) ? n : fallback;
};

// Products total
const productsTotal = computed(() =>
  products.value.reduce((sum, p) => {
    const qty = safeNumber(p.returnQty, 1);
    const price = safeNumber(p.price);
    const disc = safeNumber(p.discount);
    const tax = safeNumber(p.tax);
    const row = p.subTotal != null ? safeNumber(p.subTotal) : (price - disc + tax) * qty;
    return sum + row;
  }, 0)
);

// Tax & discount amounts
const returnTaxAmount = computed(() => (productsTotal.value * safeNumber(returnTax.value)) / 100);
const returnDiscountAmount = computed(() => safeNumber(returnDiscount.value));

// Grand total
const grandTotal = computed(
  () => productsTotal.value + returnTaxAmount.value + safeNumber(shippingCost.value) - returnDiscountAmount.value
);

// v-model bindings
const returnTaxModel = computed({
  get: () => returnTax.value,
  set: (val: string) => emit("update:returnTax", val),
});
const returnDiscountModel = computed({
  get: () => returnDiscount.value,
  set: (val: string) => emit("update:returnDiscount", val),
});
const shippingCostModel = computed({
  get: () => shippingCost.value,
  set: (val: string) => emit("update:shippingCost", val),
});
const returnStatusModel = computed({
  get: () => returnStatus.value,
  set: (val: string) => emit("update:returnStatus", val),
});
const shipmentStatusModel = computed({
  get: () => shipmentStatus.value,
  set: (val: string) => emit("update:shipmentStatus", val),
});
const noteModel = computed({
  get: () => note.value,
  set: (val: string) => emit("update:note", val),
});

// Formatting
const currency = (n: number) => `$${n.toFixed(2)}`;
const returnTaxFormatted = computed(() => currency(returnTaxAmount.value));
const returnDiscountFormatted = computed(() => currency(returnDiscountAmount.value));
const shippingCostFormatted = computed(() => currency(safeNumber(shippingCost.value)));
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
