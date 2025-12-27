<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="My Profile" />
    <MyProfile />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import MyProfile from "../../components/Settings/MyProfile/MyProfile.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "MyProfilePage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    MyProfile,
    MainFooter,
  },
});
</script>