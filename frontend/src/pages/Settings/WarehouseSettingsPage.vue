<template>
    <MainHeader />
    <MainSidebar />

    <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
        <BreadcrumbMenu pageTitle="Warehouse Settings" />

        <!-- Render main warehouse settings only if warehouseId is valid -->
        <template v-if="warehouseId !== undefined">
            <WarehouseGeneralDetails :warehouseId="warehouseId" />
            <CurrencySettings :level="'warehouse'" :warehouseId="warehouseId" />

            <!-- Clear Warehouse Cache -->
            <!-- <div class="card border-0 shadow-none rounded-1 mb-40">
                <div class="card-body p-xl-40">
                    <h6 class="fs-18 mb-35 text-title fw-semibold">Clear Cache</h6>
                    <button type="button" class="btn style-five" @click="clearCache">
                        Clear Warehouse Cache
                    </button>
                </div>
            </div> -->
        </template>

        <div class="flex-grow-1"></div>
        <MainFooter />
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useWarehouseStore } from "@/stores/warehouseStore";

// Layout Components
import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

// Warehouse Settings Components
import WarehouseGeneralDetails from "@/components/Settings/Warehouse/WarehouseGeneralDetails.vue";
import WarehouseOtherSettings from "@/components/Settings/Warehouse/WarehouseOtherSettings.vue";
import CurrencySettings from "@/components/Settings/SystemSettings/CurrencySettings.vue";

export default defineComponent({
    name: "WarehouseSettingsPage",
    components: {
        MainHeader,
        MainSidebar,
        BreadcrumbMenu,
        WarehouseGeneralDetails,
        WarehouseOtherSettings,
        CurrencySettings,
        MainFooter,
    },
    setup() {
        const route = useRoute();
        const router = useRouter();
        const warehouseStore = useWarehouseStore();

        const warehouseId = ref<number | undefined>(undefined);

        // Validate warehouse and redirect if not found
        const validateWarehouse = async (id?: number) => {
            if (!id) {
                router.replace({
                    path: "/not-found",
                    query: {
                        code: "404",
                        title: "Warehouse Not Found",
                        message: "This warehouse does not exist.",
                        buttonName: 'Go to Dashboard',
                        link: "/",
                    },
                });
                return;
            }

            try {
                await warehouseStore.fetchWarehouseDetail(id);
            } catch (err) {
                console.error("Warehouse not found:", err);
                router.replace({
                    path: "/not-found",
                    query: {
                        code: "404",
                        title: "Warehouse Not Found",
                        message: "This warehouse does not exist.",
                        linkText: 'Go to Dashboard',
                        link: "/",
                    },
                });
            }
        };

        // Watch route param for warehouseId changes
        watch(
            () => route.params.id,
            (val) => {
                const id = val ? parseInt(val as string, 10) : undefined;
                warehouseId.value = id;
                validateWarehouse(id);
            },
            { immediate: true }
        );

        // Clear warehouse cache (stub)
        const clearCache = () => {
            if (!warehouseId.value) return;
        };

        return {
            warehouseId,
            clearCache,
        };
    },
});
</script>
