<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Adjustment List" />
    <FilterContent btnText="Adjustment" btnLink="/create-adjustment" />
    <AdjustmentList @delete-adjustment="onDeleteAdjustment" @view-adjustment="onViewAdjustment" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">{{ successMessage }}</span>
      </div>
    </div>
  </div>

  <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
    <div class="offcanvas-body p-0">
      <div class="create-error">
        <img src="../../assets/img/icons/close-circle-2.svg" alt="Image" style="filter: brightness(0) invert(1);" />
        <span class="text-white fw-medium">{{ errorMessage }}</span>
      </div>
    </div>
  </div>
  <DetailsModal :adjustment="selectedAdjustment" />
</template>

<script setup lang="ts">
import { ref } from "vue";
import { Offcanvas } from "bootstrap";
import { Adjustment } from "@/types/Adjustment";
import { useAdjustmentStore } from "@/stores/adjustmentStore";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import FilterContent from "../../components/Common/FilterContent.vue";
import AdjustmentList from "../../components/Adjustments/AdjustmentList/AdjustmentList.vue";
import DetailsModal from "../../components/Adjustments/AdjustmentList/DetailsModal.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

const adjustmentStore = useAdjustmentStore();
const errorMessage = ref("Something went wrong.");
const successMessage = ref("Adjustment deleted successfully.");
const selectedAdjustment = ref<Adjustment | null>(null);

const onViewAdjustment = (adjustment: Adjustment) => {
  selectedAdjustment.value = adjustment;
};

const onDeleteAdjustment = async (id: number) => {
  try {
    await adjustmentStore.removeAdjustment(id);
    await adjustmentStore.fetchAdjustments();

    successMessage.value = `Adjustment ${id} has been successfully deleted.`;

    const el = document.getElementById("deletePopup");
    if (el) {
      const instance = (Offcanvas as any).getInstance(el) || new Offcanvas(el);
      instance.show();
      setTimeout(() => instance.hide(), 3000);
    }
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message || `Failed to delete adjustment: #${id}`;

    const el = document.getElementById("errorPopup");
    if (el) {
      const instance = (Offcanvas as any).getInstance(el) || new Offcanvas(el);
      instance.show();
      setTimeout(() => instance.hide(), 3000);
    }
  }
};
</script>