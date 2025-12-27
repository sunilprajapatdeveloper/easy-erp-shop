<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="Stock Report of: Camera" />
    <div class="card border-0 shadow-none rounded-1 mb-25">
      <div class="card-body p-xl-40">
        <div class="table-responsive">
          <table class="table text-nowrap align-middle mb-0">
            <thead>
              <tr>
                <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                  WAREHOUSE
                </th>
                <th scope="col" class="text-title fw-normal fs-14 pt-0">
                  QUANTITY
                </th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td
                  class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0"
                >
                  Warehouse 01
                </td>
                <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                  <span class="fs-14 fw-medium badge badge-success">200pc</span>
                </td>
              </tr>
              <tr>
                <td
                  class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0"
                >
                  Warehouse 02
                </td>
                <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                  <span class="fs-14 fw-medium badge badge-success">150pc</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <StockReportDetails />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import StockReportDetails from "../../components/Reports/StockReportDetails/StockReportDetails.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "StockReportDetailsPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    StockReportDetails,
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