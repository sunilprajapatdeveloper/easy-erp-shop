<template>
  <div class="row mb-40">
    <div class="col-xxl-10 col-lg-8 pe-xxl-8">
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Order Tax</label>
            <input
              type="number"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
              placeholder="0"
            />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center"
            >
              %
            </span>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Discount</label>
            <input
              type="number"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
              placeholder="0"
            />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16"
            >
              $
            </span>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Shipping Cost</label>
            <input
              type="number"
              class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
              placeholder="0"
            />
            <span
              class="percent-sign position-absolute rounded-1 text-center d-flex flex-column justify-content-center fw-semibold fs-16"
            >
              $
            </span>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-25">
            <label class="d-block fs-14 text-black mb-2">Status</label>
            <select class="bg-white border-0 rounded-1 fs-14 text-optional">
              <option value="0">Sent</option>
              <option value="1">Recieved</option>
            </select>
          </div>
        </div>
      </div>
    </div>
    <div class="col-xxl-2 col-lg-4">
      <div class="card border-0 rounded-1 w-xxl-5 pt-12 pb-12 mb-md-25">
        <table class="table style-two">
          <tbody>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">
                ORDER TAX :
              </th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                $00.00
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">
                DISCOUNT :
              </th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                $150.00
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-normal">
                SHIPPING :
              </th>
              <td class="fs-14 fw-semibold lh-1 text-optional text-end">
                $50.00
              </td>
            </tr>
            <tr>
              <th scope="row" class="fs-14 text-title lh-1 ls-1 fw-semibold">
                GRAND TOTAL :
              </th>
              <td class="fs-14 fw-bold lh-1 text-purple text-end">$200.00</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="col-12">
      <div class="form-group mb-25">
        <label class="d-block fs-14 text-black mb-2">Notes</label>
        <textarea
          cols="30"
          rows="10"
          placeholder="Add a note"
          class="d-block w-100 bg-white border-0 rounded-1 resize-none fs-14 text-title"
        ></textarea>
      </div>
    </div>
    <div class="col-xl-4">
      <button
        class="btn style-one d-inline-block transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16 mb-20"
        type="submit"
      >
        Submit Quotation
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "SubmitQuotation",
};
</script>