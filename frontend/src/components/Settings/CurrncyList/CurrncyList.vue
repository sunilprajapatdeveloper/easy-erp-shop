<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="test_1" />
                  <label class="form-check-label" for="test_1">
                    CURRENCY CODE
                  </label>
                </div>
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                CURRENCY NAME
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                SYMBOL
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th v-if="canEdit || canDelete" scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0 pe-0">
                ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="currency in currencyStore.currencies" :key="currency.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center product-item">
                  <div class="form-check checkbox style-three me-25">
                    <input class="form-check-input" type="checkbox" id="test_32" />
                    <label class="form-check-label" for="test_32"> </label>
                  </div>
                  <span class="fs-14 fw-semibold text-optional">{{ currency.code }}</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ currency.name }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ currency.symbol }}</td>
              <td v-if="canEdit || canDelete" class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a v-if="canEdit" @click="handleEdit(currency)" data-bs-toggle="modal" data-bs-target="#createModal">
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </a>
                  <a v-if="canDelete" @click="handleDelete(currency.id)" class="delete-btn" data-bs-toggle="offcanvas"
                    href="#deletePopup">
                    <img src="../../../assets/img/icons/close.svg" alt="Image" />
                  </a>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <!-- <div class="row pb-45 align-items-center">
    <div class="col-sm-6">
      <div
        class="d-flex flex-wrap align-items-center justify-content-center justify-content-sm-start page-unit"
      >
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
            <img
              src="../../../assets/img/icons/left-arrow-purple.svg"
              alt="Image"
            />
          </a>
        </li>
        <li><a href="#" class="active">1</a></li>
        <li><a href="#">2</a></li>
        <li><a href="#">3</a></li>
        <li>
          <a href="#">
            <img
              src="../../../assets/img/icons/right-arrow-purple.svg"
              alt="Image"
            />
          </a>
        </li>
      </ul>
    </div>
  </div> -->
</template>

<script setup lang="ts">
import { onMounted, defineEmits, computed } from "vue";
import { useCurrencyStore } from "@/stores/currencyStore";
import { useUserStore } from "@/stores/userStore";
import type { Currency } from "@/types/Currency";

const currencyStore = useCurrencyStore();
const userStore = useUserStore();

const canEdit = computed(() => userStore.userPermissions.includes('CURRENCY_EDIT'));
const canDelete = computed(() => userStore.userPermissions.includes('CURRENCY_DELETE'));

const emit = defineEmits<{
  (e: "edit-currency", currency: Currency): void;
  (e: "delete-currency", id: number): void;
}>();

onMounted(() => {
  currencyStore.fetchCurrencies();
});

const handleEdit = (currency: Currency) => emit("edit-currency", currency);
const handleDelete = (id: number) => emit("delete-currency", id);
</script>