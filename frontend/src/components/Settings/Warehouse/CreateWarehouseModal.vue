<template>
  <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="createModalLabel">Create Warehouse</h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Close" />
          </button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="row">
              <!-- Warehouse Name -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>Warehouse Name</label>
                  <input v-model="form.name" type="text" class="form-control" placeholder="Enter Warehouse Name"
                    required />
                </div>
              </div>

              <!-- Phone -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>Phone</label>
                  <input v-model="form.phone" type="text" class="form-control" placeholder="999 234 567" />
                </div>
              </div>

              <!-- Email -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>Email</label>
                  <input v-model="form.email" type="email" class="form-control" placeholder="Enter Email" />
                </div>
              </div>

              <!-- City -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>City</label>
                  <input v-model="form.city" type="text" class="form-control" placeholder="Enter City" required />
                </div>
              </div>

              <!-- State -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>State</label>
                  <input v-model="form.state" type="text" class="form-control" placeholder="Enter State" />
                </div>
              </div>

              <!-- Country -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>Country</label>
                  <input v-model="form.country" type="text" class="form-control" placeholder="Enter Country" required />
                </div>
              </div>

              <!-- ZIP Code -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>ZIP Code</label>
                  <input v-model="form.zipCode" type="text" class="form-control" placeholder="Enter ZIP Code" />
                </div>
              </div>

              <!-- Address Line 1 -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>Address Line 1</label>
                  <input v-model="form.addressLine1" type="text" class="form-control"
                    placeholder="Enter Address Line 1" />
                </div>
              </div>

              <!-- Address Line 2 -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>Address Line 2</label>
                  <input v-model="form.addressLine2" type="text" class="form-control"
                    placeholder="Enter Address Line 2" />
                </div>
              </div>

              <!-- Timezone -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>TimeZone</label>
                  <input v-model="form.timezone" type="text" class="form-control" placeholder="Enter TimeZone" />
                </div>
              </div>

              <!-- Invoice Prefix -->
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label>Invoice Prefix</label>
                  <input v-model="form.invoicePrefix" type="text" class="form-control"
                    placeholder="Enter Invoice Prefix" />
                </div>
              </div>

              <!-- Submit -->
              <div class="col-12">
                <button type="submit" class="btn style-five w-100" :disabled="loading">
                  <span v-if="loading">Creating...</span>
                  <span v-else>Submit</span>
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useUserStore } from "@/stores/userStore";
import type { CreateWarehouseRequest } from "@/types/Warehouse";

const warehouseStore = useWarehouseStore();
const userStore = useUserStore();
const loading = ref(false);

const form = reactive<CreateWarehouseRequest>({
  name: "",
  phone: "",
  email: "",
  city: "",
  state: "",
  country: "",
  zipCode: "",
  addressLine1: "",
  addressLine2: "",
  timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
  invoicePrefix: "WH-",
  companyId: userStore.currentUser!.companyId,
  createdBy: userStore.currentUser?.id,
  currencyId: 1,
});

const handleSubmit = async () => {
  try {
    loading.value = true;
    const created = await warehouseStore.addWarehouse(form);
    alert(`Warehouse "${created.name}" created successfully!`);

    // Reset form
    Object.assign(form, {
      name: "",
      phone: "",
      email: "",
      city: "",
      state: "",
      country: "",
      zipCode: "",
      addressLine1: "",
      addressLine2: "",
      timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
      invoicePrefix: "WH-",
    });
  } catch (err: any) {
    console.error("Failed to create warehouse:", err);
    alert(err.message ?? "Failed to create warehouse");
  } finally {
    loading.value = false;
  }
};
</script>
