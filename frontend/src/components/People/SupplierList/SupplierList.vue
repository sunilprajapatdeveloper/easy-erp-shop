<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" />
                  <label class="form-check-label">
                    DATE
                    <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
                  </label>
                </div>
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                REFERENCE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                FROM WAREHOUSE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                TO WAREHOUSE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                TOTAL ITEMS
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                GRAND TOTAL
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                STATUS
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="supplier in suppliers" :key="supplier.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input class="form-check-input" type="checkbox" />
                    <label class="form-check-label text-optional"></label>
                  </div>
                  <span class="text-optional fs-14 ms-2">
                    {{ formatDate(supplier.createdAt) }}
                  </span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ supplier.externalCode || supplier.id }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ supplier.country || '-' }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ supplier.city || '-' }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ supplier.phone || '-' }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ supplier.email || '-' }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <!-- <span class="badge fs-14 fw-normal" :class="supplier.isActive ? 'badge-success' : 'badge-danger'">
                  {{ supplier.isActive ? 'Active' : 'Inactive' }}
                </span> -->
                <span class="badge fs-14 fw-normal" :class="'badge-success'">
                  Active
                </span>
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a href="javascript:void(0)" title="View" data-bs-toggle="modal" data-bs-target="#detailsModal"
                    @click="selectSupplier(supplier)">
                    <img src="../../../assets/img/icons/eye.svg" />
                  </a>
                  <a href="javascript:void(0)" title="Edit" data-bs-toggle="modal" data-bs-target="#createModal"
                    @click="selectSupplier(supplier)">
                    <img src="../../../assets/img/icons/edit.svg" />
                  </a>
                  <a class="delete-btn" data-bs-toggle="offcanvas" href="#deletePopup"
                    @click="deleteSupplier(supplier.id)">
                    <img src="../../../assets/img/icons/close.svg" />
                  </a>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useSupplierStore } from "@/stores/supplierStore";
import type { Supplier } from "@/types/Supplier";

const supplierStore = useSupplierStore();
const { suppliers } = storeToRefs(supplierStore);

onMounted(() => {
  supplierStore.fetchSuppliers();
});

const selectSupplier = (supplier: Supplier) => {
  supplierStore.selectedSupplier = supplier;
};

const deleteSupplier = async (id: number) => {
  await supplierStore.removeSupplier(id);
};

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString();
};
</script>