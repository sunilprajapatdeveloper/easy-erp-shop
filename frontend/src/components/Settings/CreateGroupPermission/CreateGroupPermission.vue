<template>
  <div class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body p-xl-40">
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Role</label>
            <input type="text" class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="Enter Role" v-model="roleName" required />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Role Description</label>
            <input type="text" class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="Role Description" v-model="roleDescription" />
          </div>
        </div>
      </div>

      <div class="row mt-30 justify-content-center">
        <div class="col-xl-4 col-lg-6 col-md-6" v-for="(group, index) in permissionGroups" :key="index">
          <div class="group-checkbox mb-50">
            <h6 class="fs-14 text-white lh-1 fw-medium">
              {{ formatGroupName(group.name) }}
            </h6>
            <div class="row ps-xxl-2">
              <div class="col-md-6" v-for="(permission, idx) in group.children" :key="idx">
                <div class="checkbox style-four mb-30">
                  <input class="form-check-input" type="checkbox" :id="`perm_${permission.id}`" :value="permission.id"
                    v-model="selectedPermissions" />
                  <label class="form-check-label fs-14" :for="`perm_${permission.id}`">
                    {{ formatPermission(permission.name) }}
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <button class="btn style-five" @click="handleCreateRole">
        Create Role
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRoleStore } from '@/stores/roleStore';
import { PERMISSIONS } from '@/constants/PERMISSIONS';
import { useRouter } from 'vue-router';

const roleStore = useRoleStore();

const roleName = ref('');
const roleDescription = ref('');
const selectedPermissions = ref<number[]>([]);
const permissionGroups = PERMISSIONS;
const router = useRouter();

function formatGroupName(groupName: string): string {
  return groupName
    .replaceAll('_', ' ')
    .toLowerCase()
    .replace(/(^|\s)\S/g, (l) => l.toUpperCase());
}

function formatPermission(permission: string): string {
  return permission
    .replaceAll('_', ' ')
    .toLowerCase()
    .replace(/(^|\s)\S/g, (l) => l.toUpperCase());
}

async function handleCreateRole() {
  if (!roleName.value.trim()) {
    alert('Role name is required');
    return;
  }

  if (selectedPermissions.value.length === 0) {
    alert('Please select at least one permission');
    return;
  }

  try {
    await roleStore.addRole({
      name: roleName.value.trim(),
      description: roleDescription.value.trim(),
      permissionIds: selectedPermissions.value,
    });

    alert('Role created successfully');

    // Reset form
    roleName.value = '';
    roleDescription.value = '';
    selectedPermissions.value = [];
    
    router.push('/group-permission');
  } catch (err) {
    console.error('Failed to create role:', err);
    alert('Error creating role. Please check console.');
  }
}
</script>