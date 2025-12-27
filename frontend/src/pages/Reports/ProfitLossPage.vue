<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="Profit & Loss" />
    <div class="row gx-lg-0 mb-30">
      <div class="col-xl-3 col-lg-6">
        <div class="filter-left d-flex align-items-center flex-wrap">
          <div class="form-group w-100 me-xl-4">
            <select
              class="bg-white h-55 text-title fs-14 border-0 rounded-1 d-block w-100"
            >
              <option value="1">Warehouse 01</option>
              <option value="2">Warehouse 02</option>
              <option value="3">Warehouse 03</option>
            </select>
          </div>
        </div>
      </div>
      <div class="col-xl-9 col-lg-6">
        <div
          class="filter-right d-flex align-items-center justify-content-lg-end flex-wrap w-100 ms-0"
        >
          <a href="#" class="btn style-three">Excel</a>
          <a href="#" class="btn style-four">Pdf</a>
        </div>
      </div>
    </div>
    <ProfitLoss />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import ProfitLoss from "../../components/Reports/ProfitLoss/ProfitLoss.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "ProfitLossPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    ProfitLoss,
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