<template>
  <MainHeader />
  <ResponsiveNavbar />
  <div class="container-fluid px-xxl-0">
    <MainBanner />
    <CategoryWrap />
    <BrowseAllProducts />

    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/BestElectronicsShop/Layouts/MainHeader.vue";
import ResponsiveNavbar from "../../components/BestElectronicsShop/Layouts/ResponsiveNavbar.vue";
import MainBanner from "../../components/BestElectronicsShop/LandingPage/MainBanner.vue";
import CategoryWrap from "../../components/BestElectronicsShop/LandingPage/CategoryWrap.vue";
import BrowseAllProducts from "../../components/BestElectronicsShop/LandingPage/BrowseAllProducts.vue";
import MainFooter from "../../components/BestElectronicsShop/Layouts/MainFooter.vue";

export default defineComponent({
  name: "LandingPage",
  components: {
    MainHeader,
    ResponsiveNavbar,
    MainBanner,
    CategoryWrap,
    BrowseAllProducts,
    MainFooter,
  },
  mounted() {
    document.body.classList.add("bg_ash");
  },
});
</script>