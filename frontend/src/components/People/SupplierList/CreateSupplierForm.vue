<template>
  <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-xl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="createModalLabel">
            {{ isEditMode ? "Update Supplier" : "Create Supplier" }}
          </h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close" @click="resetForm">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="row">
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Supplier Name *</label>
                  <input v-model="form.name" type="text"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Supplier Name" required />
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Email</label>
                  <input v-model="form.email" type="email"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Email" />
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Phone Number</label>
                  <input v-model="form.phone" type="text"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Phone Number" />
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Country</label>
                  <input v-model="form.country" type="text"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Country" />
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">City</label>
                  <input v-model="form.city" type="text"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter City" />
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Tax Number</label>
                  <input v-model="form.taxNumber" type="text"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Tax Number" />
                </div>
              </div>
              <div class="col-12">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Address</label>
                  <textarea v-model="form.address" rows="3"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Address"></textarea>
                </div>
              </div>
              <div class="col-12">
                <button type="submit" class="btn style-five w-100 d-block" :disabled="supplierStore.isSubmitting">
                  {{ supplierStore.isSubmitting
                  ? "Please wait..."
                  : isEditMode
                  ? "Update Supplier"
                  : "Create Supplier" }}
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, computed, watch } from "vue";
import { storeToRefs } from "pinia";
import { useSupplierStore } from "@/stores/supplierStore";
import type { CreateSupplierRequest, UpdateSupplierRequest } from "@/types/Supplier";

const supplierStore = useSupplierStore();
const { selectedSupplier } = storeToRefs(supplierStore);

const isEditMode = computed(() => !!selectedSupplier.value?.id);

const form = reactive<CreateSupplierRequest & UpdateSupplierRequest>({
  name: "",
  email: "",
  phone: "",
  country: "",
  city: "",
  address: "",
  taxNumber: "",
});

watch(
  selectedSupplier,
  (supplier) => {
    if (supplier) {
      form.name = supplier.name;
      form.email = supplier.email || "";
      form.phone = supplier.phone || "";
      form.country = supplier.country || "";
      form.city = supplier.city || "";
      form.address = supplier.address || "";
      form.taxNumber = supplier.taxNumber || "";
    }
  },
  { immediate: true }
);

const resetForm = () => {
  form.name = "";
  form.email = "";
  form.phone = "";
  form.country = "";
  form.city = "";
  form.address = "";
  form.taxNumber = "";
  supplierStore.selectedSupplier = null;
};

const handleSubmit = async () => {
  try {
    if (isEditMode.value && selectedSupplier.value) {
      await supplierStore.editSupplier(selectedSupplier.value.id, form);
    } else {
      await supplierStore.addSupplier(form);
    }

    resetForm();

    const modalEl = document.getElementById("createModal");
    if (modalEl) {
      const modal = (window as any).bootstrap.Modal.getInstance(modalEl);
      modal?.hide();
    }
  } catch (e) {
    console.error("Supplier submit failed", e);
  }
};
</script>