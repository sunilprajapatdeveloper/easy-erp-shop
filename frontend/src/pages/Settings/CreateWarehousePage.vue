<template>
    <MainHeader />
    <MainSidebar />

    <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
        <BreadcrumbMenu pageTitle="Create Warehouse" />

        <!-- Render Create Form -->
        <WarehouseGeneralDetails />
        <!-- <CurrencySettings :level="'warehouse'" /> -->

        <div class="flex-grow-1"></div>
        <MainFooter />
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

// Layout Components
import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

// Warehouse Components
import WarehouseGeneralDetails from "@/components/Settings/Warehouse/WarehouseGeneralDetails.vue";
import CurrencySettings from "@/components/Settings/SystemSettings/CurrencySettings.vue";

export default defineComponent({
    name: "CreateWarehousePage",
    components: {
        MainHeader,
        MainSidebar,
        BreadcrumbMenu,
        MainFooter,
        WarehouseGeneralDetails,
        CurrencySettings
    },
});
</script>
