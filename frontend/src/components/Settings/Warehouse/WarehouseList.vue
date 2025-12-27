<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="select_all" />
                  <label class="form-check-label" for="select_all">
                    NAME
                    <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Sort" />
                  </label>
                </div>
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                PHONE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Sort" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                EMAIL
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Sort" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                COUNTRY
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Sort" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                CITY
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Sort" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="warehouse in paginatedWarehouses" :key="warehouse.id">
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                {{ warehouse.name }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ warehouse.phone ?? "-" }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ warehouse.email ?? "-" }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ warehouse.country }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ warehouse.city }}
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a href="javascript:void(0)" class="" title="Settings"
                    @click.prevent="goToWarehouseSettings(warehouse.id)">
                    <img src="../../../assets/img/icons/setting.svg" alt="Settings" class="settings-icon" />
                  </a>
                </div>
              </td>
            </tr>
            <tr v-if="warehouses.length === 0">
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                No warehouses found.
              </td>
            </tr>
          </tbody>
        </table>
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
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useWarehouseStore } from "@/stores/warehouseStore";
import type { WarehouseDetail } from "@/types/Warehouse";

const warehouseStore = useWarehouseStore();
const router = useRouter();

const warehouses = ref<WarehouseDetail[]>([]);
const currentPage = ref(1);
const itemsPerPage = ref(10);

const totalPages = computed(() =>
  Math.ceil(warehouses.value.length / itemsPerPage.value)
);
const paginatedWarehouses = computed(() =>
  warehouses.value.slice(
    (currentPage.value - 1) * itemsPerPage.value,
    currentPage.value * itemsPerPage.value
  )
);

// Fetch warehouses and their full details
const fetchWarehouses = async () => {
  try {
    await warehouseStore.fetchWarehouses();
    const details: WarehouseDetail[] = [];
    for (const w of warehouseStore.warehouses) {
      const detail = await warehouseStore.fetchWarehouseDetail(w.id);
      details.push(detail);
    }
    warehouses.value = details;
  } catch (err) {
    console.error("Failed to fetch warehouses", err);
  }
};

onMounted(fetchWarehouses);

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};
const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++;
};
const goToPage = (page: number) => {
  currentPage.value = page;
};

const goToWarehouseSettings = (warehouseId: number) => {
  router.push({ path: `/warehouse-settings/${warehouseId}` });
};

const createWarehouse = () => {
  // Redirect to create warehouse page or open a modal
  console.log("Open create warehouse modal/page");
};
</script>

<style>
.settings-icon {
  filter: brightness(0);
  /* Makes the icon fully black */
  width: 20px;
  height: 20px;
}
</style>