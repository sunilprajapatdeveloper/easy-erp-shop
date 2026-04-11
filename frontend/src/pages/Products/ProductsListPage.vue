<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Products List" />
    <FilterContent btnText="Product" btnLink="/create-product" module="Product" @refresh="refreshProducts"
      @search="onSearch" />
    <ProductsList :products="products" :loading="loading" :pagination="pagination" :sort-by="sortBy"
      :sort-order="sortOrder" @page-change="onPageChange" @size-change="onSizeChange" @sort-change="onSortChange"
      @bulk-delete="onBulkDelete" />
    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, onMounted, ref } from 'vue';
import { useProductStore } from '@/stores/productStore';
import MainHeader from '../../components/Layouts/MainHeader.vue';
import MainSidebar from '../../components/Layouts/MainSidebar.vue';
import BreadcrumbMenu from '../../components/Common/BreadcrumbMenu.vue';
import FilterContent from '../../components/Common/FilterContent.vue';
import ProductsList from '../../components/Products/ProductsList/ProductsList.vue';
import MainFooter from '../../components/Layouts/MainFooter.vue';

export default defineComponent({
  name: 'ProductsListPage',
  components: {
    MainHeader,
    MainSidebar,
    BreadcrumbMenu,
    FilterContent,
    ProductsList,
    MainFooter,
  },
  setup() {
    const productStore = useProductStore();

    // Local state for sorting
    const sortBy = ref('');
    const sortOrder = ref<'asc' | 'desc'>('asc');

    const products = computed(() => productStore.products);
    const loading = computed(() => productStore.loading);
    const pagination = computed(() => productStore.pagination);

    // Helper to build sort parameter for API
    const getSortParam = () => {
      if (!sortBy.value) return '';
      return `${sortBy.value},${sortOrder.value}`;
    };

    const fetchProducts = (page: number, size: number, search: string) => {
      productStore.fetchProductsPaginated({
        page,
        size,
        sort: getSortParam(),
        search,
      });
    };

    const refreshProducts = () => {
      fetchProducts(
        productStore.pagination.page,
        productStore.pagination.size,
        productStore.currentSearch
      );
    };

    const onSearch = (query: string) => {
      fetchProducts(0, productStore.pagination.size, query);
    };

    const onPageChange = (page: number) => {
      fetchProducts(page, productStore.pagination.size, productStore.currentSearch);
    };

    const onSizeChange = (size: number) => {
      fetchProducts(0, size, productStore.currentSearch);
    };

    const onSortChange = (payload: { sortBy: string; sortOrder: 'asc' | 'desc' }) => {
      sortBy.value = payload.sortBy;
      sortOrder.value = payload.sortOrder;
      // Reset to first page when sorting changes
      fetchProducts(0, productStore.pagination.size, productStore.currentSearch);
    };

    const onBulkDelete = async (ids: number[]) => {
      if (!confirm(`Are you sure you want to delete ${ids.length} product(s)?`)) return;
      try {
        await productStore.bulkDelete(ids);
        // Refresh current page after deletion
        fetchProducts(
          productStore.pagination.page,
          productStore.pagination.size,
          productStore.currentSearch
        );
        alert(`${ids.length} product(s) deleted successfully.`);
      } catch (error) {
        console.error('Bulk delete failed:', error);
        alert('Failed to delete products.');
      }
    };

    onMounted(() => {
      fetchProducts(0, 20, '');
    });

    return {
      products,
      loading,
      pagination,
      sortBy,
      sortOrder,
      refreshProducts,
      onSearch,
      onPageChange,
      onSizeChange,
      onSortChange,
      onBulkDelete,
    };
  },
});
</script>

<style lang="scss" scoped>
.chart-wrapper {
  margin: 0 -10px;

  .card {
    margin-left: 10px;
    margin-right: 10px;
  }
}
</style>