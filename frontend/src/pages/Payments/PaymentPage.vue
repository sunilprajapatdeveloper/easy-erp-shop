<template>
    <MainHeader />
    <MainSidebar />

    <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
        <div class="container py-4">
            <h3 class="mb-4 fw-bold">Checkout & Payment</h3>
            <!-- <PaymentGateway /> -->
        </div>

        <MainFooter />
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";
// import PaymentGateway from "@/components/Payment/PaymentGateway.vue";

export default defineComponent({
    name: "PaymentGatewayPage",
    components: {
        MainHeader,
        MainSidebar,
        MainFooter,
        // PaymentGateway,
    },
});
</script>
