<template>
    <MainHeader />
    <MainSidebar />
    <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
        <!-- Breadcrumb -->
        <BreadcrumbMenu pageTitle="Update User" />

        <!-- User Form -->
        <CreateUser />

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
import CreateUser from "@/components/People/UserList/CreateUser.vue";

export default defineComponent({
    name: "UpdateUserPage",
    components: {
        MainHeader,
        MainSidebar,
        BreadcrumbMenu,
        MainFooter,
        CreateUser,
    },
});
</script>
