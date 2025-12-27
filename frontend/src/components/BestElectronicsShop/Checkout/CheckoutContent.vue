<template>
  <div class="checkout-wrap pt-100">
    <div class="row gx-xxl-6">
      <div class="col-xl-8">
        <h5 class="fs-20 fw-semibold mb-20">Billing Details</h5>
        <form class="checkout-form pb-100" action="#">
          <div class="row">
            <div class="col-md-6">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">First Name</label>
                <input type="text" class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                  placeholder="Enter First Name" required />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">Last Name</label>
                <input type="text" class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                  placeholder="Enter Last Name" required />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">Email</label>
                <input type="email" class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                  placeholder="Enter Email Address" required />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">
                  Phone Number
                </label>
                <input type="number" class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                  placeholder="Enter Phone No" required />
              </div>
            </div>
            <div class="col-12">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">
                  Country/Regiony
                </label>
                <select class="bg-white border-0 rounded-1 fs-14 text-optional">
                  <option value="0">United States</option>
                  <option value="1">United Kingdom</option>
                  <option value="2">Canada</option>
                </select>
              </div>
            </div>
            <div class="col-12">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">Street</label>
                <input type="number" class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                  placeholder="Enter Phone No" required />
              </div>
            </div>
            <div class="col-12">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">Town/City</label>
                <input type="number" class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                  placeholder="Enter Phone No" required />
              </div>
            </div>
            <div class="col-12">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">District</label>
                <select class="bg-white border-0 rounded-1 fs-14 text-optional">
                  <option value="0">Choose District</option>
                  <option value="1">United Kingdom</option>
                  <option value="2">Canada</option>
                </select>
              </div>
            </div>
            <div class="col-12">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">Post Code</label>
                <input type="number" class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                  placeholder="Enter Phone No" required />
              </div>
            </div>
            <div class="col-lg-12">
              <div class="form-group mb-15">
                <label class="d-block fs-14 text-black mb-2">
                  Additional Notes
                </label>
                <textarea cols="30" rows="10" placeholder="Add a note"
                  class="d-block w-100 bg-white border-0 rounded-1 resize-none fs-14 text-title"></textarea>
              </div>
            </div>
            <div class="col-12">
              <div class="form-group">
                <div class="form-check checkbox style-two">
                  <input name="gridCheck" value="I agree to the terms and privacy policy." class="form-check-input"
                    type="checkbox" id="gridCheck" required />
                  <label class="form-check-label mb-0 text-paragraph fs-14 fw-medium" for="gridCheck">
                    I accept all
                    <router-link to="/terms-conditions" class="text-purple">
                      Terms & Conditions
                    </router-link>
                  </label>
                  <div class="help-block with-errors gridCheck-error"></div>
                </div>
              </div>
            </div>
          </div>
        </form>
        <!-- Payment Method Selection -->
        <div class="select-payment-method">
          <h5 class="fs-20 fw-semibold mb-20">Payment Method</h5>

          <div class="card payment-method border-0 mb-15">
            <div class="card-body p-3">
              <div class="checkbox style-two">
                <input type="radio" id="card" name="paymentMethod" value="CARD" v-model="selectedMethod" />
                <label class="form-check-label fw-semibold" for="card">
                  Card Payment
                </label>
              </div>
            </div>
          </div>

          <div class="card payment-method border-0 mb-15">
            <div class="card-body p-3">
              <div class="checkbox style-two">
                <input type="radio" id="upi" name="paymentMethod" value="UPI" v-model="selectedMethod" />
                <label class="form-check-label fw-semibold" for="upi">
                  UPI
                </label>
              </div>
            </div>
          </div>

          <div class="card payment-method border-0 mb-15">
            <div class="card-body p-3">
              <div class="checkbox style-two">
                <input type="radio" id="cash" name="paymentMethod" value="CASH" v-model="selectedMethod" />
                <label class="form-check-label fw-semibold" for="cash">
                  Cash On Delivery
                </label>
              </div>
              <p class="text-optional ms-4 ps-2 mt-3">
                You'll pay with cash when the order is delivered to your
                address.
              </p>
            </div>
          </div>

          <!-- Conditionally Render Payment Gateway Component -->
          <div v-if="selectedMethod === 'CARD'">
            <PaymentCardGateway />
          </div>
          <div v-if="selectedMethod === 'UPI'">
            <PaymentUPIGateway />
          </div>
        </div>
      </div>
      <div class="col-xl-4">
        <div class="sidebar">
          <div class="card border-0 shadow-none">
            <YourOrder />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import YourOrder from "./YourOrder.vue";
import PaymentCardGateway from "./PaymentCardGateway.vue";
import PaymentUPIGateway from "./PaymentUPIGateway.vue";

export default defineComponent({
  name: "CheckoutContent",
  components: {
    YourOrder,
    PaymentCardGateway,
    PaymentUPIGateway,
  },
  setup() {
    const selectedMethod = ref("CARD");

    return {
      selectedMethod,
    };
  },
});
</script>

<style lang="scss" scoped>
@media only screen and (max-width: 991px) {
  .checkout-wrap {
    .table-responsive {
      margin: 0 -22px;

      table {
        tr {
          td {
            padding: 20px 22px;

            &:last-child {
              padding-right: 22px;
            }
          }
        }
      }
    }
  }
}
</style>
