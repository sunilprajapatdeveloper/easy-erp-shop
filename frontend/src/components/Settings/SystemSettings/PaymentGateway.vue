<template>
  <div class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-35 text-title fw-semibold">Payment Gateway</h6>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Stripe Key</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="*********************************************"
              required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Stripe Secret</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="Please leave this field if you haven’t changed it"
              required
            />
          </div>
        </div>
        <div class="col-12">
          <div class="checkbox style-four mb-30">
            <input class="form-check-input" type="checkbox" id="test_200" />
            <label class="form-check-label" for="test_200">
              Delete Stripe API Keys
            </label>
          </div>
        </div>
        <div class="col-lg-6">
          <button type="button" class="btn style-five">Change Settings</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PaymentGateway",
};
</script>