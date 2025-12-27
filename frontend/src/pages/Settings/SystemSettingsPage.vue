<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="System Settings" />
    <SystemSettings />
    <CurrencySettings :level="'company'" />
    <!-- <CurrencySettings :level="'warehouse'" :warehouseId="123" /> -->
    <!-- <ExchangeRates /> -->
    <!-- <PosReceiptSettings /> -->
    <!-- <PaymentGateway /> -->
    <!-- <SMSConfiguration /> -->
    <!-- <SMTPConfiguration /> -->
    <!-- <div class="card border-0 shadow-none rounded-1 mb-40">
      <div class="card-body p-xl-40">
        <h6 class="fs-18 mb-35 text-title fw-semibold">Clear Cache</h6>
        <button type="button" class="btn style-five">Change Settings</button>
      </div>
    </div> -->

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import SystemSettings from "../../components/Settings/SystemSettings/SystemSettings.vue";
import CurrencySettings from "@/components/Settings/SystemSettings/CurrencySettings.vue";
// import ExchangeRates from "@/components/Settings/SystemSettings/ExchangeRates.vue";
import PosReceiptSettings from "../../components/Settings/SystemSettings/PosReceiptSettings.vue";
import PaymentGateway from "../../components/Settings/SystemSettings/PaymentGateway.vue";
import SMSConfiguration from "../../components/Settings/SystemSettings/SMSConfiguration.vue";
import SMTPConfiguration from "../../components/Settings/SystemSettings/SMTPConfiguration.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "SystemSettingsPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    SystemSettings,
    CurrencySettings,
    // ExchangeRates,
    PosReceiptSettings,
    PaymentGateway,
    SMSConfiguration,
    SMTPConfiguration,
    MainFooter,
  },
});
</script>