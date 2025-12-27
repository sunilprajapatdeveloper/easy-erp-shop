<template>
    <MainHeader />
    <MainSidebar />
    <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
        <BreadcrumbMenu pageTitle="POS Settings" />

        <!-- POS General Settings -->
        <PosGeneralSettings />

        <!-- You can later add more POS-related configs here -->
        <!-- <PosReceiptSettings /> -->
        <!-- <PosPrinterSettings /> -->
        <!-- <PosShortcutSettings /> -->

        <div class="flex-grow-1"></div>
        <MainFooter />
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

// New POS Settings component
import PosGeneralSettings from "@/components/Settings/SystemSettings/PosGeneralSettings.vue";
// import PosReceiptSettings from "../../components/Settings/POS/PosReceiptSettings.vue";
// import PosPrinterSettings from "../../components/Settings/POS/PosPrinterSettings.vue";

export default defineComponent({
    name: "PosSettingsPage",
    components: {
        MainHeader,
        MainSidebar,
        BreadcrumbMenu,
        MainFooter,
        PosGeneralSettings,
        // PosReceiptSettings,
        // PosPrinterSettings
    },
});
</script>
