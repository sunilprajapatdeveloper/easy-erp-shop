<template>
  <div
    class="modal fade"
    id="createModal"
    tabindex="-1"
    aria-labelledby="createModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-rl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
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
          <div class="fs-14 fs-medium text-center text-optional">
            <span class="text-green">Your backup is generated.</span> You will
            find your backup on
            <span class="text-title fw-semibold">
              /storage/app/public/backup
            </span>
            and save it to your pc.
          </div>
          <button
            type="button"
            class="btn style-one w-100 d-inline-block mt-4"
            data-bs-toggle="close"
          >
            Okay
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "BackupContentModal",
};
</script>