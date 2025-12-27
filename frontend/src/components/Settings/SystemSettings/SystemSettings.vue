<template>
  <div class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-35 text-title fw-semibold">System General Settings</h6>

      <form @submit.prevent="handleSubmit">
        <div class="row">
          <!-- Company Name -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">Company Name</label>
              <input v-model="form.companyName" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="EasyERPShop Corporation"
                required />
            </div>
          </div>

          <!-- Phone -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">Company Phone Number</label>
              <input v-model="form.phone" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="990 321 52 36 21"
                required />
            </div>
          </div>

          <!-- Email -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">Default Email</label>
              <input v-model="form.email" type="email"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="hello@orlo.com"
                required />
            </div>
          </div>

          <!-- Registration Number -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">Registration Number</label>
              <input v-model="form.registrationNumber" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="REG-123456" />
            </div>
          </div>

          <!-- Country -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">Country</label>
              <input v-model="form.country" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="United States" />
            </div>
          </div>

          <!-- State -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">State</label>
              <input v-model="form.state" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="California" />
            </div>
          </div>

          <!-- City -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">City</label>
              <input v-model="form.city" type="text" class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                placeholder="Los Angeles" />
            </div>
          </div>

          <!-- Postal Code -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">Postal Code</label>
              <input v-model="form.postalCode" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="90001" />
            </div>
          </div>

          <!-- Timezone -->
          <div class="col-lg-4">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">Timezone</label>
              <input v-model="form.timezone" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="America/Los_Angeles" />
            </div>
          </div>

          <!-- Address -->
          <div class="col-lg-8">
            <div class="form-group mb-30">
              <label class="d-block fs-14 text-black mb-2">Address</label>
              <input v-model="form.address" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                placeholder="413 North Las Vegas, NV 89032" />
            </div>
          </div>

          <!-- Submit -->
          <div class="col-lg-6">
            <button type="submit" class="btn style-five" :disabled="loading">
              <span v-if="loading">Updating...</span>
              <span v-else>Change Settings</span>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted, ref } from "vue";
import { useCompanyStore } from "@/stores/companyStore";
import { useUserStore } from "@/stores/userStore";
import type { CompanyDetail, UpdateCompanyRequest } from "@/types/Company";

const companyStore = useCompanyStore();
const userStore = useUserStore();

const loading = ref(false);
const form = reactive<UpdateCompanyRequest>({
  companyName: "",
  phone: "",
  email: "",
  registrationNumber: "",
  country: "",
  state: "",
  city: "",
  address: "",
  postalCode: "",
  timezone: "",
  updatedBy: userStore.currentUser?.id ?? 0,
});

onMounted(async () => {
  const companyId = userStore.currentUser?.companyId;
  if (!companyId) return;

  const company: CompanyDetail | null = await companyStore.fetchCompanyDetail(companyId);
  if (company) {
    form.companyName = company.companyName;
    form.phone = company.phone;
    form.email = company.email;
    form.registrationNumber = company.registrationNumber ?? "";
    form.country = company.country ?? "";
    form.state = company.state ?? "";
    form.city = company.city ?? "";
    form.address = company.address ?? "";
    form.postalCode = company.postalCode ?? "";
    form.timezone = company.timezone ?? "";
  }
});

const handleSubmit = async () => {
  const companyId = userStore.currentUser?.companyId;
  if (!companyId) return;

  try {
    loading.value = true;
    await companyStore.updateCompany(companyId, form);
    alert("System settings updated successfully!");
  } catch (err: any) {
    console.error("Failed to update system settings:", err);
    alert(err.message ?? "Update failed");
  } finally {
    loading.value = false;
  }
};
</script>
