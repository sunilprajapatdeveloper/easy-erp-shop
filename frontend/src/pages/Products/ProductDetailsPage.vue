<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="Product Details" />
    <div class="row gx-0 mb-30 align-items-lg-end align-items-start">
      <div class="col-md-6 col-7">
        <div class="filter-left">
          <img src="../../assets/img/products/barcode.webp" alt="Barcode" />
        </div>
      </div>
      <div class="col-md-6 col-5">
        <div
          class="filter-right d-flex align-items-center justify-content-end flex-wrap"
        >
          <a
            class="btn style-five upload-btn px-xxl-6"
            onclick="window.print();"
          >
            Print
            <img src="../../assets/img/icons/download.svg" alt="Image" />
          </a>
        </div>
      </div>
    </div>
    <ProductDetails />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import ProductDetails from "../../components/Products/ProductDetails/ProductDetails.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "ProductDetailsPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    ProductDetails,
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