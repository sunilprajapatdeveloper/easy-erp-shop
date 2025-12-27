<template>
  <form @submit.prevent="submit" class="pb-60">
    <div class="row">
      <div class="col-xxl-9 col-xl-8 col-lg-8 pe-xxl-6 mb-md-25">
        <div class="row gx-xxl-6">

          <!-- Firstname -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">First Name</label>
              <input v-model="user.firstname" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter First Name"
                required />
            </div>
          </div>

          <!-- Lastname -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Last Name</label>
              <input v-model="user.lastname" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter Last Name"
                required />
            </div>
          </div>

          <!-- Username -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Username</label>
              <input v-model="user.username" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter Username"
                required />
            </div>
          </div>

          <!-- Email -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Email</label>
              <input v-model="user.email" type="email"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter Email"
                required />
            </div>
          </div>

          <!-- Phone -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Phone</label>
              <input v-model="user.phone" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter Phone" />
            </div>
          </div>

          <!-- Role -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Role</label>
              <select v-model.number="user.roleId" class="bg-white border-0 rounded-1 fs-14 text-optional" required>
                <option disabled :value="0">Choose Role</option>
                <option v-for="role in roles" :key="role.id" :value="role.id">
                  {{ role.name }}
                </option>
              </select>
            </div>
          </div>

          <!-- Assigned Warehouses -->
          <div class="col-lg-12">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Assigned Warehouses</label>
              <Multiselect v-model="selectedWarehouses" :options="warehouses" :multiple="true" :track-by="'id'"
                :label="'name'" placeholder="Select Warehouses" :close-on-select="false" :clear-on-select="false"
                :preserve-search="true" :custom-label="warehouseLabel" />
            </div>
          </div>

          <!-- Default Warehouse -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Default Warehouse</label>
              <select v-model="user.defaultWarehouseId" class="bg-white border-0 rounded-1 fs-14 text-optional">
                <option :value="undefined">Choose Default</option>
                <option v-for="wh in selectedWarehouses" :key="wh.id" :value="wh.id">
                  {{ wh.name }}
                </option>
              </select>
            </div>
          </div>

          <!-- Submit -->
          <div class="col-12">
            <button class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16"
              type="submit">
              {{ isEditMode ? 'Update User' : 'Create User' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed, watch, getCurrentInstance } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "@/stores/userStore";
import { useRoleStore } from "@/stores/roleStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import type { CreateUserRequest, UpdateUserRequest } from "@/types/User";

import Multiselect from "vue-multiselect";
import "vue-multiselect/dist/vue-multiselect.css";
import { showPopup } from "@/components/Common/Popup.vue";

export default defineComponent({
  name: "CreateUser",
  components: { Multiselect },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const userStore = useUserStore();
    const roleStore = useRoleStore();
    const warehouseStore = useWarehouseStore();

    const userId = route.params.id ? Number(route.params.id) : null;
    const isEditMode = computed(() => !!userId);

    // Popup from plugin
    const { appContext } = getCurrentInstance()!;

    // Editable user model
    type EditableUser = Partial<CreateUserRequest> & { id?: number };
    const user = ref<EditableUser>({
      firstname: "",
      lastname: "",
      username: "",
      email: "",
      phone: "",
      roleId: 0,
      companyId: userStore.currentUser?.companyId ?? 0,
      warehouseIds: [],
      defaultWarehouseId: undefined,
    });

    // Multiselect bound model
    const selectedWarehouses = ref<{ id: number; name: string }[]>([]);
    const warehouseLabel = (wh: { id: number; name: string }) => wh.name;

    // Reset form helper
    const resetForm = () => {
      user.value = {
        firstname: "",
        lastname: "",
        username: "",
        email: "",
        phone: "",
        roleId: 0,
        companyId: userStore.currentUser?.companyId ?? 0,
        warehouseIds: [],
        defaultWarehouseId: undefined,
      };
      selectedWarehouses.value = [];
    };

    // Sync selectedWarehouses <-> user.warehouseIds
    watch(selectedWarehouses, (newVal) => {
      user.value.warehouseIds = newVal.map((w) => w.id);
      if (!user.value.defaultWarehouseId && newVal.length > 0) {
        user.value.defaultWarehouseId = newVal[0].id;
      }
    }, { immediate: true });

    // Load user if edit mode
    const loadUser = async () => {
      if (userId) {
        try {
          const fetched = await userStore.fetchUserById(userId);
          if (fetched) {
            user.value = { ...fetched };
            selectedWarehouses.value = warehouseStore.warehouses.filter(w =>
              user.value.warehouseIds?.includes(w.id)
            );
          }
        } catch (err: any) {
          console.error(err);
          showPopup("error", err?.response?.data?.error || "Failed to load user.");
        }
      }
    };

    const submit = async () => {
      try {
        if (
          user.value.defaultWarehouseId &&
          user.value.warehouseIds &&
          !user.value.warehouseIds.includes(user.value.defaultWarehouseId)
        ) {
          showPopup("error", "Default warehouse must be one of the assigned warehouses.");
          return;
        }

        if (isEditMode.value && userId) {
          await userStore.updateUser(userId, user.value as UpdateUserRequest);
          showPopup("success", "User updated successfully!");
          router.push('/user-list');
        } else {
          await userStore.addUser(user.value as CreateUserRequest);
          showPopup("success", "User created successfully!");
          resetForm();
        }
      } catch (err: any) {
        console.error(err);
        showPopup("error", err?.response?.data?.error || "Failed to save user.");
      }
    };

    const roles = computed(() => roleStore.roles);
    const warehouses = computed(() => warehouseStore.warehouses);

    onMounted(async () => {
      await Promise.all([roleStore.fetchRoles(), warehouseStore.fetchWarehouses()]);
      await loadUser();
    });

    return {
      user,
      selectedWarehouses,
      warehouseLabel,
      roles,
      warehouses,
      isEditMode,
      submit
    };
  },
});
</script>
