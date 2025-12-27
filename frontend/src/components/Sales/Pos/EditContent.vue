<template>
  <div class="modal fade" id="filterModal" tabindex="-1" aria-labelledby="filterModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="filterModalLabel">Edit</h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="row">
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Discount</label>
                  <input type="text" v-model="discount"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Discount Amount" />
                </div>
              </div>

              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Tax Percentage</label>
                  <input type="text" v-model="orderTax"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Tax Percent" />
                </div>
              </div>

              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Shipping Cost</label>
                  <input type="text" v-model="shippingCost"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Shipping Cost" />
                </div>
              </div>

              <div class="col-12">
                <button type="submit" class="btn style-five w-100 d-block">
                  Submit
                </button>
              </div>
            </div>
          </form>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { usePosStore } from "@/stores/posStore";

export default defineComponent({
  name: "EditContent",
  setup() {
    const posStore = usePosStore();

    const submitForm = () => {
      // Optional: Validate or parse values
      // Close the modal manually if needed
      const modalEl = document.getElementById("filterModal");
      if (modalEl && window.bootstrap?.Modal) {
        const modal = window.bootstrap.Modal.getInstance(modalEl);
        modal?.hide();
      }
    };

    return {
      discount: posStore.discount,
      orderTax: posStore.orderTax,
      shippingCost: posStore.shippingCost,
      submitForm,
    };
  },
});
</script>