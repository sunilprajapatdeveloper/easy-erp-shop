<template>
  <div class="row mb-40">
    <div class="col-xxl-10 col-lg-8 pe-xxl-8">
      <div class="row">
        <!-- Manual Discount -->
        <div class="col-12">
          <h6 class="fs-16 fw-semibold mb-3">Discount</h6>
        </div>
        <div class="col-md-4">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Discount Value</label>
            <input type="number" v-model="manualDiscountValueModel" placeholder="0" min="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
          </div>
        </div>
        <div class="col-md-4">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Discount Type</label>
            <select v-model="manualDiscountTypeModel" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option :value="null" disabled>Select Type</option>
              <option value="FLAT">Flat ($)</option>
              <option value="PERCENTAGE">Percentage (%)</option>
            </select>
          </div>
        </div>
        <div class="col-md-4">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Reason (optional)</label>
            <input type="text" v-model="manualDiscountReasonModel" placeholder="e.g. Loyalty discount"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
          </div>
        </div>

        <!-- System Discount -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">System Discount (optional)</label>
            <select v-model="appliedDiscountIdModel" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option :value="null">None</option>
              <option v-for="d in availableDiscounts" :key="d.id" :value="d.id">
                {{ d.name }} ({{ d.code }})
              </option>
            </select>
          </div>
        </div>

        <!-- Coupon Code -->
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Coupon Code</label>
            <input type="text" v-model="couponCodeModel" placeholder="SUMMER10"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
          </div>
        </div>

        <!-- Shipping Cost -->
        <div class="col-md-6">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Shipping Cost</label>
            <input type="number" v-model="shippingCostModel" placeholder="0" min="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">$</span>
          </div>
        </div>

        <!-- Paid Amount -->
        <div class="col-md-6">
          <div class="form-group mb-25 position-relative">
            <label class="d-block fs-14 text-black mb-2">Amount Paid</label>
            <input type="number" v-model="paidAmountModel" placeholder="0" min="0"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">$</span>
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
      </div>
    </div>

    <!-- Summary Section -->
    <div class="col-xxl-2 col-lg-4">
      <div class="card border-0 rounded-1 w-xxl-5 pt-12 pb-12 mb-md-25">
        <table class="table style-two">
          <tbody>
            <tr>
              <th class="fs-14 text-title lh-1 ls-1 fw-normal">SUBTOTAL :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ subtotalFormatted }}
              </td>
            </tr>
            <tr>
              <th class="fs-14 text-title lh-1 ls-1 fw-normal">TAX :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ taxFormatted }}
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
import { computed, toRefs } from "vue";
import { PaymentStatusLabels } from "@/enums/paymentStatus";
import { SaleStatusLabels } from "@/enums/saleStatus";
import type { SelectedSaleProduct } from "@/types/Sale";
import { DiscountType } from "@/enums/discountType";
import { SaleStatus } from "@/enums/saleStatus";
import { PaymentStatus } from "@/enums/paymentStatus";
import { calculateLineTotal } from "@/utils/saleCalculations";
import { DiscountItem } from "@/types/Discount";

const saleStatusOptions = SaleStatusLabels;
const paymentOptions = PaymentStatusLabels;

const props = defineProps<{
  products: SelectedSaleProduct[];
  manualDiscountValue: string;
  manualDiscountType: DiscountType | null;
  manualDiscountReason: string;
  appliedDiscountId: number | null;
  couponCode: string;
  shippingCost: string;
  paidAmount: string;
  paymentStatus: PaymentStatus;
  saleStatus: SaleStatus;
  note: string;
  availableDiscounts: DiscountItem[];
}>();

const emit = defineEmits<{
  (e: "update:manualDiscountValue", value: string): void;
  (e: "update:manualDiscountType", value: DiscountType | null): void;
  (e: "update:manualDiscountReason", value: string): void;
  (e: "update:appliedDiscountId", value: number | null): void;
  (e: "update:couponCode", value: string): void;
  (e: "update:shippingCost", value: string): void;
  (e: "update:paidAmount", value: string): void;
  (e: "update:paymentStatus", value: PaymentStatus): void;
  (e: "update:saleStatus", value: SaleStatus): void;
  (e: "update:note", value: string): void;
  (e: "submit"): void;
}>();

const {
  products,
  manualDiscountValue,
  manualDiscountType,
  manualDiscountReason,
  appliedDiscountId,
  couponCode,
  shippingCost,
  paidAmount,
  paymentStatus,
  saleStatus,
  note,
  availableDiscounts,
} = toRefs(props);

const safeNum = (v: unknown, fallback = 0) => {
  const n = typeof v === "number" ? v : parseFloat((v as string) || "0");
  return Number.isFinite(n) ? n : fallback;
};

// Local preview totals (for summary only, NOT sent to backend)
const subtotal = computed(() =>
  products.value.reduce((sum, p) => sum + (p.productUnitPrice * p.quantity), 0)
);
const taxTotal = computed(() =>
  products.value.reduce((sum, p) => sum + (p.lineTaxAmount ?? 0), 0)
);
const grandTotal = computed(
  () => subtotal.value + taxTotal.value + safeNum(shippingCost.value)
);

const currency = (n: number) => `$${n.toFixed(2)}`;
const subtotalFormatted = computed(() => currency(subtotal.value));
const taxFormatted = computed(() => currency(taxTotal.value));
const shippingCostFormatted = computed(() => currency(safeNum(shippingCost.value)));
const grandTotalFormatted = computed(() => currency(grandTotal.value));

// Two-way models
const manualDiscountValueModel = computed({
  get: () => manualDiscountValue.value,
  set: (v) => emit("update:manualDiscountValue", v),
});
const manualDiscountTypeModel = computed({
  get: () => manualDiscountType.value,
  set: (v) => emit("update:manualDiscountType", v),
});
const manualDiscountReasonModel = computed({
  get: () => manualDiscountReason.value,
  set: (v) => emit("update:manualDiscountReason", v),
});
const appliedDiscountIdModel = computed({
  get: () => appliedDiscountId.value,
  set: (v) => emit("update:appliedDiscountId", v),
});
const couponCodeModel = computed({
  get: () => couponCode.value,
  set: (v) => emit("update:couponCode", v),
});
const shippingCostModel = computed({
  get: () => shippingCost.value,
  set: (v) => emit("update:shippingCost", v),
});
const paidAmountModel = computed({
  get: () => paidAmount.value,
  set: (v) => emit("update:paidAmount", v),
});
const paymentStatusModel = computed({
  get: () => paymentStatus.value,
  set: (v) => emit("update:paymentStatus", v),
});
const saleStatusModel = computed({
  get: () => saleStatus.value,
  set: (v) => emit("update:saleStatus", v),
});
const noteModel = computed({
  get: () => note.value,
  set: (v) => emit("update:note", v),
});

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