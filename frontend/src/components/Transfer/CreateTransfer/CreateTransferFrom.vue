<template>
  <div class="row mb-40">
    <div class="col-xxl-10 col-lg-8 pe-xxl-8">
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Order Tax</label>
            <input type="number" v-model="modelOrderTax"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="0" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center">%</span>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Discount</label>
            <input type="number" v-model="modelDiscount"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="0" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">%</span>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Shipping Cost</label>
            <input type="number" v-model="modelShippingCost"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="0" />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16">{{
                symbol }}</span>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Status</label>
            <select v-model="modelStatus" class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option v-for="(label, value) in ShipmentStatusLabels" :key="value" :value="value">
                {{ label }}
              </option>
            </select>
          </div>
        </div>
      </div>
    </div>
    <div class="col-xxl-2 col-lg-4">
      <div class="card border-0 rounded-1 w-xxl-5 pt-12 pb-12 mb-md-25">
        <table class="table style-two">
          <tbody>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">ORDER TAX :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ modelOrderTax }}% ({{ symbol }}{{ taxAmount.toFixed(2) }})
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">DISCOUNT :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ modelDiscount }}% (-{{ symbol }}{{ discountAmount.toFixed(2) }})
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">SHIPPING :</th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                {{ symbol }}{{ shippingAmount.toFixed(2) }}
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-semibold">GRAND TOTAL :</th>
              <td class="fs-14 fw-bold lh-1 text-purple text-end">
                {{ symbol }}{{ grandTotal }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="col-12">
      <div class="form-group mb-25">
        <label class="d-block fs-14 text-black mb-2">Notes</label>
        <textarea cols="30" rows="10" v-model="modelNote" placeholder="Add a note"
          class="d-block w-100 bg-white border-0 rounded-1 resize-none fs-14 text-title" />
      </div>
    </div>
    <div class="col-xl-4">
      <button
        class="btn style-one d-inline-block transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16 mb-20"
        type="submit" @click="handleSubmit">
        Create Transfer
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps, defineEmits } from "vue";
import { ShipmentStatus, ShipmentStatusLabels, SelectedTransferProduct } from "@/types/Transfer";

const props = defineProps<{
  orderTax: string;
  discount: string;
  shippingCost: string;
  note: string;
  status: ShipmentStatus;
  shipmentStatusOptions: { label: string; value: ShipmentStatus }[];
  products: SelectedTransferProduct[];
  currencySymbol?: string;
}>();

const emit = defineEmits<{
  (e: "update:orderTax", value: string): void;
  (e: "update:discount", value: string): void;
  (e: "update:shippingCost", value: string): void;
  (e: "update:note", value: string): void;
  (e: "update:status", value: ShipmentStatus): void;
  (e: "submit"): void;
}>();

const modelOrderTax = computed({
  get: () => props.orderTax,
  set: (value) => emit("update:orderTax", value),
});

const modelDiscount = computed({
  get: () => props.discount,
  set: (value) => emit("update:discount", value),
});

const modelShippingCost = computed({
  get: () => props.shippingCost,
  set: (value) => emit("update:shippingCost", value),
});

const modelNote = computed({
  get: () => props.note,
  set: (value) => emit("update:note", value),
});

const modelStatus = computed({
  get: () => props.status,
  set: (value: ShipmentStatus) => emit("update:status", value),
});

const symbol = computed(() => props.currencySymbol ?? "$");

const totalSubtotals = computed(() =>
  props.products.reduce((sum, p) => sum + parseFloat(p.subTotal || "0"), 0)
);

const discountAmount = computed(() => {
  const percent = parseFloat(props.discount || "0");
  return (totalSubtotals.value * percent) / 100;
});

const taxAmount = computed(() => {
  const percent = parseFloat(props.orderTax || "0");
  const baseAfterDiscount = totalSubtotals.value - discountAmount.value;
  return (baseAfterDiscount * percent) / 100;
});

const shippingAmount = computed(() => parseFloat(props.shippingCost || "0"));

const grandTotal = computed(() => {
  const total = totalSubtotals.value - discountAmount.value + taxAmount.value + shippingAmount.value;
  return total.toFixed(2);
});

const handleSubmit = () => {
  emit("submit");
};
</script>