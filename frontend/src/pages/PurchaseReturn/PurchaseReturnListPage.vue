<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Purchase Return List" />
    <FilterContent btnText="Purchase Return" btnLink="/create-purchase-return" />

    <!-- Pass event handlers -->
    <PurchaseReturnList @delete-return="onDeleteReturn" @view-return="onViewReturn" />
    <PurchaseReturnDetails :returnItem="selectedReturn" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- Delete success popup -->
  <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">{{ successMessage }}</span>
      </div>
    </div>
  </div>

  <!-- Delete error popup -->
  <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
    <div class="offcanvas-body p-0">
      <div class="create-error">
        <img src="../../assets/img/icons/close-circle-2.svg" alt="Image" style="filter: brightness(0) invert(1);" />
        <span class="text-white fw-medium">{{ errorMessage }}</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { Offcanvas } from "bootstrap";
import { usePurchaseReturnStore } from "@/stores/purchaseReturnStore";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import FilterContent from "../../components/Common/FilterContent.vue";
import PurchaseReturnList from "../../components/PurchaseReturn/PurchaseReturnList/PurchaseReturnList.vue";
import PurchaseReturnDetails from "../../components/PurchaseReturn/PurchaseReturnList/PurchaseReturnDetails.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "PurchaseReturnListPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    FilterContent,
    PurchaseReturnList,
    PurchaseReturnDetails,
    MainFooter,
  },
  setup() {
    const purchaseReturnStore = usePurchaseReturnStore();
    const selectedReturn = ref(null);
    const successMessage = ref("Purchase return deleted successfully.");
    const errorMessage = ref("Something went wrong.");

    // View handler
    const onViewReturn = (returnItem: any) => {
      selectedReturn.value = returnItem;
    };

    // Delete handler
    const onDeleteReturn = async (id: number) => {
      try {
        await purchaseReturnStore.removePurchaseReturn(id);
        await purchaseReturnStore.fetchPurchaseReturns();

        successMessage.value = `Purchase return #${id} has been successfully deleted.`;

        const el = document.getElementById("deletePopup");
        if (el) {
          const instance = Offcanvas.getOrCreateInstance(el);
          instance.show();
          setTimeout(() => instance.hide(), 3000);
        }
      } catch (error: any) {
        errorMessage.value =
          error?.response?.data?.message ||
          error?.response?.data?.error ||
          `Failed to delete purchase return: #${id}`;

        const el = document.getElementById("errorPopup");
        if (el) {
          const instance = Offcanvas.getOrCreateInstance(el);
          instance.show();
          setTimeout(() => instance.hide(), 3000);
        }
      }
    };

    return {
      selectedReturn,
      successMessage,
      errorMessage,
      onViewReturn,
      onDeleteReturn,
    };
  },
});
</script>