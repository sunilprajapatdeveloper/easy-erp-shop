<template>
  <PreLoader />
  <SettingSidebar />
  <router-view />
  <Popup />
</template>

<script lang="ts">
import { defineComponent } from "vue";

import PreLoader from "./components/Layouts/PreLoader.vue";
import SettingSidebar from "./components/Layouts/SettingSidebar.vue";
import Popup from "./components/Common/Popup.vue";

export default defineComponent({
  name: "App",
  components: {
    PreLoader,
    SettingSidebar,
    Popup
  },
});
</script>