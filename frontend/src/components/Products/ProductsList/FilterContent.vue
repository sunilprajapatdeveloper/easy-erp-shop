<template>
  <div class="row gx-0 mb-30">
    <div class="col-md-6">
      <div class="filter-left d-flex align-items-center flex-wrap">
        <!-- Filter button with dropdown -->
        <div class="dropdown">
          <button type="button" class="btn style-two dropdown-toggle position-relative" data-bs-toggle="dropdown"
            aria-expanded="false">
            Filter <img src="@/assets/img/icons/filter.svg" alt="Filter" />
            <span v-if="activeFilterCount > 0" class="filter-badge">{{ activeFilterCount }}</span>
          </button>
          <div class="dropdown-menu filter-dropdown-menu p-3" style="min-width: 280px;">
            <div class="filter-options">
              <!-- Warehouse filter -->
              <div class="mb-3">
                <label class="form-label fw-semibold">Warehouse</label>
                <select v-model="filters.warehouseId" class="form-select form-select-sm">
                  <option :value="null">All Products (Company Level)</option>
                  <option v-for="wh in warehouses" :key="wh.id" :value="wh.id">{{ wh.name }}</option>
                </select>
              </div>

              <!-- Category filter -->
              <div class="mb-3">
                <label class="form-label fw-semibold">Category</label>
                <select v-model="filters.categoryId" class="form-select form-select-sm">
                  <option :value="null">All Categories</option>
                  <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
                </select>
              </div>

              <!-- Brand filter -->
              <div class="mb-3">
                <label class="form-label fw-semibold">Brand</label>
                <select v-model="filters.brandId" class="form-select form-select-sm">
                  <option :value="null">All Brands</option>
                  <option v-for="brand in brands" :key="brand.id" :value="brand.id">{{ brand.name }}</option>
                </select>
              </div>

              <!-- Unit filter -->
              <div class="mb-3">
                <label class="form-label fw-semibold">Product Unit</label>
                <select v-model="filters.unitId" class="form-select form-select-sm">
                  <option :value="null">All Units</option>
                  <option v-for="unit in units" :key="unit.id" :value="unit.id">{{ unit.name }} ({{ unit.shortName }})
                  </option>
                </select>
              </div>

              <!-- Status filter -->
              <div class="mb-3">
                <label class="form-label fw-semibold">Status</label>
                <select v-model="filters.status" class="form-select form-select-sm">
                  <option :value="null">All Status</option>
                  <option v-for="(label, value) in productStatusOptions" :key="value" :value="value">{{ label }}
                  </option>
                </select>
              </div>

              <!-- Product Type filter -->
              <div class="mb-3">
                <label class="form-label fw-semibold">Product Type</label>
                <select v-model="filters.productType" class="form-select form-select-sm">
                  <option :value="null">All Types</option>
                  <option v-for="(label, value) in productTypeOptions" :key="value" :value="value">{{ label }}</option>
                </select>
              </div>

              <div class="d-flex flex-wrap gap-2 mt-2">
                <button class="btn btn-filter-apply flex-grow-1" @click="applyFilters">
                  Apply Filters
                </button>
                <button class="btn btn-filter-reset flex-grow-1" @click="resetFilters">
                  Reset
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Search input -->
        <form action="#" class="search-area position-relative w-sm-100" @submit.prevent="handleSearch">
          <input type="text" placeholder="Search On This Table"
            class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" v-model="searchQuery"
            @input="onSearchInput" />
          <button type="submit" class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
            <img src="@/assets/img/icons/search.svg" alt="Search" />
          </button>
        </form>
      </div>
    </div>
    <div class="col-md-6">
      <div class="filter-right d-flex align-items-center justify-content-md-end flex-wrap">
        <div class="btn style-one" @click.prevent="openExportModal">
          Export
          <img src="@/assets/img/icons/download.svg" alt="Export" style="transform: rotate(180deg);" />
        </div>
        <div class="btn style-five upload-btn" @click.prevent="openImportModal">
          Import
          <img src="@/assets/img/icons/download.svg" alt="Import" />
        </div>
        <router-link :to="btnLink" class="btn style-one">
          Create {{ btnText }}
          <img src="@/assets/img/icons/add-circle.svg" alt="Create" />
        </router-link>
      </div>
    </div>
  </div>

  <ImportDialog ref="importDialogRef" :module="module" :moduleName="btnText" :showOptions="showImportOptions"
    :warehouses="warehouses" @imported="onImported" @refresh="onImportRefresh" />
  <ExportDialog ref="exportDialogRef" :module="module" :moduleName="btnText" @exported="onExported"
    @refresh="onImportRefresh" />
</template>

<script lang="ts">
import { defineComponent, PropType, ref, onMounted, computed } from 'vue';
import { Modal } from 'bootstrap';
import ImportDialog from '@/components/Common/ImportDialog.vue';
import ExportDialog from '@/components/Common/ExportDialog.vue';
import type { WarehouseListItem } from '@/types/Warehouse';
import { useCategoryStore } from '@/stores/categoryStore';
import { useBrandStore } from '@/stores/brandStore';
import { useWarehouseStore } from '@/stores/warehouseStore';
import { useUnitStore } from '@/stores/unitStore';
import { ProductStatusLabels } from '@/enums/productStatus';
import { ProductTypeLabels } from '@/enums/productType';

function debounce(fn: Function, delay: number) {
  let timeoutId: ReturnType<typeof setTimeout>;
  return (...args: any[]) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => fn(...args), delay);
  };
}

export default defineComponent({
  name: 'FilterContent',
  components: { ImportDialog, ExportDialog },
  props: {
    btnText: { type: String, required: true },
    btnLink: { type: String, required: true },
    module: { type: String, required: true },
    showImportOptions: { type: Boolean, default: false },
    warehouses: { type: Array as PropType<WarehouseListItem[]>, default: () => [] },
  },
  emits: ['imported', 'exported', 'refresh', 'filter-change', 'search'],
  setup(props, { emit }) {
    const categoryStore = useCategoryStore();
    const brandStore = useBrandStore();
    const warehouseStore = useWarehouseStore();
    const unitStore = useUnitStore();

    const importDialogRef = ref<InstanceType<typeof ImportDialog>>();
    const exportDialogRef = ref<InstanceType<typeof ExportDialog>>();
    const searchQuery = ref('');

    const filters = ref({
      warehouseId: null as number | null,
      categoryId: null as number | null,
      brandId: null as number | null,
      unitId: null as number | null,
      status: null as string | null,
      productType: null as string | null,
    });

    const categories = ref<any[]>([]);
    const brands = ref<any[]>([]);
    const warehouses = ref<WarehouseListItem[]>([]);
    const units = ref<any[]>([]);

    const productStatusOptions = ProductStatusLabels;
    const productTypeOptions = ProductTypeLabels;

    const activeFilterCount = computed(() => {
      let count = 0;
      if (filters.value.warehouseId !== null) count++;
      if (filters.value.categoryId !== null) count++;
      if (filters.value.brandId !== null) count++;
      if (filters.value.unitId !== null) count++;
      if (filters.value.status !== null) count++;
      if (filters.value.productType !== null) count++;
      return count;
    });

    onMounted(async () => {
      await Promise.all([
        categoryStore.fetchCategories(),
        brandStore.fetchBrands(),
        warehouseStore.fetchWarehouses(),
        unitStore.fetchUnits(),
      ]);
      categories.value = categoryStore.categories;
      brands.value = brandStore.brands;
      warehouses.value = warehouseStore.warehouses;
      units.value = unitStore.units;
    });

    const openImportModal = () => {
      const modalEl = document.getElementById('importModal');
      if (modalEl) new Modal(modalEl).show();
    };
    const openExportModal = () => {
      const modalEl = document.getElementById('exportModal');
      if (modalEl) new Modal(modalEl).show();
    };
    const onImported = (jobId: number) => emit('imported', jobId);
    const onExported = (jobId: number) => emit('exported', jobId);
    const onImportRefresh = () => emit('refresh');

    const emitSearch = debounce((query: string) => {
      emit('search', query);
    }, 500);

    const onSearchInput = () => {
      emitSearch(searchQuery.value);
    };

    const handleSearch = () => {
      emit('search', searchQuery.value);
    };

    const applyFilters = () => {
      const filterPayload = {
        warehouseId: filters.value.warehouseId ? Number(filters.value.warehouseId) : null,
        categoryId: filters.value.categoryId ? Number(filters.value.categoryId) : null,
        brandId: filters.value.brandId ? Number(filters.value.brandId) : null,
        unitId: filters.value.unitId ? Number(filters.value.unitId) : null,
        status: filters.value.status,
        productType: filters.value.productType,
      };
      emit('filter-change', filterPayload);
    };

    const resetFilters = () => {
      filters.value = {
        warehouseId: null,
        categoryId: null,
        brandId: null,
        unitId: null,
        status: null,
        productType: null,
      };
      applyFilters();
    };

    return {
      importDialogRef,
      exportDialogRef,
      searchQuery,
      filters,
      categories,
      brands,
      warehouses,
      units,
      productStatusOptions,
      productTypeOptions,
      activeFilterCount,
      openImportModal,
      openExportModal,
      onImported,
      onExported,
      onImportRefresh,
      onSearchInput,
      handleSearch,
      applyFilters,
      resetFilters,
    };
  },
});
</script>

<style lang="scss" scoped>
.filter-left {
  button {
    margin-right: 10px;
    position: relative;
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

.filter-dropdown-menu {
  border-radius: 12px;
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;

  .form-label {
    font-size: 0.75rem;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    margin-bottom: 0.25rem;
  }

  .form-select-sm {
    font-size: 0.875rem;
    border-radius: 8px;
  }
}

.filter-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: #ef4444;
  color: white;
  font-size: 10px;
  font-weight: bold;
  padding: 2px 6px;
  border-radius: 20px;
  line-height: 1;
}

.btn-filter-apply,
.btn-filter-reset {
  font-size: 0.75rem;
  font-weight: 500;
  padding: 6px 12px;
  border-radius: 40px;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  cursor: pointer;
  border: none;
}

.btn-filter-apply {
  background: linear-gradient(132deg, #4f46e5 4.27%, #6366f1 100%);
  color: white;
  box-shadow: 0 2px 6px rgba(79, 70, 229, 0.2);
}

.btn-filter-apply:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.3);
}

.btn-filter-reset {
  background: transparent;
  border: 1px solid #cbd5e1;
  color: #475569;
}

.btn-filter-reset:hover {
  background: #f8fafc;
  border-color: #94a3b8;
  transform: translateY(-1px);
}

@media only screen and (max-width: 767px) {
  .filter-left .btn {
    margin-bottom: 15px;
  }

  .filter-right a,
  .filter-right div,
  .filter-right button {
    margin-bottom: 10px;
  }

  .btn-filter-apply,
  .btn-filter-reset {
    width: 100%;
    margin-bottom: 0;
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
  .filter-left .search-area {
    width: 320px;
  }
}

@media only screen and (min-width: 1400px) {
  .filter-left button {
    margin-right: 15px;
  }

  .filter-left .search-area {
    width: 320px;
  }

  .filter-right a,
  .filter-right div,
  .filter-right button {
    margin-right: 15px;
  }
}
</style>