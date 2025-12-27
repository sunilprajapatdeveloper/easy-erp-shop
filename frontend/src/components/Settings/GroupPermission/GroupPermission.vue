<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                ROLE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                DESCRIPTION
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="role in roles" :key="role.id">
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                {{ role.name }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ role.description || '—' }}
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <router-link :to="`/edit-group-permission/${role.id}`" title="Edit">
                    <img src="../../../assets/img/icons/edit.svg" alt="Edit" />
                  </router-link>
                  <a class="delete-btn" data-bs-toggle="modal" href="#deletePopup" role="button" aria-controls="deletePopup"
                    @click.prevent="setRoleToDelete(role.id)">
                    <img src="../../../assets/img/icons/close.svg" alt="Delete" />
                  </a>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deletePopup" tabindex="-1" aria-labelledby="deletePopupLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0">
              <div class="modal-header">
                <h5 class="modal-title text-danger" id="deletePopupLabel">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" />
              </div>
              <div class="modal-body">
                Are you sure you want to delete this role?
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-danger" @click="confirmDeleteRole">
                  Yes, Delete
                </button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- <div class="row pb-45 align-items-center">
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
  </div> -->
</template>

<script setup>
import * as bootstrap from 'bootstrap';
import { onMounted, ref, watch } from "vue";
import { useRoleStore } from "@/stores/roleStore";

const roleStore = useRoleStore();
const roles = ref([]);
const roleToDeleteId = ref(null);

// Fetch roles on mount
onMounted(async () => {
  await roleStore.fetchRoles();
  roles.value = roleStore.roles;
});

// Keep roles in sync with the store
watch(
  () => roleStore.roles,
  (newRoles) => {
    roles.value = newRoles;
  },
  { immediate: true }
);

// Prepare the ID to delete
const setRoleToDelete = (id) => {
  roleToDeleteId.value = id;
};

// Confirm delete
const confirmDeleteRole = async () => {
  if (roleToDeleteId.value !== null) {
    await roleStore.removeRole(roleToDeleteId.value);
    roleToDeleteId.value = null;

    // Close the Bootstrap modal
    const modalEl = document.getElementById("deletePopup");
    if (modalEl) {
      const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
      modalInstance.hide();
    }
  }
};
</script>