<template>
  <MainHeader />
  <MainSidebar />
  <div
    class="main-content bg_gray d-flex flex-column transition overflow-hidden"
  >
    <BreadcrumbMenu pageTitle="Create Expenses" />
    <CreateExpensesFrom />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import CreateExpensesFrom from "../../components/Expenses/CreateExpenses/CreateExpensesFrom.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

export default defineComponent({
  name: "EditExpensesPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    CreateExpensesFrom,
    MainFooter,
  },
});
</script>