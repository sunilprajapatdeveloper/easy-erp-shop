<template>
  <MainHeader />
  <ResponsiveNavbar />
  <div class="bg_ash pt-57">
    <div class="container">
      <BreadCrumb pageTitle="Terms & Conditions" class="mb-80" />
      <TermsConditions />
    </div>
  </div>
  <MainFooter />
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/BestElectronicsShop/Layouts/MainHeader.vue";
import ResponsiveNavbar from "../../components/BestElectronicsShop/Layouts/ResponsiveNavbar.vue";
import BreadCrumb from "../../components/BestElectronicsShop/Common/BreadCrumb.vue";
import TermsConditions from "../../components/BestElectronicsShop/TermsConditions/TermsConditions.vue";
import MainFooter from "../../components/BestElectronicsShop/Layouts/MainFooter.vue";

export default defineComponent({
  name: "TermsConditionsPage",
  components: {
    MainHeader,
    ResponsiveNavbar,
    BreadCrumb,
    TermsConditions,
    MainFooter,
  },
});
</script>