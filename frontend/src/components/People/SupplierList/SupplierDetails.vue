<template>
  <div
    class="modal fade"
    id="detailsModal"
    tabindex="-1"
    aria-labelledby="detailsModalLabel"
    aria-hidden="true"
  >
    <div
      class="modal-dialog modal-rl modal-dialog-centered modal-dialog-scrollable"
    >
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="detailsModalLabel">
            Supplier Details
          </h5>
          <button
            type="button"
            class="btn-close p-0"
            data-bs-dismiss="modal"
            aria-label="Close"
          >
            <img
              src="../../../assets/img/icons/close-circle-2.svg"
              alt="Image"
            />
          </button>
        </div>
        <div class="modal-body p-0">
          <ul class="details-list list-style">
            <li>
              <span class="fs-14 text-title">Supplier Code</span>
              <span class="fs-14 fw-bold text-title">S - 103</span>
            </li>
            <li>
              <span class="fs-14 text-title">Supplier Name</span>
              <span class="fs-14 fw-bold text-title">Solit IT</span>
            </li>
            <li>
              <span class="fs-14 text-title">Phone Number</span>
              <span class="fs-14 fw-bold text-title">9923 345 876</span>
            </li>
            <li>
              <span class="fs-14 text-title">Email Address</span>
              <span class="fs-14 fw-bold text-title">angela326@gmail.com</span>
            </li>
            <li>
              <span class="fs-14 text-title">Country</span>
              <span class="fs-14 fw-bold text-title">Australia</span>
            </li>
            <li>
              <span class="fs-14 text-title">City</span>
              <span class="fs-14 fw-bold text-title">Canberra</span>
            </li>
            <li>
              <span class="fs-14 text-title">Address</span>
              <span class="fs-14 fw-bold text-title">
                413 North Las Vegas, NV 89032
              </span>
            </li>
            <li>
              <span class="fs-14 text-title">Tax No</span>
              <span class="fs-14 fw-bold text-title">321 - 723</span>
            </li>
            <li>
              <span class="fs-14 text-title">Total Purchase Due</span>
              <span class="fs-14 fw-bold text-title">$ 3210.00</span>
            </li>
            <li>
              <span class="fs-14 text-title">Total Purchase Return Due</span>
              <span class="fs-14 fw-bold text-title">$ 00.00</span>
            </li>
          </ul>
        </div>
        <div class="modal-footer pt-20 pb-35">
          <a
            class="btn style-five upload-btn px-xxl-6 ms-auto"
            onclick="window.print();"
          >
            Print
            <img src="../../../assets/img/icons/download.svg" alt="Image" />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SupplierDetails",
};
</script>