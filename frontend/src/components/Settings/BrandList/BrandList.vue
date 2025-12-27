<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="selectAll" />
                  <label class="form-check-label" for="selectAll">BRAND IMAGE</label>
                </div>
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                BRAND NAME
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Sort" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                BRAND DESCRIPTION
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Sort" />
              </th>
              <th v-if="canEdit || canDelete" scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">ACTION</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="brand in brandStore.brands" :key="brand.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center product-item">
                  <div class="form-check checkbox style-three me-25">
                    <input class="form-check-input" type="checkbox" :id="`brand_${brand.id}`" />
                    <label class="form-check-label" :for="`brand_${brand.id}`"></label>
                  </div>
                  <img v-if="brand.image" :src="brand.image" alt="Brand Image"
                    style="width: 40px; height: 40px; object-fit: cover; border-radius: 6px;" />
                  <span v-else class="shadow-none lh-1 fs-14 fw-normal text-paragraph">No image</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ brand.name }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ brand.description || '—' }}
              </td>
              <td v-if="canEdit || canDelete" class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a v-if="canEdit" href="javascript:void(0)" data-bs-toggle="modal" data-bs-target="#createModal"
                    @click="handleEdit(brand)">
                    <img src="../../../assets/img/icons/edit.svg" alt="Edit" />
                  </a>
                  <a v-if="canDelete" class="delete-btn" data-bs-toggle="offcanvas" href="#deletePopup" @click="handleDelete(brand.id)">
                    <img src="../../../assets/img/icons/close.svg" alt="Delete" />
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
import type { Brand } from "@/types/Brand";
import { useBrandStore } from "@/stores/brandStore";
import { useUserStore } from "@/stores/userStore";

const emit = defineEmits<{
  (e: "edit-brand", brand: Brand): void;
  (e: "delete-brand", id: number): void;
}>();

const brandStore = useBrandStore();
const userStore = useUserStore();

const canEdit = computed(() => userStore.userPermissions.includes('BRAND_EDIT'));
const canDelete = computed(() => userStore.userPermissions.includes('BRAND_DELETE'));

onMounted(() => {
  brandStore.fetchBrands();
});

const handleEdit = (brand: Brand) => {
  emit("edit-brand", brand);
};

const handleDelete = (id: number) => {
  emit("delete-brand", id);
};
</script>