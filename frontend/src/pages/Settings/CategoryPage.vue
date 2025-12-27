<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Category" />
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
      <div class="col-md-6" v-if="canCreate">
        <div class="filter-right d-flex align-items-center justify-content-md-end flex-wrap">
          <button class="btn style-one" data-bs-toggle="modal" data-bs-target="#createModal" @click="onCreateClick">
            Create Category
            <img src="../../assets/img/icons/add-circle.svg" alt="Image" />
          </button>
        </div>
      </div>
    </div>
    <CategoryList @edit-category="onEditCategory" @delete-category="onDeleteCategory" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <div class="delete-popup offcanvas offcanvas-end border-0" tabindex="-1" id="deletePopup">
    <div class="offcanvas-body p-0">
      <div class="delete-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">
          Your category is deleted from the list.
        </span>
      </div>
    </div>
  </div>

  <CreateCategoryModal :initial-data="selectedCategory" :is-editing="!!selectedCategory" @submit="onSubmitCategory" />
</template>

<script lang="ts">
import { defineComponent, ref, computed } from "vue";
import type { Category } from "@/types/Category";

import MainHeader from "../../components/Layouts/MainHeader.vue";
import MainSidebar from "../../components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "../../components/Common/BreadcrumbMenu.vue";
import CategoryList from "../../components/Settings/Category/CategoryList.vue";
import CreateCategoryModal from "../../components/Settings/Category/CreateCategoryModal.vue";
import MainFooter from "../../components/Layouts/MainFooter.vue";
import { useUserStore } from '@/stores/userStore';

import { useCategoryStore } from "@/stores/categoryStore";

export default defineComponent({
  name: "CategoryPage",
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    CategoryList,
    CreateCategoryModal,
    MainFooter,
  },
  setup() {
    const categoryStore = useCategoryStore();
    const selectedCategory = ref<Category | null>(null);
    const userStore = useUserStore();

    const canCreate = computed(() => userStore.userPermissions.includes('CATEGORY_CREATE'));

    const onEditCategory = (category: Category) => {
      selectedCategory.value = { ...category };
    };

    const onCreateClick = () => {
      selectedCategory.value = null;
    };

    const onDeleteCategory = async (id: number) => {
      await categoryStore.removeCategory(id);
    };

    const onSubmitCategory = async (payload: Omit<Category, "id">) => {
      if (selectedCategory.value) {
        await categoryStore.updateCategory(selectedCategory.value.id, payload);
      } else {
        await categoryStore.addCategory(payload);
      }
    };

    return {
      selectedCategory,
      onEditCategory,
      onDeleteCategory,
      onSubmitCategory,
      onCreateClick,
      canCreate
    };
  },
});
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