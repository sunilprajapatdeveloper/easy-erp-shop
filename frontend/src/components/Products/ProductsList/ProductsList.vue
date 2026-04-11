<template>
  <DataTable :rows="products" :loading="loading" :pagination="pagination" :columns="tableColumns" :sort-by="sortBy"
    :sort-order="sortOrder" :show-bulk-actions="true" selection-label="IMAGE" item-name="product"
    :can-delete="canDelete" @sort-change="$emit('sort-change', $event)" @page-change="$emit('page-change', $event)"
    @size-change="$emit('size-change', $event)" @bulk-delete="$emit('bulk-delete', $event)">
    <template #checkbox-add-on="{ row }">
      <div class="product-image-container">
        <img :src="getProductImage(row)" :alt="row.name" class="product-image" @error="handleImageError" />
        <span v-if="row.mediaImages?.length > 0"
          class="badge bg-secondary position-absolute top-0 end-0 image-count-badge">
          {{ row.mediaImages.length }}
        </span>
      </div>
    </template>

    <template #cell(name)="{ row }">
      <div class="d-flex flex-column">
        <strong>{{ row.name }}</strong>
        <small class="text-muted">{{ getProductTypeLabel(row.productType) }}</small>
      </div>
    </template>

    <template #cell(code)="{ value }">
      <code>{{ value }}</code>
    </template>

    <template #cell(sku)="{ value }">
      <small class="text-muted">{{ value || '—' }}</small>
    </template>

    <template #cell(categoryName)="{ row }">
      {{ row.categoryName || getCategoryName(row.categoryId) }}
    </template>

    <template #cell(brandName)="{ row }">
      {{ row.brandName || getBrandName(row.brandId) }}
    </template>

    <template #cell(productUnitName)="{ row }">
      {{ row.productUnitName || getUnitName(row.productUnitId) }}
    </template>

    <template #cell(status)="{ value }">
      <span :class="getStatusClass(value)" class="badge">{{ getStatusLabel(value) }}</span>
    </template>

    <template #cell(actions)="{ row }">
      <router-link v-if="canView" :to="`/product-details/${row.id}`" class="btn btn-sm btn-outline-secondary"
        title="View">
        <FontAwesomeIcon icon="eye" />
      </router-link>

      <router-link v-if="canEdit" :to="`/edit-product/${row.id}`" class="btn btn-sm btn-outline-primary" title="Edit">
        <FontAwesomeIcon icon="edit" />
      </router-link>

      <button v-if="canDelete" class="btn btn-sm btn-outline-danger" title="Delete"
        @click.prevent="confirmDelete(row.id)">
        <FontAwesomeIcon icon="trash" />
      </button>
    </template>

    <template #empty-action>
      <router-link v-if="canEdit" to="/create-product" class="btn btn-sm btn-primary mt-2">
        <i class="fas fa-plus me-1"></i> Create First Product
      </router-link>
    </template>
  </DataTable>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useProductStore } from '@/stores/productStore';
import { useCategoryStore } from '@/stores/categoryStore';
import { useBrandStore } from '@/stores/brandStore';
import { useUnitStore } from '@/stores/unitStore';
import { useUserStore } from '@/stores/userStore';
import { ProductStatus, ProductStatusLabels } from '@/enums/productStatus';
import { ProductType, ProductTypeLabels } from '@/enums/productType';
import defaultImage from '@/assets/img/products/default-product.jpg';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import DataTable from '@/components/Common/DataTable.vue'; // Adjust path

const props = defineProps({
  products: { type: Array as any, required: true },
  loading: { type: Boolean, default: false },
  pagination: { type: Object as any, required: true },
  sortBy: { type: String, default: '' },
  sortOrder: { type: String as () => 'asc' | 'desc', default: 'asc' },
});

const emit = defineEmits(['page-change', 'size-change', 'sort-change', 'bulk-delete']);

const productStore = useProductStore();
const categoryStore = useCategoryStore();
const brandStore = useBrandStore();
const unitStore = useUnitStore();
const userStore = useUserStore();

const tableColumns = [
  { field: 'name', label: 'NAME', sortable: true, minWidth: '150px' },
  { field: 'code', label: 'CODE', sortable: true, minWidth: '100px' },
  { field: 'sku', label: 'SKU', sortable: true, minWidth: '100px' },
  { field: 'categoryName', label: 'CATEGORY', sortable: true, minWidth: '150px' },
  { field: 'brandName', label: 'BRAND', sortable: true, minWidth: '150px' },
  { field: 'productUnitName', label: 'UNIT', sortable: true, minWidth: '100px' },
  { field: 'status', label: 'STATUS', sortable: true, minWidth: '120px' },
  { field: 'actions', label: 'ACTIONS', sortable: false, minWidth: '150px', headerClass: 'pe-0', cellClass: 'text-end pe-0' }
];

const canView = computed(() => userStore.userPermissions.includes('PRODUCT_VIEW'));
const canEdit = computed(() => userStore.userPermissions.includes('PRODUCT_EDIT'));
const canDelete = computed(() => userStore.userPermissions.includes('PRODUCT_DELETE'));

const getProductImage = (product: any) => {
  if (product.mediaImages?.length > 0) return product.mediaImages[0].thumbnailUrl || product.mediaImages[0].url;
  return product.productImage || defaultImage;
};

const handleImageError = (event: Event) => { (event.target as HTMLImageElement).src = defaultImage; };
const getCategoryName = (id: number) => categoryStore.categories.find(c => c.id === id)?.name || '—';
const getBrandName = (id?: number) => id ? brandStore.brands.find(b => b.id === id)?.name || '—' : '—';
const getUnitName = (id: number) => unitStore.units.find(u => u.id === id)?.name || '—';
const getStatusLabel = (s: ProductStatus) => ProductStatusLabels[s] || s;
const getProductTypeLabel = (t: ProductType) => ProductTypeLabels[t] || t;

const getStatusClass = (status: ProductStatus) => {
  switch (status) {
    case ProductStatus.ACTIVE: return 'bg-success';
    case ProductStatus.INACTIVE: return 'bg-warning';
    case ProductStatus.DISCONTINUED: return 'bg-danger';
    default: return 'bg-secondary';
  }
};

const confirmDelete = async (id: number) => {
  if (!confirm('Are you sure you want to delete this product?')) return;
  try {
    await productStore.removeProduct(id);
    emit('page-change', 0);
  } catch (error) {
    alert('Failed to delete product.');
  }
};
</script>

<style scoped>
/* Specific UI element styles preserved from original ProductList */
.product-image-container {
  width: 45px;
  height: 45px;
  position: relative;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.image-count-badge {
  font-size: 8px;
  padding: 2px 4px;
}

.badge {
  font-size: 0.75em;
  padding: 0.25em 0.6em;
}

.btn-sm {
  width: 32px;
  height: 32px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 768px) {
  .product-image-container {
    width: 40px;
    height: 40px;
  }
}
</style>