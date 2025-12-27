<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div v-if="canView" class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="select_all" />
                  <label class="form-check-label" for="select_all">
                    DATE
                    <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
                  </label>
                </div>
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">REFERENCE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">SUPPLIER
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">WAREHOUSE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">STATUS
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">GRAND TOTAL
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">ACTION</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="purchase in purchases" :key="purchase.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input class="form-check-input" type="checkbox" :id="'checkbox_' + purchase.id" />
                    <label class="form-check-label text-optional" :for="'checkbox_' + purchase.id"></label>
                  </div>
                  <span class="text-optional fs-14 ms-2">{{ purchase.date }}</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">#{{ purchase.id }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ supplierMap[purchase.supplierId] ?? `#${purchase.supplierId}` }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ warehouseMap[purchase.warehouseId] ?? `#${purchase.warehouseId}` }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fs-14 fw-normal">{{ purchase.purchaseStatus }}</span>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ currencySymbol }}{{ purchase.totalAmount }}
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a v-if="canView" href="javascript:void(0)" title="View" data-bs-toggle="modal"
                    data-bs-target="#detailsModal" @click="$emit('view-purchase', purchase)">
                    <img src="../../../assets/img/icons/eye.svg" alt="Image" />
                  </a>
                  <router-link v-if="canEdit" :to="`/edit-purchase/${purchase.id}`" title="Edit">
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </router-link>
                  <a v-if="canDelete" class="delete-btn" data-bs-toggle="offcanvas" href="#deletePopup" role="button"
                    @click="$emit('delete-purchase', purchase.id)">
                    <img src="../../../assets/img/icons/close.svg" alt="Image" />
                  </a>
                </div>
              </td>
            </tr>

            <tr v-if="!purchaseStore.loading && purchases.length === 0">
              <td colspan="7" class="text-center text-muted py-3">No purchases found.</td>
            </tr>
          </tbody>
        </table>

        <div v-if="purchaseStore.loading" class="text-center py-4">
          <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
          <span class="ms-2">Loading purchases...</span>
        </div>
      </div>
    </div>
  </div>

  <!-- Pagination -->
  <div class="row pb-45 align-items-center">
    <div class="col-sm-6">
      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-sm-start page-unit">
        <span class="fs-13">Showing purchases per page</span>
        <select class="text-title border-0 fs-14 bg-transparent">
          <option value="10">10</option>
          <option value="20">20</option>
          <option value="30">30</option>
        </select>
      </div>
    </div>
    <div class="col-sm-6 text-sm-end text-center">
      <ul class="page-nav list-style">
        <li><a href="#"><img src="../../../assets/img/icons/left-arrow-purple.svg" alt="Image" /></a></li>
        <li><a href="#" class="active">1</a></li>
        <li><a href="#">2</a></li>
        <li><a href="#">3</a></li>
        <li><a href="#"><img src="../../../assets/img/icons/right-arrow-purple.svg" alt="Image" /></a></li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed } from "vue";
import { usePurchaseStore } from "@/stores/purchaseStore";
import { useUserStore } from "@/stores/userStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useSupplierStore } from "@/stores/supplierStore";
import { useSettingStore } from "@/stores/settingStore";

const purchaseStore = usePurchaseStore();
const userStore = useUserStore();
const warehouseStore = useWarehouseStore();
const supplierStore = useSupplierStore();
const settingStore = useSettingStore();

onMounted(async () => {
  await Promise.all([
    warehouseStore.fetchWarehouses(),
    supplierStore.fetchSuppliers(),
    purchaseStore.fetchPurchases(),
  ]);
});

const purchases = computed(() => purchaseStore.purchases);
const currencySymbol = computed(() => settingStore.currencySymbol ?? "$");
const warehouseMap = computed(() => warehouseStore.warehouseMap);
const supplierMap = computed(() => supplierStore.supplierMap);

// Permissions
const canView = computed(() => userStore.userPermissions.includes("PURCHASE_VIEW"));
const canEdit = computed(() => userStore.userPermissions.includes("PURCHASE_EDIT"));
const canDelete = computed(() => userStore.userPermissions.includes("PURCHASE_DELETE"));
</script>
