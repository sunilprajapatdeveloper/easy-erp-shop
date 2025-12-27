<template>
  <div class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-35 text-title fw-semibold">SMS Configuration</h6>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">SMS Gateway</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="Twilio"
              required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Twilio_SID</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Twilio_Token</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Twilio_From</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              required
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
  name: "SMSConfiguration",
};
</script>