<template>
  <div class="modal fade" id="detailsModal" tabindex="-1" aria-labelledby="detailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-rl modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="detailsModalLabel">
            Adjustment Details
          </h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>

        <div class="modal-body" v-if="adjustment">
          <ul class="details-title list-style mb-40">
            <li class="fs-14 fw-medium text-title lh-1">
              DATE :<span class="fw-semibold ms-1">{{ formattedDate }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              TIME :<span class="fw-semibold ms-1">{{ formattedTime }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              REFERENCE :<span class="fw-semibold ms-1">{{ adjustment?.id }}</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              WAREHOUSE :<span class="fw-semibold ms-1">{{ adjustment?.warehouse.name }}</span>
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
                <tr v-for="(product, index) in adjustment?.products" :key="product.id">
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ String(index + 1).padStart(2, "0") }}.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ product.name }}
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ product.code }}
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ product.adjustedQty }}pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    {{ product.stockEffect === 'ADD' ? 'Addition' : 'Deduction' }}
                  </td>
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
import type { Adjustment } from "@/types/Adjustment";
import { defineProps, computed } from "vue";

const props = defineProps<{ adjustment: Adjustment | null }>();

const formattedDate = computed(() =>
  props.adjustment?.date ? new Date(props.adjustment.date).toLocaleDateString() : "-"
);
const formattedTime = computed(() =>
  props.adjustment?.date ? new Date(props.adjustment.date).toLocaleTimeString() : "-"
);

const handlePrint = () => {
  window.print();
};
</script>