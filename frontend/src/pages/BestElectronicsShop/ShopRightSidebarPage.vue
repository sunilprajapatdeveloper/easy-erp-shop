<template>
  <MainHeader />
  <ResponsiveNavbar />
  <div class="bg_ash pt-100 pb-100">
    <div class="container">
      <BreadCrumb pageTitle="Shop" />
      <ProductsContent />
    </div>
  </div>
  <MainFooter />
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/BestElectronicsShop/Layouts/MainHeader.vue";
import ResponsiveNavbar from "../../components/BestElectronicsShop/Layouts/ResponsiveNavbar.vue";
import BreadCrumb from "../../components/BestElectronicsShop/Common/BreadCrumb.vue";
import ProductsContent from "../../components/BestElectronicsShop/ShopRightSidebar/ProductsContent.vue";
import MainFooter from "../../components/BestElectronicsShop/Layouts/MainFooter.vue";

export default defineComponent({
  name: "ShopRightSidebarPage",
  components: {
    MainHeader,
    ResponsiveNavbar,
    BreadCrumb,
    ProductsContent,
    MainFooter,
  },
});
</script>