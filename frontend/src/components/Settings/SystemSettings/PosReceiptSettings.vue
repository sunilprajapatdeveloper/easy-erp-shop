<template>
  <div class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-35 text-title fw-semibold">POS Receipt Settings</h6>
      <div class="row">
        <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
          <div class="checkbox style-four mb-40">
            <input class="form-check-input" type="checkbox" id="test_1" />
            <label class="form-check-label" for="test_1">
              Show Phone Number
            </label>
          </div>
        </div>
        <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
          <div class="checkbox style-four mb-40">
            <input class="form-check-input" type="checkbox" id="test_2" />
            <label class="form-check-label" for="test_2"> Show Address </label>
          </div>
        </div>
        <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
          <div class="checkbox style-four mb-40">
            <input class="form-check-input" type="checkbox" id="test_3" />
            <label class="form-check-label" for="test_3">
              Show Email Address
            </label>
          </div>
        </div>
        <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
          <div class="checkbox style-four mb-40">
            <input class="form-check-input" type="checkbox" id="test_4" />
            <label class="form-check-label" for="test_4"> Show Customer </label>
          </div>
        </div>
        <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
          <div class="checkbox style-four mb-40">
            <input class="form-check-input" type="checkbox" id="test_5" />
            <label class="form-check-label" for="test_5">
              Show Tax & Discounts
            </label>
          </div>
        </div>
        <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
          <div class="checkbox style-four mb-40">
            <input class="form-check-input" type="checkbox" id="test_6" />
            <label class="form-check-label" for="test_6"> Show Barcode </label>
          </div>
        </div>
        <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
          <div class="checkbox style-four mb-40">
            <input class="form-check-input" type="checkbox" id="test_7" />
            <label class="form-check-label" for="test_7">
              Show Note to Customer
            </label>
          </div>
        </div>
        <div class="col-12">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">
              Note To Customer
            </label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="Thanks for shopping with Us. Please Come Again."
            />
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
  name: "PosReceiptSettings",
};
</script>