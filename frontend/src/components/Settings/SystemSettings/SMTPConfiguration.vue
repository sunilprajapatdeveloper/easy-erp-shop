<template>
  <div class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-35 text-title fw-semibold">SMTP Configuration</h6>
      <div class="row">
        <div class="col-lg-4">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Host</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="Twilio"
              required
            />
          </div>
        </div>
        <div class="col-lg-4">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Port</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="990 321"
            />
          </div>
        </div>
        <div class="col-lg-4">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Username</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="hello_Orlo"
              required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Passwrod</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-30">
            <label class="d-block fs-14 text-black mb-2">Encryption</label>
            <input
              type="text"
              class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
              placeholder="tldts"
            />
          </div>
        </div>
        <div class="col-12">
          <button type="button" class="btn style-five">Change Settings</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SMTPConfiguration",
};
</script>