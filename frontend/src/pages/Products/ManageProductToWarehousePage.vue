<template>
    <MainHeader />
    <MainSidebar />
    <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
        <BreadcrumbMenu pageTitle="Manage Product to Warehouse" />
        <ProductWarehouseConfigurator />

        <div class="flex-grow-1"></div>
        <MainFooter />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import MainHeader from '@/components/Layouts/MainHeader.vue';
import MainSidebar from '@/components/Layouts/MainSidebar.vue';
import BreadcrumbMenu from '@/components/Common/BreadcrumbMenu.vue';
import ProductWarehouseConfigurator from '@/components/Products/ManageProductToWarehouse/ProductWarehouseConfigurator.vue';
import MainFooter from '@/components/Layouts/MainFooter.vue';

export default defineComponent({
    name: 'ManageProductToWarehousePage',
    components: {
        MainHeader,
        MainSidebar,
        BreadcrumbMenu,
        ProductWarehouseConfigurator,
        MainFooter,
    },
});
</script>

<style lang="scss" scoped>
// Optional page-specific styles</style>