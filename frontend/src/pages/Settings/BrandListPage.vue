<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Brand List" />
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
            Create Brand
            <img src="../../assets/img/icons/add-circle.svg" alt="Image" />
          </button>
        </div>
      </div>
    </div>
    <BrandList v-if="canViewBrandList" @edit-brand="onEditBrand" @delete-brand="onDeleteBrand" />

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

  <CreateBrandModal v-if="canCreate" :initial-data="selectedBrand" :is-editing="!!selectedBrand" @submit="onSubmitBrand" />
</template>

<script lang="ts" setup>
import { ref, computed } from "vue";
import type { Brand } from "@/types/Brand";
import { useBrandStore } from "@/stores/brandStore";
import { useUserStore } from "@/stores/userStore";

// Components automatically available in template (no need to register them)
import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import BrandList from "../../components/Settings/BrandList/BrandList.vue";
import CreateBrandModal from "../../components/Settings/BrandList/CreateBrandModal.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";

const brandStore = useBrandStore();
const userStore = useUserStore();
const selectedBrand = ref<Brand | null>(null);

const canViewBrandList = computed(() => userStore.userPermissions.includes('BRAND_LIST'));
const canCreate = computed(() => userStore.userPermissions.includes('BRAND_CREATE'));

const onEditBrand = (brand: Brand) => {
  selectedBrand.value = { ...brand };
};

const onDeleteBrand = async (id: number) => {
  await brandStore.removeBrand(id);
};

const onSubmitBrand = async (data: Omit<Brand, "id">) => {
  if (selectedBrand.value) {
    await brandStore.updateBrand(selectedBrand.value.id, data);
  } else {
    await brandStore.addBrand(data);
  }
  selectedBrand.value = null;
};

const onCreateClick = () => {
  selectedBrand.value = null;
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