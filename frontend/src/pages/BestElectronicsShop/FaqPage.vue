<template>
  <MainHeader />
  <ResponsiveNavbar />
  <div class="bg_ash pt-57 pb-100">
    <div class="container">
      <BreadCrumb pageTitle="FAQ" class="mb-80" />
      <FaqAccordion />
    </div>
  </div>
  <MainFooter />
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/BestElectronicsShop/Layouts/MainHeader.vue";
import ResponsiveNavbar from "../../components/BestElectronicsShop/Layouts/ResponsiveNavbar.vue";
import BreadCrumb from "../../components/BestElectronicsShop/Common/BreadCrumb.vue";
import FaqAccordion from "../../components/BestElectronicsShop/Faq/FaqAccordion.vue";
import MainFooter from "../../components/BestElectronicsShop/Layouts/MainFooter.vue";

export default defineComponent({
  name: "FaqPage",
  components: {
    MainHeader,
    ResponsiveNavbar,
    BreadCrumb,
    FaqAccordion,
    MainFooter,
  },
});
</script>