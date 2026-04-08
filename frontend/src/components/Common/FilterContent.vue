<template>
  <div class="row gx-0 mb-30">
    <div class="col-md-6">
      <div class="filter-left d-flex align-items-center flex-wrap">
        <button type="button" class="btn style-two" data-bs-toggle="modal" data-bs-target="#filterModal">
          Filter <img src="../../assets/img/icons/filter.svg" alt="Image" />
        </button>
        <form action="#" class="search-area position-relative w-sm-100" @submit.prevent="handleSearch">
          <input type="text" placeholder="Search On This Table"
            class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" v-model="searchQuery"
            @input="onSearchInput" />
          <button type="submit" class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
            <img src="../../assets/img/icons/search.svg" alt="Image" />
          </button>
        </form>
      </div>
    </div>
    <div class="col-md-6">
      <div class="filter-right d-flex align-items-center justify-content-md-end flex-wrap">
        <div class="btn style-one" @click.prevent="openExportModal">
          Export
          <img src="../../assets/img/icons/download.svg" alt="Export" style="transform: rotate(180deg);" />
        </div>
        <div class="btn style-five upload-btn" @click.prevent="openImportModal">
          Import
          <img src="../../assets/img/icons/download.svg" alt="Import" />
        </div>
        <router-link :to="btnLink" class="btn style-one">
          Create {{ btnText }}
          <img src="../../assets/img/icons/add-circle.svg" alt="Image" />
        </router-link>
      </div>
    </div>
  </div>

  <FilterModal />
  <ImportDialog ref="importDialogRef" :module="module" :moduleName="btnText" :showOptions="showImportOptions"
    :warehouses="warehouses" @imported="onImported" @refresh="onImportRefresh" />
  <ExportDialog ref="exportDialogRef" :module="module" :moduleName="btnText" @exported="onExported"
    @refresh="onImportRefresh" />
</template>

<script lang="ts">
import { defineComponent, PropType, ref, watch } from 'vue';
import { Modal } from 'bootstrap';
import FilterModal from './FilterModal.vue';
import ImportDialog from './ImportDialog.vue';
import ExportDialog from './ExportDialog.vue';
import type { WarehouseListItem } from '@/types/Warehouse';

// Simple debounce helper
function debounce(fn: Function, delay: number) {
  let timeoutId: ReturnType<typeof setTimeout>;
  return (...args: any[]) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => fn(...args), delay);
  };
}

export default defineComponent({
  name: 'FilterContent',
  components: { FilterModal, ImportDialog, ExportDialog },
  props: {
    btnText: { type: String, required: true },
    btnLink: { type: String, required: true },
    module: { type: String, required: true },
    showImportOptions: { type: Boolean, default: false },
    warehouses: { type: Array as PropType<WarehouseListItem[]>, default: () => [] },
  },
  emits: ['imported', 'exported', 'refresh', 'search'],
  setup(props, { emit }) {
    const importDialogRef = ref<InstanceType<typeof ImportDialog>>();
    const exportDialogRef = ref<InstanceType<typeof ExportDialog>>();
    const searchQuery = ref('');

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

    // Emit search query to parent (debounced)
    const emitSearch = debounce((query: string) => {
      emit('search', query);
    }, 500);

    const onSearchInput = () => {
      emitSearch(searchQuery.value);
    };

    const handleSearch = () => {
      emit('search', searchQuery.value);
    };

    return {
      importDialogRef,
      exportDialogRef,
      searchQuery,
      openImportModal,
      openExportModal,
      onImported,
      onExported,
      onImportRefresh,
      onSearchInput,
      handleSearch,
    };
  },
});
</script>

<style lang="scss" scoped>
/* Keep your existing styles unchanged */
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
  .filter-left .btn {
    margin-bottom: 15px;
  }

  .filter-right a,
  .filter-right div,
  .filter-right button {
    margin-bottom: 10px;
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