<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="Create Quotation" />
    <div class="row mb-40">
      <div class="col-lg-4">
        <div class="form-group mb-25">
          <label class="d-block fs-14 text-black mb-2">Date</label>
          <input
            type="date"
            class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white"
          />
        </div>
      </div>
      <div class="col-lg-4">
        <div class="form-group mb-25">
          <label class="d-block fs-14 text-black mb-2">Customer</label>
          <select class="bg-white border-0 rounded-1 fs-14 text-optional">
            <option value="0">Victor James</option>
            <option value="1">Tony Satrk</option>
          </select>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="form-group mb-25">
          <label class="d-block fs-14 text-black mb-2">Warehouse</label>
          <select class="bg-white border-0 rounded-1 fs-14 text-optional">
            <option value="0">Select Warehouse</option>
            <option value="1">Warehouse 01</option>
            <option value="2">Warehouse 02</option>
          </select>
        </div>
      </div>
      <div class="col-12">
        <div class="form-group">
          <label class="d-block fs-14 text-black mb-2">Choose Product</label>
          <div class="search-area style-two position-relative w-100">
            <input
              type="text"
              placeholder="Scan / Search product by code"
              class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white"
            />
            <button
              type="submit"
              class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2"
            >
              <img src="../../assets/img/icons/search.svg" alt="Image" />
            </button>
          </div>
        </div>
      </div>
    </div>
    <SelectedProducts />
    <SubmitQuotation />

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
import SelectedProducts from "../../components/Quotations/CreateQuotation/SelectedProducts.vue";
import SubmitQuotation from "../../components/Quotations/CreateQuotation/SubmitQuotation.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "CreateQuotationPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    SelectedProducts,
    SubmitQuotation,
    MainFooter,
  },
});
</script>