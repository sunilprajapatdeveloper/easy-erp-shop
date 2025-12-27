<template>
  <div
    class="modal fade searchModal"
    id="searchModal"
    data-bs-backdrop="static"
    data-bs-keyboard="false"
    tabindex="-1"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <form action="#">
          <input
            type="text"
            class="form-control"
            placeholder="Search here...."
          />
          <button type="submit">
            <img src="../../../assets/img/icons/search.svg" alt="Close" />
          </button>
        </form>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
          aria-label="Close"
        >
          <img src="../../../assets/img/icons/close-circle.svg" alt="Close" />
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SearchModal",
};
</script>