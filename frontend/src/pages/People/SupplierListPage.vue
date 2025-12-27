<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Supplier List" />
    <FilterContent />
    <FilterModal />
    <SupplierList />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Your product is deleted from the list.
        </span>
      </div>
    </div>
  </div>

  <SupplierDetails />
  <CreateSupplierForm />
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import FilterContent from "../../components/People/SupplierList/FilterContent.vue";
import FilterModal from "../../components/People/SupplierList/FilterModal.vue";
import SupplierList from "../../components/People/SupplierList/SupplierList.vue";
import SupplierDetails from "../../components/People/SupplierList/SupplierDetails.vue";
import CreateSupplierForm from "../../components/People/SupplierList/CreateSupplierForm.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "SupplierListPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    FilterContent,
    FilterModal,
    SupplierList,
    SupplierDetails,
    CreateSupplierForm,
    MainFooter,
  },
});
</script>