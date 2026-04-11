// composables/usePagination.ts
import { ref, reactive, watch, computed } from "vue";
import { PaginationRequest, PaginationResponse } from "@/types/pagination";
import { useDebounceFn } from "@vueuse/core"; // optional, but recommended

export function usePagination<T>(
  fetchFn: (params: PaginationRequest) => Promise<PaginationResponse<T>>,
) {
  const data = ref<T[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const pagination = reactive({
    page: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0,
    hasNext: false,
    hasPrevious: false,
  });

  const sort = ref("");
  const search = ref("");

  const loadData = async () => {
    loading.value = true;
    error.value = null;
    try {
      const response = await fetchFn({
        page: pagination.page,
        size: pagination.size,
        sort: sort.value,
        search: search.value,
      });
      data.value = response.data;
      pagination.page = response.pagination.page;
      pagination.size = response.pagination.size;
      pagination.totalElements = response.pagination.totalElements;
      pagination.totalPages = response.pagination.totalPages;
      pagination.hasNext = response.pagination.hasNext;
      pagination.hasPrevious = response.pagination.hasPrevious;
    } catch (err: any) {
      error.value = err.message || "Failed to load data";
    } finally {
      loading.value = false;
    }
  };

  const goToPage = (page: number) => {
    if (page >= 0 && page < pagination.totalPages) {
      pagination.page = page;
      loadData();
    }
  };

  const nextPage = () => goToPage(pagination.page + 1);
  const prevPage = () => goToPage(pagination.page - 1);

  const setPageSize = (newSize: number) => {
    pagination.size = newSize;
    pagination.page = 0;
    loadData();
  };

  const setSort = (newSort: string) => {
    sort.value = newSort;
    pagination.page = 0;
    loadData();
  };

  const setSearch = (newSearch: string) => {
    search.value = newSearch;
    pagination.page = 0;
    loadData();
  };

  // Debounced search (300ms)
  const debouncedSetSearch = useDebounceFn
    ? useDebounceFn(setSearch, 300)
    : setSearch;

  // Auto-load on mount
  loadData();

  return {
    data,
    loading,
    error,
    pagination,
    sort,
    search,
    loadData,
    goToPage,
    nextPage,
    prevPage,
    setPageSize,
    setSort,
    setSearch,
    debouncedSetSearch,
  };
}
