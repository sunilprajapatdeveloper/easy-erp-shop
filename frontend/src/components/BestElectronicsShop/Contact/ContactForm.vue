<template>
  <div class="bg-white ptb-100">
    <div class="container">
      <h2 class="section-title text-center text-title mb-25">
        Send Us A Message Anytime
      </h2>
      <div class="contact_form">
        <form action="#">
          <div class="row">
            <div class="col-xl-10 offset-xl-1">
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="fs-14 text-title mb-2 d-block">
                      First name
                    </label>
                    <input
                      type="text"
                      placeholder="Enter First Name"
                      class="bg_ash border-0 rounded-1 fs-14 w-100 mb-15"
                    />
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="fs-14 text-title mb-2 d-block">
                      Last name
                    </label>
                    <input
                      type="text"
                      placeholder="Enter Last Name"
                      class="bg_ash border-0 rounded-1 fs-14 w-100 mb-15"
                    />
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="fs-14 text-title mb-2 d-block">Email</label>
                    <input
                      type="email"
                      placeholder="Enter Email Address"
                      class="bg_ash border-0 rounded-1 fs-14 w-100 mb-15"
                    />
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="fs-14 text-title mb-2 d-block">
                      Phone Number
                    </label>
                    <input
                      type="number"
                      placeholder="Enter Phone Number"
                      class="bg_ash border-0 rounded-1 fs-14 w-100 mb-15"
                    />
                  </div>
                </div>
                <div class="col-12">
                  <div class="form-group">
                    <label class="fs-14 text-title mb-2 d-block">
                      Your Message
                    </label>
                    <textarea
                      name="message"
                      id="message"
                      cols="30"
                      rows="10"
                      placeholder="Enter Your Message Here"
                      class="bg_ash border-0 rounded-1 fs-14 w-100 mb-15"
                    ></textarea>
                  </div>
                </div>
                <div class="col-md-9">
                  <div class="form-group">
                    <div class="form-check checkbox style-two">
                      <input
                        name="gridCheck"
                        value="I agree to the terms and privacy policy."
                        class="form-check-input"
                        type="checkbox"
                        id="gridCheck"
                        required
                      />
                      <label
                        class="form-check-label mb-0 text-paragraph fs-14 fw-medium"
                        for="gridCheck"
                      >
                        Save my name & password next time I message.
                      </label>
                    </div>
                  </div>
                </div>
                <div class="col-md-3 text-md-end">
                  <button
                    type="submit"
                    class="btn style-one text-white py-3 px-4"
                  >
                    Send Message Now
                    <img
                      src="../../../assets/img/icons/right-long-arrow.svg"
                      alt="Image"
                    />
                  </button>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ContactForm",
};
</script>