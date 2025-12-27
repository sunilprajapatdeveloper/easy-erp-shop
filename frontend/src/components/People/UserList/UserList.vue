<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div v-if="canView" class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="test_1" />
                  <label class="form-check-label" for="test_1"> IMAGE </label>
                </div>
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                FIRST NAME
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                LAST NAME
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                USERNAME
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                EMAIL
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                PHONE
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                STATUS
                <img class="ms-2" src="../../../assets/img/icons/up-down-aroow.svg" alt="Image" />
              </th>
              <th scope="col" v-if="canView || canEdit || canDelete" style="min-width: 150px;"
                class="text-title fw-normal fs-14 pt-0 pe-0">ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex product-item">
                  <div class="form-check checkbox me-25">
                    <input class="form-check-input" type="checkbox" :id="`check_${user.id}`" />
                    <label class="form-check-label" :for="`check_${user.id}`"></label>
                  </div>
                  <img src="../../../assets/img/users/user-1.webp" alt="Image" />
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ user.firstname }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ user.lastname }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ user.username }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ user.email }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">{{ user.phone || '—' }}</td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span :class="['badge fs-14 fw-medium', user.status ? 'badge-success' : 'badge-danger']">
                  {{ user.status ? 'Active' : 'Inactive' }}
                </span>
              </td>
              <td v-if="canView || canEdit || canDelete" class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <!-- View -->
                  <a v-if="canView" href="javascript:void(0)" title="View" data-bs-toggle="modal"
                    data-bs-target="#detailsModal">
                    <img src="../../../assets/img/icons/eye.svg" alt="View" />
                  </a>
                  <!-- Edit -->
                  <a v-if="canEdit" href="javascript:void(0)" title="Edit" @click="handleEdit(user)">
                    <img src="../../../assets/img/icons/edit.svg" alt="Edit" />
                  </a>
                  <!-- Delete -->
                  <a v-if="canDelete" class="delete-btn" href="javascript:void(0)" @click="handleDelete(user.id)">
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
</template>

<script lang="ts">
import { defineComponent, onMounted, computed } from "vue";
import { useUserStore } from "@/stores/userStore";
import { useRouter } from 'vue-router';
// import { showPopup } from "@/components/Common/Popup.vue";

export default defineComponent({
  name: "UserList",
  setup() {
    const userStore = useUserStore();

    onMounted(() => {
      userStore.fetchUsers();
    });

    const users = computed(() => userStore.users);
    const permissions = computed(() => userStore.userPermissions);

    const canView = computed(() => permissions.value.includes("USER_VIEW"));
    const canEdit = computed(() => permissions.value.includes("USER_EDIT"));
    const canDelete = computed(() => permissions.value.includes("USER_DELETE"));

    const router = useRouter();

    // Edit user handler
    const handleEdit = (user: { id: number }) => {
      router.push(`/update-user/${user.id}`);
    };

    // Delete user handler
    const handleDelete = async (id: number) => {
      if (!confirm("Are you sure you want to delete this user?")) return;
      try {
        await userStore.removeUser(id);
        alert('User deleted successfully!');
        // showPopup("success", "User deleted successfully!");
      } catch (err: any) {
        console.log(err.message);
        // showPopup("error", err?.response?.data?.error || "Failed to delete user.");
      }
    };

    return {
      users,
      canView,
      canEdit,
      canDelete,
      handleEdit,
      handleDelete,
    };
  },
});
</script>

<style>
.table-responsive {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}
</style>
