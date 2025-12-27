<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="Backup" />
    <div class="row gx-0 mb-30">
      <div class="col-xxl-5 col-md-6">
        <div class="filter-left d-flex align-items-center flex-wrap">
          <div
            class="bg-white pt-3 pb-3 px-4 d-flex justify-content-between align-items-center w-100"
          >
            <h6 class="fs-14 text-optional fw-normal mb-0">
              Backup File
              <span class="text-title fw-semibold">
                Location storage/software/Recent/Backup
              </span>
            </h6>
            <router-link to="/" class="fw-semibold text-purple fs-14 lh-1">
              Edit File Location
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-xxl-7 col-md-6">
        <div
          class="filter-right d-flex align-items-center justify-content-md-end flex-wrap"
        >
          <a
            href="javascript:void(0)"
            class="btn style-one"
            data-bs-toggle="modal"
            data-bs-target="#createModal"
          >
            Generate Backup
            <img src="../../assets/img/icons/add-circle.svg" alt="Image" />
          </a>
        </div>
      </div>
    </div>
    <BackupContent />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <div
    class="delete-popup offcanvas offcanvas-end border-0"
    tabindex="-1"
    id="deletePopup"
  >
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Your product is deleted from the list.
        </span>
      </div>
    </div>
  </div>
  <BackupContentModal />
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import BackupContent from "../../components/Settings/Backup/BackupContent.vue";
import BackupContentModal from "../../components/Settings/Backup/BackupContentModal.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "BackupPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    BackupContent,
    BackupContentModal,
    MainFooter,
  },
});
</script>

<style lang="scss" scoped>
.filter-left {
  button {
    margin-right: 10px;
  }
  .search-area {
    input {
      padding-left: 20px;
      &::placeholder {
        opacity: 0.8;
      }
    }
  }
}
.filter-right {
  a {
    margin-right: 10px;
    &:last-child {
      margin-right: 0;
    }
  }
  div,
  button {
    margin-right: 10px;
  }
}

@media only screen and (max-width: 767px) {
  .filter-left {
    .btn {
      margin-bottom: 15px;
    }
  }
  .filter-right {
    a,
    div,
    button {
      margin-bottom: 10px;
    }
  }
}

@media only screen and (min-width: 768px) and (max-width: 991px) {
  .filter-right {
    width: calc(100% + 20px);
    margin-left: -20px;
  }
}

@media only screen and (max-width: 991px) {
  .filter-left {
    margin-bottom: 20px;
  }
}

@media only screen and (min-width: 992px) {
  .filter-left {
    .search-area {
      width: 320px;
    }
  }
}

@media only screen and (min-width: 1400px) {
  .filter-left {
    button {
      margin-right: 15px;
    }
    .search-area {
      width: 320px;
    }
  }
  .filter-right {
    a,
    div,
    button {
      margin-right: 15px;
    }
  }
}
</style>