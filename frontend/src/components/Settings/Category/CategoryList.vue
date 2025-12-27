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
                    CATEGORY CODE
                    <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
                  </label>
                </div>
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                CATEGORY NAME
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0 text-end"
                v-if="hasAnyPermission(['CATEGORY_EDIT', 'CATEGORY_DELETE'])">
                ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="category in categoryStore.categories" :key="category.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input class="form-check-input" type="checkbox" :id="`check-${category.id}`" />
                    <label class="form-check-label text-optional" :for="`check-${category.id}`"></label>
                  </div>
                  <span class="text-optional fs-14 ms-2">{{ category.code }}</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ category.name }}
              </td>
              <td class="shadow-none lh-1 text-end pe-0" v-if="hasAnyPermission(['CATEGORY_EDIT', 'CATEGORY_DELETE'])">
                <div class="button-group d-flex flex-wrap align-items-center justify-content-end">
                  <a href="javascript:void(0)" title="Edit" data-bs-toggle="modal" data-bs-target="#createModal"
                    @click="handleEdit(category)" v-if="hasPermission('CATEGORY_EDIT')">
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </a>
                  <a class="delete-btn" data-bs-toggle="offcanvas" href="#deletePopup" role="button"
                    aria-controls="deletePopup" @click="handleDelete(category.id)" v-if="hasPermission('CATEGORY_DELETE')">
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
import { onMounted, defineEmits } from 'vue';
import { useCategoryStore } from '@/stores/categoryStore';
import type { Category } from "@/types/Category";
import { useUserStore } from "@/stores/userStore";

const emit = defineEmits<{
  (e: 'edit-category', category: Category): void;
  (e: 'delete-category', id: number): void;
}>();

const categoryStore = useCategoryStore();
const userStore = useUserStore();

const hasPermission = (permission: string): boolean =>
  userStore.userPermissions.includes(permission);

const hasAnyPermission = (permissions: string[]): boolean =>
  permissions.some((p) => hasPermission(p));

onMounted(() => {
  categoryStore.fetchCategories();
});

const handleEdit = (category: Category) => {
  emit("edit-category", category);
};

const handleDelete = (id: number) => {
  emit("delete-category", id);
};
</script>

<style>
.table-responsive {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}
</style>