<template>
  <MainHeader />
  <ResponsiveNavbar />
  <div class="bg_ash pt-57">
    <div class="container">
      <BreadCrumb pageTitle="Contact" />
      <ContactInfo />
    </div>
  </div>
  <ContactForm />
  <MainFooter />
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/BestElectronicsShop/Layouts/MainHeader.vue";
import ResponsiveNavbar from "../../components/BestElectronicsShop/Layouts/ResponsiveNavbar.vue";
import BreadCrumb from "../../components/BestElectronicsShop/Common/BreadCrumb.vue";
import ContactInfo from "../../components/BestElectronicsShop/Contact/ContactInfo.vue";
import ContactForm from "../../components/BestElectronicsShop/Contact/ContactForm.vue";
import MainFooter from "../../components/BestElectronicsShop/Layouts/MainFooter.vue";

export default defineComponent({
  name: "ContactPage",
  components: {
    MainHeader,
    ResponsiveNavbar,
    BreadCrumb,
    ContactInfo,
    ContactForm,
    MainFooter,
  },
});
</script>