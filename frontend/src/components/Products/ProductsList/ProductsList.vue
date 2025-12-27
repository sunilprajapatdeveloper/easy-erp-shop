<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" style="min-width: 120px;" class="text-title fw-normal fs-14 pt-0 ps-0">
                IMAGE
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                NAME
              </th>
              <th scope="col" style="min-width: 100px;" class="text-title fw-normal fs-14 pt-0">
                CODE
              </th>
              <th scope="col" style="min-width: 100px;" class="text-title fw-normal fs-14 pt-0">
                SKU
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                CATEGORY
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0">
                BRAND
              </th>
              <th scope="col" style="min-width: 100px;" class="text-title fw-normal fs-14 pt-0">
                UNIT
              </th>
              <th scope="col" style="min-width: 120px;" class="text-title fw-normal fs-14 pt-0">
                STATUS
              </th>
              <th scope="col" style="min-width: 150px;" class="text-title fw-normal fs-14 pt-0 pe-0"
                v-if="canView || canEdit || canDelete">
                ACTIONS
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in products" :key="product.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="product-image-container position-relative">
                  <img :src="getProductImage(product) " :alt="product.name" class="product-image"
                    @error="handleImageError" />
                  <span v-if="product.mediaImages && product.mediaImages.length > 0"
                    class="badge bg-secondary position-absolute top-0 end-0" style="font-size: 8px; padding: 2px 4px;">
                    {{ product.mediaImages.length }}
                  </span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <div class="d-flex flex-column">
                  <strong>{{ product.name }}</strong>
                  <small class="text-muted">{{ getProductTypeLabel(product.productType) }}</small>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <code>{{ product.code }}</code>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <small class="text-muted">{{ product.sku || '—' }}</small>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ product.categoryName || getCategoryName(product.categoryId) }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ product.brandName || getBrandName(product.brandId) }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ product.productUnitName || getUnitName(product.productUnitId) }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal">
                <span :class="getStatusClass(product.status)" class="badge">
                  {{ getStatusLabel(product.status) }}
                </span>
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center justify-content-end">
                  <!-- View Button -->
                  <router-link v-if="canView" :to="`/product-details/${product.id}`"
                    class="btn btn-sm btn-outline-secondary me-1" title="View">
                    <FontAwesomeIcon icon="eye" />
                  </router-link>

                  <!-- Edit Button -->
                  <router-link v-if="canEdit" :to="`/edit-product/${product.id}`"
                    class="btn btn-sm btn-outline-primary me-1" title="Edit">
                    <FontAwesomeIcon icon="edit" />
                  </router-link>

                  <!-- Delete Button -->
                  <button v-if="canDelete" class="btn btn-sm btn-outline-danger" title="Delete"
                    @click.prevent="confirmDelete(product.id)">
                    <FontAwesomeIcon icon="trash" />
                  </button>
                </div>
              </td>
            </tr>

            <!-- Loading State -->
            <tr v-if="loading">
              <td colspan="9" class="text-center py-4">
                <div class="spinner-border text-primary" role="status">
                  <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2 text-muted">Loading products...</p>
              </td>
            </tr>

            <!-- Empty State -->
            <tr v-if="!loading && products.length === 0">
              <td colspan="9" class="text-center py-4">
                <div class="text-muted">
                  <i class="fas fa-box-open fa-2x mb-3"></i>
                  <p class="mb-0">No products found</p>
                  <router-link v-if="canEdit" to="/create-product" class="btn btn-sm btn-primary mt-2">
                    <i class="fas fa-plus me-1"></i> Create First Product
                  </router-link>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, computed } from 'vue';
import { useProductStore } from '@/stores/productStore';
import { useCategoryStore } from '@/stores/categoryStore';
import { useBrandStore } from '@/stores/brandStore';
import { useUnitStore } from '@/stores/unitStore';
import { useUserStore } from '@/stores/userStore';
import type { ProductResponse } from '@/types/Product';
import { ProductStatus, ProductStatusLabels } from '@/enums/productStatus';
import { ProductType, ProductTypeLabels } from '@/enums/productType';
import defaultImage from '@/assets/img/products/default-product.jpg';

export default defineComponent({
  name: 'ProductsList',

  setup() {
    const productStore = useProductStore();
    const categoryStore = useCategoryStore();
    const brandStore = useBrandStore();
    const unitStore = useUnitStore();
    const userStore = useUserStore();

    // Computed properties
    const products = computed(() => productStore.products);
    const loading = computed(() => productStore.loading);
    const categories = computed(() => categoryStore.categories);
    const brands = computed(() => brandStore.brands);
    const units = computed(() => unitStore.units);

    const canView = computed(() => userStore.userPermissions.includes('PRODUCT_VIEW'));
    const canEdit = computed(() => userStore.userPermissions.includes('PRODUCT_EDIT'));
    const canDelete = computed(() => userStore.userPermissions.includes('PRODUCT_DELETE'));

    // Helper functions
    const getProductImage = (product: ProductResponse): string => {
      // Try media images first (from mediaImages array)
      if (product.mediaImages && product.mediaImages.length > 0) {
        return product.mediaImages[0].thumbnailUrl || product.mediaImages[0].url;
      }

      // Fall back to legacy productImage field (if it exists)
      if (product.productImage) {
        return product.productImage;
      }

      // Default placeholder
      return defaultImage;
    };

    const handleImageError = (event: Event) => {
      const img = event.target as HTMLImageElement;
      img.src = defaultImage;
    };

    const getCategoryName = (categoryId: number): string => {
      if (!categoryId) return '—';
      const category = categories.value.find(c => c.id === categoryId);
      return category?.name || '—';
    };

    const getBrandName = (brandId?: number | null): string => {
      if (!brandId) return '—';
      const brand = brands.value.find(b => b.id === brandId);
      return brand?.name || '—';
    };

    const getUnitName = (unitId: number): string => {
      if (!unitId) return '—';
      const unit = units.value.find(u => u.id === unitId);
      return unit?.name || '—';
    };

    const getStatusLabel = (status: ProductStatus): string => {
      return ProductStatusLabels[status] || status;
    };

    const getProductTypeLabel = (type: ProductType): string => {
      return ProductTypeLabels[type] || type;
    };

    const getStatusClass = (status: ProductStatus): string => {
      switch (status) {
        case ProductStatus.ACTIVE: return 'bg-success';
        case ProductStatus.INACTIVE: return 'bg-warning';
        case ProductStatus.DISCONTINUED: return 'bg-danger';
        default: return 'bg-secondary';
      }
    };

    const confirmDelete = async (id: number) => {
      if (!confirm('Are you sure you want to delete this product? This action cannot be undone.')) {
        return;
      }

      try {
        await productStore.removeProduct(id);
        alert('Product deleted successfully.');
      } catch (error) {
        console.error('Delete failed:', error);
        alert('Failed to delete product.');
      }
    };

    // Initialize
    onMounted(async () => {
      await Promise.all([
        productStore.fetchProducts(),
        categoryStore.fetchCategories(),
        brandStore.fetchBrands(),
        unitStore.fetchUnits()
      ]);
    });

    return {
      // Computed
      products,
      loading,
      categories,
      brands,
      units,
      canView,
      canEdit,
      canDelete,

      // Functions
      getProductImage,
      handleImageError,
      getCategoryName,
      getBrandName,
      getUnitName,
      getStatusLabel,
      getProductTypeLabel,
      getStatusClass,
      confirmDelete,
    };
  }
});
</script>

<style scoped>
.product-image-container {
  width: 60px;
  height: 60px;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.badge {
  font-size: 0.75em;
  padding: 0.25em 0.6em;
}

.table th {
  border-top: none;
  border-bottom: 2px solid #dee2e6;
}

.table td {
  vertical-align: middle;
}

.btn {
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
}

.btn-sm {
  width: 32px;
  height: 32px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 768px) {
  .table-responsive {
    font-size: 0.875rem;
  }

  .product-image-container {
    width: 40px;
    height: 40px;
  }
}
</style>