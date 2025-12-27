<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Currncy List" />
    <div class="row gx-0 mb-30">
      <div class="col-md-6">
        <div class="filter-left d-flex align-items-center flex-wrap">
          <form action="#" class="search-area position-relative w-sm-100">
            <input type="text" placeholder="Search On This Table"
              class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
            <button type="submit" class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
              <img src="../../assets/img/icons/search.svg" alt="Image" />
            </button>
          </form>
        </div>
      </div>
      <div v-if="canCreate" class="col-md-6">
        <div class="filter-right d-flex align-items-center justify-content-md-end flex-wrap">
          <button class="btn style-one" data-bs-toggle="modal" data-bs-target="#createModal" @click="onCreateClick">
            Create Currency
            <img src="../../assets/img/icons/add-circle.svg" alt="Image" />
          </button>
        </div>
      </div>
    </div>
    <CurrncyList v-if="canViewCurrencyList" @edit-currency="onEditCurrency" @delete-currency="onDeleteCurrency" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Your product is deleted from the list.
        </span>
      </div>
    </div>
  </div>

  <CreateCurrencyModal v-if="canCreate" :initial-data="selectedCurrency" :is-editing="!!selectedCurrency" @submit="onSubmitCurrency" />
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import type { Currency } from "@/types/Currency";
import { useCurrencyStore } from "@/stores/currencyStore";
import { useUserStore } from "@/stores/userStore";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import CurrncyList from "../../components/Settings/CurrncyList/CurrncyList.vue";
import CreateCurrencyModal from "../../components/Settings/CurrncyList/CreateCurrencyModal.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

const selectedCurrency = ref<Currency | null>(null);
const currencyStore = useCurrencyStore();
const userStore = useUserStore();

const canViewCurrencyList = computed(() => userStore.userPermissions.includes('CURRENCY_LIST'));
const canCreate = computed(() => userStore.userPermissions.includes('CURRENCY_CREATE'));

const onEditCurrency = (currency: Currency) => {
  selectedCurrency.value = { ...currency };
};

const onDeleteCurrency = async (id: number) => {
  await currencyStore.removeCurrency(id);
};

const onCreateClick = () => {
  selectedCurrency.value = null;
};

const onSubmitCurrency = async (payload: Omit<Currency, "id">) => {
  if (selectedCurrency.value) {
    await currencyStore.updateCurrency(selectedCurrency.value.id, payload);
  } else {
    await currencyStore.addCurrency(payload);
  }
  selectedCurrency.value = null;
};
</script>

<style lang="scss" scoped>
.filter-left {
  button {
    margin-right: 10px;
  }

  .search-area {
    input {
      padding-left: 20px;

      &::placeholder {
        opacity: 0.8;
      }
    }
  }
}

.filter-right {
  a {
    margin-right: 10px;

    &:last-child {
      margin-right: 0;
    }
  }

  div,
  button {
    margin-right: 10px;
  }
}

@media only screen and (max-width: 767px) {
  .filter-left {
    .btn {
      margin-bottom: 15px;
    }
  }

  .filter-right {

    a,
    div,
    button {
      margin-bottom: 10px;
    }
  }
}

@media only screen and (min-width: 768px) and (max-width: 991px) {
  .filter-right {
    width: calc(100% + 20px);
    margin-left: -20px;
  }
}

@media only screen and (max-width: 991px) {
  .filter-left {
    margin-bottom: 20px;
  }
}

@media only screen and (min-width: 992px) {
  .filter-left {
    .search-area {
      width: 320px;
    }
  }
}

@media only screen and (min-width: 1400px) {
  .filter-left {
    button {
      margin-right: 15px;
    }

    .search-area {
      width: 320px;
    }
  }

  .filter-right {

    a,
    div,
    button {
      margin-right: 15px;
    }
  }
}
</style>