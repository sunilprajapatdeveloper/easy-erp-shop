<template>
  <div
    class="modal fade"
    id="filterModal"
    tabindex="-1"
    aria-labelledby="filterModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg modal-dialog-centered">
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
            <div class="row">
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10"
                    >Product Name</label
                  >
                  <input
                    type="text"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Product Name"
                  />
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Category</label>
                  <select class="bg_ash border-0 rounded-1 fs-14 text-optional">
                    <option value="0">Electronics</option>
                    <option value="1">Clothing</option>
                    <option value="2">Accessories</option>
                  </select>
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10"
                    >Product Code</label
                  >
                  <input
                    type="text"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Product Code"
                  />
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Brand</label>
                  <select class="bg_ash border-0 rounded-1 fs-14 text-optional">
                    <option value="0">Choose Brand</option>
                    <option value="1">Brand One</option>
                    <option value="2">Brand Two</option>
                  </select>
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">Price</label>
                  <input
                    type="number"
                    class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                    placeholder="Enter Price"
                  />
                </div>
              </div>
              <div class="col-lg-6">
                <div class="form-group mb-15">
                  <label class="d-block fs-14 text-black mb-10">
                    Select Availability
                  </label>
                  <select class="bg_ash border-0 rounded-1 fs-14 text-optional">
                    <option value="1">Instock</option>
                    <option value="2">Out Of Stock</option>
                  </select>
                </div>
              </div>
              <div class="col-lg-6 mb-md-15">
                <button
                  type="button"
                  class="btn style-four w-100 d-block"
                  data-bs-dismiss="modal"
                >
                  Cancel
                </button>
              </div>
              <div class="col-lg-6">
                <button type="button" class="btn style-five w-100 d-block">
                  Filter
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FilterModal",
};
</script>