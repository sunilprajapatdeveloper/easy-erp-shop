<template>
  <div
    class="modal fade"
    id="filterModal"
    tabindex="-1"
    aria-labelledby="filterModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-rl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="filterModalLabel">Filter</h5>
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
        <div class="modal-body">
          <form action="#">
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Status</label>
              <select class="bg_ash border-0 rounded-1 fs-14 text-optional">
                <option value="0">Delivered</option>
                <option value="1">Pending</option>
                <option value="2">Cancelled</option>
              </select>
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Delivered To</label>
              <select class="bg_ash border-0 rounded-1 fs-14 text-optional">
                <option value="0">Choose Brand</option>
                <option value="1">Brand One</option>
                <option value="2">Brand Two</option>
              </select>
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Address</label>
              <textarea
                cols="30"
                rows="10"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Product Code"
              ></textarea>
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10"
                >Other Details</label
              >
              <textarea
                cols="30"
                rows="10"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Other Details"
              ></textarea>
            </div>
            <button type="button" class="btn style-five w-100 d-block">
              Filter
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "EditShipment",
};
</script>