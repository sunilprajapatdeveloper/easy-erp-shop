<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div v-if="canView" class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="selectAll" />
                  <label class="form-check-label" for="selectAll">
                    DATE
                    <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
                  </label>
                </div>
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                REFERENCE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                CUSTOMER
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                WAREHOUSE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                STATUS
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                GRAND TOTAL
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                PAID
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                DUE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                PAYMENT STATUS
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="returnItem in saleReturns" :key="returnItem.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input class="form-check-input" type="checkbox" :id="'checkbox_' + returnItem.id" />
                    <label class="form-check-label text-optional" :for="'checkbox_' + returnItem.id">
                    </label>
                  </div>
                  <span class="text-optional fs-14 ms-2">{{ returnItem.date ?? "-" }}</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ returnItem.referenceNumber ?? "-" }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ customerMap[returnItem.customerId] ?? `#${returnItem.customerId ?? "-"}` }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ warehouseMap[returnItem.warehouseId] ?? `#${returnItem.warehouseId ?? "-"}` }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fs-14 fw-normal">
                  {{ returnItem.returnStatus ?? "-" }}
                </span>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ currencySymbol }}{{ returnItem.returnTax ?? "0.00" }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ currencySymbol }}{{ returnItem.returnDiscount ?? "0.00" }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ currencySymbol }}{{ returnItem.shippingCost ?? "0.00" }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fs-14 fw-normal">
                  {{ returnItem.shipmentStatus ?? "-" }}
                </span>
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a v-if="canView" href="javascript:void(0)" title="View" data-bs-toggle="modal"
                    data-bs-target="#detailsModal" @click="$emit('view-return', returnItem)">
                    <img src="../../../assets/img/icons/eye.svg" alt="Image" />
                  </a>
                  <router-link v-if="canEdit" :to="`/edit-sales-return/${returnItem.id}`" title="Edit">
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </router-link>
                  <a v-if="canDelete" class="delete-btn" data-bs-toggle="offcanvas" href="#deletePopup" role="button"
                    @click="$emit('delete-return', returnItem.id)">
                    <img src="../../../assets/img/icons/close.svg" alt="Image" />
                  </a>
                </div>
              </td>
            </tr>

            <tr v-if="!saleReturnStore.loading && saleReturns.length === 0">
              <td colspan="10" class="text-center text-muted py-3">No sale returns found.</td>
            </tr>
          </tbody>
        </table>

        <div v-if="saleReturnStore.loading" class="text-center py-4">
          <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
          <span class="ms-2">Loading sale returns...</span>
        </div>
      </div>
    </div>
  </div>

  <!-- Pagination -->
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
import { useSaleReturnStore } from "@/stores/saleReturnStore";
import { useUserStore } from "@/stores/userStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCustomerStore } from "@/stores/customerStore";
import { useSettingStore } from "@/stores/settingStore";

const saleReturnStore = useSaleReturnStore();
const userStore = useUserStore();
const warehouseStore = useWarehouseStore();
const customerStore = useCustomerStore();
const settingStore = useSettingStore();

onMounted(async () => {
  await Promise.all([
    warehouseStore.fetchWarehouses(),
    customerStore.fetchCustomers(),
    saleReturnStore.fetchSaleReturns(),
  ]);
});

const saleReturns = computed(() => saleReturnStore.saleReturns);
const currencySymbol = computed(() => settingStore.currencySymbol ?? "$");
const warehouseMap = computed(() => warehouseStore.warehouseMap);
const customerMap = computed(() => customerStore.customerMap);

// Permissions
const canView = computed(() => userStore.userPermissions.includes("SALE_RETURN_VIEW"));
const canEdit = computed(() => userStore.userPermissions.includes("SALE_RETURN_EDIT"));
const canDelete = computed(() => userStore.userPermissions.includes("SALE_RETURN_DELETE"));
</script>