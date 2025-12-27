<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <div class="pos-content d-flex flex-wrap justify-content-between">
      <PosRightContent />
      <PosLeftContent />
    </div>
    <EditContent />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <div
    class="delete-popup offcanvas offcanvas-end border-0"
    tabindex="-1"
    id="deletePopup"
  >
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Your product is deleted from the list.
        </span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import PosLeftContent from "../../components/Sales/Pos/PosLeftContent.vue";
import PosRightContent from "../../components/Sales/Pos/PosRightContent.vue";
import EditContent from "../../components/Sales/Pos/EditContent.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "PosPage",
  components: {
    MainHeader,
    MainSidebar,
    PosLeftContent,
    PosRightContent,
    EditContent,
    MainFooter,
  },
});
</script>