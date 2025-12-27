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
            Create Category
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
        <div class="modal-body">
          <form action="#">
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10"
                >Category Name</label
              >
              <input
                type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Product Name"
              />
            </div>
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Descriptions</label>
              <textarea
                cols="30"
                rows="10"
                placeholder="Add few words to describe the category..."
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
              ></textarea>
            </div>
            <button type="button" class="btn style-five w-100 d-block">
              Create Category
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CreateCategoryModal",
};
</script>