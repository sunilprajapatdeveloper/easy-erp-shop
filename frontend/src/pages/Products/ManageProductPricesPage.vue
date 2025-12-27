<template>
    <MainHeader />
    <MainSidebar />
    <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
        <BreadcrumbMenu pageTitle="Manage Product Prices" />
        <ManageProductPrices />

        <div class="flex-grow-1"></div>
        <MainFooter />
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ManageProductPrices from "@/components/Products/ManageProductPrices/ManageProductPrices.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

export default defineComponent({
    name: "ManageProductPricePage",
    components: {
        MainHeader,
        MainSidebar,
        BreadcrumbMenu,
        ManageProductPrices,
        MainFooter,
    },
});
</script>

<style lang="scss" scoped>
.chart-wrapper {
    margin: 0 -10px;

    .card {
        margin-left: 10px;
        margin-right: 10px;
    }
}
</style>
