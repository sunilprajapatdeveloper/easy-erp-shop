<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="check_all" />
                  <label class="form-check-label" for="check_all"> NAME </label>
                </div>
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                SHORT NAME
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                BASE UNIT
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                OPERATOR
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                OPERATION VALUE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th v-if="canEdit || canDelete" scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0 pe-0">
                ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="unit in units" :key="unit.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center product-item">
                  <div class="form-check checkbox style-three me-25">
                    <input class="form-check-input" type="checkbox" :id="'unit_' + unit.id" />
                    <label class="form-check-label" :for="'unit_' + unit.id"></label>
                  </div>
                  <span class="fs-14 fw-semibold text-optional">{{ unit.name }}</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ unit.shortName }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ unit.baseUnit }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ unit.operator }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ unit.operatorValue }}</td>
              <td v-if="canEdit || canDelete" class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a v-if="canEdit" href="javascript:void(0)" title="Edit" data-bs-toggle="modal" data-bs-target="#createModal"
                    @click="handleEdit(unit)">
                    <img src="../../../assets/img/icons/edit.svg" alt="Edit" />
                  </a>
                  <a v-if="canDelete" class="delete-btn" data-bs-toggle="offcanvas" href="#deletePopup" role="button"
                    aria-controls="deletePopup" @click="handleDelete(unit.id)">
                    <img src="../../../assets/img/icons/close.svg" alt="Delete" />
                  </a>
                </div>
              </td>
            </tr>
            <tr v-if="units.length === 0">
              <td colspan="6" class="text-center text-muted py-3">No units found.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, defineEmits, computed } from "vue";
import type { Unit } from "@/types/Unit";
import { useUnitStore } from "@/stores/unitStore";
import { storeToRefs } from "pinia";
import { useUserStore } from "@/stores/userStore";

const emit = defineEmits<{
  (e: "edit", data: Unit): void;
  (e: "delete", id: number): void;
}>();

const unitStore = useUnitStore();
const userStore = useUserStore();
const { units } = storeToRefs(unitStore);

const canEdit = computed(() => userStore.userPermissions.includes('UNIT_EDIT'));
const canDelete = computed(() => userStore.userPermissions.includes('UNIT_DELETE'));

onMounted(() => {
  unitStore.fetchUnits();
});

const handleEdit = (unit: Unit) => {
  emit("edit", unit);
};

const handleDelete = (id: number) => {
  emit("delete", id);
};
</script>