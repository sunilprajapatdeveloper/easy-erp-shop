<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="Shipment" />
    <div class="row gx-0 mb-30">
      <div class="col-md-6">
        <div class="filter-left d-flex align-items-center flex-wrap">
          <form action="#" class="search-area position-relative w-sm-100">
            <input
              type="text"
              placeholder="Search On This Table"
              class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white"
            />
            <button
              type="submit"
              class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2"
            >
              <img src="../../assets/img/icons/search.svg" alt="Image" />
            </button>
          </form>
        </div>
      </div>
      <div class="col-md-6">
        <div
          class="filter-right d-flex align-items-center justify-content-md-end flex-wrap"
        >
          <a href="#" class="btn style-three"> Excel </a>
          <a href="#" class="btn style-four"> Pdf </a>
        </div>
      </div>
    </div>
    <ShipmentList />
    <EditShipment />

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
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import ShipmentList from "../../components/Sales/Shipment/ShipmentList.vue";
import EditShipment from "../../components/Sales/Shipment/EditShipment.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "ShipmentPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    ShipmentList,
    EditShipment,
    MainFooter,
  },
});
</script>

<style lang="scss" scoped>
.filter-left {
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
    .search-area {
      width: 320px;
    }
  }
}
</style>