<template>
  <div class="modal fade" id="detailsModal" tabindex="-1" aria-labelledby="detailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-rl modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="detailsModalLabel">Transfer Details</h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>

        <div class="modal-body" v-if="transfer">
          <ul class="details-title list-style mb-40">
            <li class="fs-14 fw-medium text-title lh-1">
              DATE :<span class="fw-semibold ms-1">{{ formattedDate }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              TIME :<span class="fw-semibold ms-1">{{ formattedTime }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              REFERENCE :<span class="fw-semibold ms-1">#{{ transfer.id }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              FROM WAREHOUSE :<span class="fw-semibold ms-1">{{ fromWarehouseName }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              TO WAREHOUSE :<span class="fw-semibold ms-1">{{ toWarehouseName }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              GRAND TOTAL :<span class="fw-semibold ms-1">{{ currencySymbol }}{{ transfer.grandTotal }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              STATUS :
              <span class="fw-medium ms-1 badge badge-outline-green fs-14">
                {{ shipmentStatusLabel }}
              </span>
            </li>
          </ul>

          <div class="table-responsive style-two">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">NO.</th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">PRODUCT</th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">CODE</th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">QUANTITY</th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">TYPE</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(product, index) in transfer.products" :key="product.productId">
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ String(index + 1).padStart(2, '0') }}.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ productMap[product.productId] ?? `#${product.productId}` }}
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ product.productCode }}
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ product.transferredQty }}pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">Addition</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="modal-footer pt-20 pb-35">
          <a class="btn style-five upload-btn px-xxl-6 ms-auto" @click="handlePrint">
            Print
            <img src="../../../assets/img/icons/download.svg" alt="Image" />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps } from "vue";
import { Transfer, ShipmentStatusLabels } from "@/types/Transfer";
import { useSettingStore } from "@/stores/settingStore";
import { useWarehouseStore } from "@/stores/warehouseStore";

const props = defineProps<{
  transfer: Transfer | null;
  productMap: Record<number, string>;
}>();

const settingStore = useSettingStore();
const warehouseStore = useWarehouseStore();

const currencySymbol = computed(() => settingStore.currencySymbol ?? "$");

const formattedDate = computed(() =>
  props.transfer?.date ? new Date(props.transfer.date).toLocaleDateString() : "-"
);

const formattedTime = computed(() =>
  props.transfer?.date ? new Date(props.transfer.date).toLocaleTimeString() : "-"
);

const shipmentStatusLabel = computed(() =>
  props.transfer?.status ? ShipmentStatusLabels[props.transfer.status] : "-"
);

const fromWarehouseName = computed(() => {
  if (!props.transfer) return "-";
  return warehouseStore.warehouseMap[props.transfer.fromWarehouse] ?? `#${props.transfer.fromWarehouse}`;
});

const toWarehouseName = computed(() => {
  if (!props.transfer) return "-";
  return warehouseStore.warehouseMap[props.transfer.toWarehouse] ?? `#${props.transfer.toWarehouse}`;
});

const handlePrint = () => {
  window.print();
};
</script>