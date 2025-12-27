<template>
  <MainHeader />
  <ResponsiveNavbar />
  <HeroContent />
  <OfferWrapper />
  <MainFooter />
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/BestElectronicsShop/Layouts/MainHeader.vue";
import ResponsiveNavbar from "../../components/BestElectronicsShop/Layouts/ResponsiveNavbar.vue";
import HeroContent from "../../components/BestElectronicsShop/Offer/HeroContent.vue";
import OfferWrapper from "../../components/BestElectronicsShop/Offer/OfferWrapper.vue";
import MainFooter from "../../components/BestElectronicsShop/Layouts/MainFooter.vue";

export default defineComponent({
  name: "OfferPage",
  components: {
    MainHeader,
    ResponsiveNavbar,
    HeroContent,
    OfferWrapper,
    MainFooter,
  },
});
</script>