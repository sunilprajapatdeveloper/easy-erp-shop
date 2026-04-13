<template>
  <DataTable :rows="products" :loading="loading" :pagination="pagination" :columns="dynamicColumns" :sort-by="sortBy"
    :sort-order="sortOrder" :show-bulk-actions="true" selection-label="IMAGE" item-name="product"
    :can-delete="canDelete" @sort-change="$emit('sort-change', $event)" @page-change="$emit('page-change', $event)"
    @size-change="$emit('size-change', $event)" @bulk-delete="$emit('bulk-delete', $event)">

    <!-- Checkbox add-on (image) -->
    <template #checkbox-add-on="{ row }">
      <div class="product-image-container">
        <img :src="getProductImage(row)" :alt="row.name" class="product-image" @error="handleImageError" />
        <span v-if="row.mediaImages?.length > 0"
          class="badge bg-secondary position-absolute top-0 end-0 image-count-badge">
          {{ row.mediaImages.length }}
        </span>
      </div>
    </template>

    <!-- Name column -->
    <template #cell(name)="{ row }">
      <div class="d-flex flex-column">
        <strong>{{ row.name }}</strong>
        <small class="text-muted">{{ getProductTypeLabel(row.productType) }}</small>
      </div>
    </template>

    <!-- Code column -->
    <template #cell(code)="{ value }">
      <code>{{ value }}</code>
    </template>

    <!-- SKU column -->
    <template #cell(sku)="{ value }">
      <small class="text-muted">{{ value || '—' }}</small>
    </template>

    <!-- Category Name column -->
    <template #cell(categoryName)="{ row }">
      {{ row.categoryName || getCategoryName(row.categoryId) }}
    </template>

    <!-- Brand Name column -->
    <template #cell(brandName)="{ row }">
      {{ row.brandName || getBrandName(row.brandId) }}
    </template>

    <!-- Unit column -->
    <template #cell(productUnitName)="{ row }">
      {{ row.productUnitName || getUnitName(row.productUnitId) }}
    </template>

    <!-- PRICE column – actual price or configure button -->
    <template v-if="showWarehouseColumns" #cell(priceStatus)="{ row }">
      <div v-if="getPriceConfig(row)">
        <span class="fw-semibold">{{ formatPrice(getPriceConfig(row)) }}</span>
        <small class="text-muted d-block">{{ getPriceCurrency(getPriceConfig(row)) }}</small>
      </div>
      <button v-else class="btn btn-sm btn-outline-primary configure-btn" @click="navigateToConfigure(row, 'price')">
        <FontAwesomeIcon icon="plus-circle" class="me-1" /> Configure Price
      </button>
    </template>

    <!-- STOCK column – actual stock or configure button -->
    <template v-if="showWarehouseColumns" #cell(stockStatus)="{ row }">
      <div v-if="getStockConfig(row)">
        <span class="fw-semibold">{{ getStockQuantity(getStockConfig(row)) }}</span>
        <small class="text-muted d-block">on hand</small>
      </div>
      <button v-else class="btn btn-sm btn-outline-primary configure-btn" @click="navigateToConfigure(row, 'stock')">
        <FontAwesomeIcon icon="plus-circle" class="me-1" /> Configure Stock
      </button>
    </template>

    <!-- TAX column – actual tax rate or configure button -->
    <template v-if="showWarehouseColumns" #cell(taxStatus)="{ row }">
      <div v-if="getTaxConfig(row)">
        <span class="fw-semibold">{{ getTaxRate(getTaxConfig(row)) }}%</span>
        <small class="text-muted d-block">{{ getTaxType(getTaxConfig(row)) }}</small>
      </div>
      <button v-else class="btn btn-sm btn-outline-primary configure-btn" @click="navigateToConfigure(row, 'tax')">
        <FontAwesomeIcon icon="plus-circle" class="me-1" /> Configure Tax
      </button>
    </template>

    <!-- Status column (product status) -->
    <template #cell(status)="{ value }">
      <span :class="getStatusClass(value)" class="badge">{{ getStatusLabel(value) }}</span>
    </template>

    <!-- Actions column -->
    <template #cell(actions)="{ row }">
      <div class="button-group d-flex flex-wrap align-items-center">
        <router-link v-if="canView" :to="{
          path: `/product-details/${row.id}`,
          query: warehouseId ? {
            warehouseId: String(warehouseId),
            includePrice: 'true',
            includeStock: 'true',
            includeTax: 'true'
          } : undefined
        }" class="btn btn-sm btn-outline-secondary" title="View">
          <FontAwesomeIcon icon="eye" />
        </router-link>
        <router-link v-if="canEdit" :to="`/edit-product/${row.id}`" class="btn btn-sm btn-outline-primary" title="Edit">
          <FontAwesomeIcon icon="edit" />
        </router-link>
        <button v-if="canDelete" class="btn btn-sm btn-outline-danger" title="Delete"
          @click.prevent="confirmDelete(row.id)">
          <FontAwesomeIcon icon="trash" />
        </button>
      </div>
    </template>

    <!-- Empty action slot -->
    <template #empty-action>
      <router-link v-if="canEdit" to="/create-product" class="btn btn-sm btn-primary mt-2">
        <i class="fas fa-plus me-1"></i> Create First Product
      </router-link>
    </template>
  </DataTable>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useProductStore } from '@/stores/productStore';
import { useCategoryStore } from '@/stores/categoryStore';
import { useBrandStore } from '@/stores/brandStore';
import { useUnitStore } from '@/stores/unitStore';
import { useUserStore } from '@/stores/userStore';
import { ProductStatus, ProductStatusLabels } from '@/enums/productStatus';
import { ProductType, ProductTypeLabels } from '@/enums/productType';
import defaultImage from '@/assets/img/products/default-product.jpg';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import DataTable from '@/components/Common/DataTable.vue';

const router = useRouter();

const props = defineProps({
  products: { type: Array as any, required: true },
  loading: { type: Boolean, default: false },
  pagination: { type: Object as any, required: true },
  sortBy: { type: String, default: '' },
  sortOrder: { type: String as () => 'asc' | 'desc', default: 'asc' },
  warehouseId: { type: Number, default: null },
});

const emit = defineEmits(['page-change', 'size-change', 'sort-change', 'bulk-delete']);

const productStore = useProductStore();
const categoryStore = useCategoryStore();
const brandStore = useBrandStore();
const unitStore = useUnitStore();
const userStore = useUserStore();

// Show warehouse columns only if a warehouse is selected
const showWarehouseColumns = computed(() => {
  return props.warehouseId !== null && props.warehouseId !== undefined && props.warehouseId > 0;
});

// Helper to get price/stock/tax for selected warehouse (objects or arrays fallback)
const getWarehouseConfig = (product: any) => {
  if (!showWarehouseColumns.value) return null;
  const warehouseId = props.warehouseId;

  let price = product.price ?? null;
  let stock = product.stock ?? null;
  let tax = product.tax ?? null;

  if (!price && product.prices?.length) {
    price = product.prices.find((p: any) => p.warehouseId === warehouseId) || null;
  }
  if (!stock && product.stocks?.length) {
    stock = product.stocks.find((s: any) => s.warehouseId === warehouseId) || null;
  }
  if (!tax && product.taxes?.length) {
    tax = product.taxes.find((t: any) => t.warehouseId === warehouseId) || null;
  }
  return { price, stock, tax };
};

// Individual config getters
const getPriceConfig = (product: any) => getWarehouseConfig(product)?.price;
const getStockConfig = (product: any) => getWarehouseConfig(product)?.stock;
const getTaxConfig = (product: any) => getWarehouseConfig(product)?.tax;

// Price formatting
const formatPrice = (priceConfig: any) => {
  if (!priceConfig) return '—';
  const amount = priceConfig.price;
  const currency = priceConfig.currencySymbol || priceConfig.currencyCode || '';
  return `${currency} ${Number(amount).toFixed(2)}`;
};
const getPriceCurrency = (priceConfig: any) => priceConfig?.currencyCode || '';

// Stock – use `quantity` (on‑hand stock)
const getStockQuantity = (stockConfig: any) => {
  if (!stockConfig) return '—';
  return stockConfig.quantity ?? 0;
};

// Tax – rate and type
const getTaxRate = (taxConfig: any) => taxConfig?.taxRate ?? 0;
const getTaxType = (taxConfig: any) => taxConfig?.taxType || '';

// Navigation to configure page
const navigateToConfigure = (product: any, event: 'price' | 'stock' | 'tax') => {
  router.push({
    path: '/products/manage-to-warehouse',
    query: {
      productId: String(product.id),
      warehouseId: String(props.warehouseId),
      event,
    },
  });
};

// Base columns (always visible)
const baseColumns = [
  { field: 'name', label: 'NAME', sortable: true, minWidth: '150px' },
  { field: 'code', label: 'CODE', sortable: true, minWidth: '100px' },
  { field: 'sku', label: 'SKU', sortable: true, minWidth: '100px' },
  { field: 'categoryName', label: 'CATEGORY', sortable: true, minWidth: '150px' },
  { field: 'brandName', label: 'BRAND', sortable: true, minWidth: '150px' },
  { field: 'productUnitName', label: 'UNIT', sortable: true, minWidth: '100px' },
  { field: 'status', label: 'STATUS', sortable: true, minWidth: '120px' },
  { field: 'actions', label: 'ACTIONS', sortable: false, minWidth: '150px', headerClass: 'pe-0', cellClass: 'text-end pe-0' }
];

// Warehouse columns (dynamic)
const warehouseColumns = [
  { field: 'priceStatus', label: 'PRICE', sortable: false, minWidth: '130px' },
  { field: 'stockStatus', label: 'STOCK', sortable: false, minWidth: '130px' },
  { field: 'taxStatus', label: 'TAX', sortable: false, minWidth: '130px' }
];

const dynamicColumns = computed(() => {
  if (showWarehouseColumns.value) {
    const statusIndex = baseColumns.findIndex(col => col.field === 'status');
    return [
      ...baseColumns.slice(0, statusIndex),
      ...warehouseColumns,
      ...baseColumns.slice(statusIndex)
    ];
  }
  return baseColumns;
});

// Permissions & helpers
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

/* Modern configure button */
.configure-btn {
  width: auto;
  height: auto;
  padding: 0.25rem 0.75rem;
  font-size: 0.75rem;
  border-radius: 20px;
  background: transparent;
  border: 1px solid #6366f1;
  color: #4f46e5;
  transition: all 0.2s;
}

.configure-btn:hover {
  background: linear-gradient(105deg, #4f46e5 0%, #6366f1 100%);
  color: white;
  border-color: transparent;
  transform: translateY(-1px);
}

.configure-btn .svg-inline--fa {
  font-size: 0.7rem;
}

@media (max-width: 768px) {
  .product-image-container {
    width: 40px;
    height: 40px;
  }

  .configure-btn {
    padding: 0.2rem 0.5rem;
    font-size: 0.7rem;
  }
}
</style>