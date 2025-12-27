<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div v-if="canView" class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="test_1" />
                  <label class="form-check-label" for="test_1">
                    DATE
                    <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
                  </label>
                </div>
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                REFERENCE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                WAREHOUSE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                TOTAL PRODUCTS
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th v-if="canView || canEdit || canDelete" scope="col" style="min-width: 150px;"
                class="text-title fw-normal fs-14 pt-0 pe-0">ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="adjustment in adjustments" :key="adjustment.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input class="form-check-input" type="checkbox" />
                  </div>
                  <span class="text-optional fs-14 ms-2">{{ adjustment.date }}</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ adjustment.id }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ adjustment.warehouse?.name ?? '-' }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ adjustment.products.length }}</td>
              <td v-if="canView || canEdit || canDelete" class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a v-if="canView" href="javascript:void(0)" title="View" data-bs-toggle="modal"
                    data-bs-target="#detailsModal" @click="$emit('view-adjustment', adjustment)">
                    <img src="../../../assets/img/icons/eye.svg" alt="Image" />
                  </a>
                  <router-link v-if="canEdit" :to="`/edit-adjustment/${adjustment.id}`" title="Edit">
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </router-link>
                  <a v-if="canDelete" class="delete-btn" href="javascript:void(0)"
                    @click="$emit('delete-adjustment', adjustment.id)">
                    <img src="../../../assets/img/icons/close.svg" alt="Image" />
                  </a>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="adjustmentStore.loading" class="text-center py-4">
          <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
          <span class="ms-2">Loading adjustments...</span>
        </div>
      </div>
    </div>
  </div>
  <div class="row pb-45 align-items-center">
    <div class="col-sm-6">
      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-sm-start page-unit">
        <span class="fs-13">Showing product per page</span>
        <select class="text-title border-0 fs-14 bg-transparent">
          <option value="0">10</option>
          <option value="1">20</option>
          <option value="2">30</option>
        </select>
      </div>
    </div>
    <div class="col-sm-6 text-sm-end text-center">
      <ul class="page-nav list-style">
        <li>
          <a href="#">
            <img src="../../../assets/img/icons/left-arrow-purple.svg" alt="Image" />
          </a>
        </li>
        <li><a href="#" class="active">1</a></li>
        <li><a href="#">2</a></li>
        <li><a href="#">3</a></li>
        <li>
          <a href="#">
            <img src="../../../assets/img/icons/right-arrow-purple.svg" alt="Image" />
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed } from "vue";
import { useAdjustmentStore } from "@/stores/adjustmentStore";
import { useUserStore } from "@/stores/userStore";

const adjustmentStore = useAdjustmentStore();
const userStore = useUserStore();

onMounted(() => {
  adjustmentStore.fetchAdjustments();
});

const adjustments = computed(() => adjustmentStore.adjustments);

const canView = computed(() => userStore.userPermissions.includes("ADJUSTMENT_VIEW"));
const canEdit = computed(() => userStore.userPermissions.includes("ADJUSTMENT_EDIT"));
const canDelete = computed(() => userStore.userPermissions.includes("ADJUSTMENT_DELETE"));
</script>