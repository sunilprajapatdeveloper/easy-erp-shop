<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="Edit Group Permission" />
    <EditGroupPermission />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import EditGroupPermission from "../../components/Settings/EditGroupPermission/EditGroupPermission.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "EditGroupPermissionPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    EditGroupPermission,
    MainFooter,
  },
});
</script>