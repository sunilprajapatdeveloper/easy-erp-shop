<template>
  <div class="card border-0 shadow-none rounded-1 mb-40" v-if="product">
    <div class="card-body p-xl-60">
      <!-- Breadcrumb/Header -->
      <div class="mb-30">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-2">
            <li class="breadcrumb-item">
              <router-link to="/products" class="text-decoration-none text-secondary">
                <i class="fas fa-arrow-left me-2"></i> Back to Products
              </router-link>
            </li>
          </ol>
        </nav>
        <div class="d-flex justify-content-between align-items-center">
          <h1 class="h3 mb-0">{{ product.name }}</h1>
          <div class="d-flex gap-2">
            <router-link :to="`/edit-product/${product.id}`" class="btn btn-primary">
              <i class="fas fa-edit me-2"></i> Edit Product
            </router-link>
            <router-link v-if="product.mediaImages && product.mediaImages.length > 0"
              :to="`/product-images/${product.id}`" class="btn btn-info">
              <i class="fas fa-images me-2"></i> Manage Images
            </router-link>
          </div>
        </div>
        <div class="d-flex align-items-center gap-3 mt-2">
          <span class="badge" :class="getStatusClass(product.status)">
            {{ getStatusLabel(product.status) }}
          </span>
          <span class="badge bg-secondary">{{ getProductTypeLabel(product.productType) }}</span>
          <small class="text-muted">ID: {{ product.id }}</small>
        </div>
      </div>

      <div class="row gx-xxl-6">
        <!-- Product Images Section -->
        <div class="col-xl-4 col-lg-5 pe-xxl-1">
          <div class="single-product-img mb-4">
            <!-- Main Image -->
            <div class="main-image-container text-center mb-3">
              <img :src="currentImage" :alt="product.name" class="img-fluid rounded"
                style="max-height: 400px; object-fit: contain;" @error="handleImageError" />
            </div>

            <!-- Image Thumbnails -->
            <div v-if="product.mediaImages && product.mediaImages.length > 1" class="image-thumbnails">
              <h6 class="fs-14 text-black mb-3">Product Images ({{ product.mediaImages.length }})</h6>
              <div class="d-flex flex-wrap gap-2">
                <div v-for="(media, index) in product.mediaImages" :key="media.id" class="thumbnail-container"
                  :class="{ 'active': currentImageIndex === index }" @click="changeImage(index)">
                  <img :src="media.thumbnailUrl || media.url" :alt="media.originalFilename" class="img-thumbnail"
                    style="width: 80px; height: 80px; object-fit: cover; cursor: pointer;" />
                </div>
              </div>
            </div>

            <!-- No Images Message -->
            <div v-else-if="!product.mediaImages || product.mediaImages.length === 0"
              class="alert alert-light text-center">
              <i class="fas fa-image fa-2x text-muted mb-3"></i>
              <p class="mb-0">No images uploaded for this product</p>
              <router-link :to="`/edit-product/${product.id}`" class="btn btn-sm btn-outline-primary mt-2">
                <i class="fas fa-plus me-1"></i> Add Images
              </router-link>
            </div>
          </div>

          <!-- Quick Info Card -->
          <div class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <h6 class="card-title mb-3">Quick Info</h6>
              <div class="row">
                <div class="col-6 mb-2">
                  <small class="text-muted d-block">Code</small>
                  <strong>{{ product.code }}</strong>
                </div>
                <div class="col-6 mb-2">
                  <small class="text-muted d-block">SKU</small>
                  <strong>{{ product.sku || '—' }}</strong>
                </div>
                <div class="col-6 mb-2">
                  <small class="text-muted d-block">Barcode</small>
                  <strong>{{ product.barcode || '—' }}</strong>
                </div>
                <div class="col-6 mb-2">
                  <small class="text-muted d-block">Unit</small>
                  <strong>{{ unitName }}</strong>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Product Details Section -->
        <div class="col-xl-8 col-lg-7 ps-xxl-6">
          <!-- Tabs for different sections -->
          <ul class="nav nav-tabs mb-4" id="productTabs" role="tablist">
            <li class="nav-item" role="presentation">
              <button class="nav-link active" id="details-tab" data-bs-toggle="tab" data-bs-target="#details"
                type="button" role="tab">
                <i class="fas fa-info-circle me-2"></i> Details
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button class="nav-link" id="pricing-tab" data-bs-toggle="tab" data-bs-target="#pricing" type="button"
                role="tab">
                <i class="fas fa-tag me-2"></i> Pricing
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button class="nav-link" id="stock-tab" data-bs-toggle="tab" data-bs-target="#stock" type="button"
                role="tab">
                <i class="fas fa-warehouse me-2"></i> Stock
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button class="nav-link" id="attributes-tab" data-bs-toggle="tab" data-bs-target="#attributes"
                type="button" role="tab">
                <i class="fas fa-list me-2"></i> Attributes
              </button>
            </li>
          </ul>

          <div class="tab-content" id="productTabsContent">
            <!-- Details Tab -->
            <div class="tab-pane fade show active" id="details" role="tabpanel">
              <table class="table single-product-table mb-0">
                <tbody>
                  <tr>
                    <th scope="row" style="width: 200px;">Product Name</th>
                    <td>{{ product.name }}</td>
                  </tr>
                  <tr>
                    <th scope="row">Category</th>
                    <td>{{ categoryName }}</td>
                  </tr>
                  <tr>
                    <th scope="row">Brand</th>
                    <td>{{ brandName }}</td>
                  </tr>
                  <tr>
                    <th scope="row">Description</th>
                    <td>
                      <div v-if="product.description" class="product-description">
                        {{ product.description }}
                      </div>
                      <span v-else class="text-muted">No description provided</span>
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">Created</th>
                    <td>
                      <div v-if="product.createdAt">
                        {{ formatDate(product.createdAt) }}
                        <span v-if="product.createdBy" class="text-muted">by User {{ product.createdBy }}</span>
                      </div>
                      <span v-else class="text-muted">—</span>
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">Last Updated</th>
                    <td>
                      <div v-if="product.updatedAt">
                        {{ formatDate(product.updatedAt) }}
                        <span v-if="product.updatedBy" class="text-muted">by User {{ product.updatedBy }}</span>
                      </div>
                      <span v-else class="text-muted">—</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <!-- Pricing Tab -->
            <div class="tab-pane fade" id="pricing" role="tabpanel">
              <div v-if="product.prices && product.prices.length > 0" class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>Price List</th>
                      <th>Price</th>
                      <th>Currency</th>
                      <th>Min Qty</th>
                      <th>Valid From</th>
                      <th>Valid To</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="price in product.prices" :key="price.id">
                      <td>{{ price.priceList || 'Default' }}</td>
                      <td class="fw-bold">₹{{ price.price.toFixed(2) }}</td>
                      <td>{{ price.currencyCode || 'INR' }}</td>
                      <td>{{ price.minQuantity || '—' }}</td>
                      <td>{{ price.validFrom ? formatDate(price.validFrom) : '—' }}</td>
                      <td>{{ price.validTo ? formatDate(price.validTo) : '—' }}</td>
                      <td>
                        <span :class="price.isActive ? 'badge bg-success' : 'badge bg-secondary'">
                          {{ price.isActive ? 'Active' : 'Inactive' }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div v-else class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i>
                No pricing information available.
                <router-link :to="`/product-pricing/${product.id}`" class="alert-link">Add pricing</router-link>
              </div>
            </div>

            <!-- Stock Tab -->
            <div class="tab-pane fade" id="stock" role="tabpanel">
              <div v-if="product.stocks && product.stocks.length > 0" class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>Warehouse</th>
                      <th>Available Qty</th>
                      <th>Reserved</th>
                      <th>In Transit</th>
                      <th>Committed</th>
                      <th>Min Stock</th>
                      <th>Reorder Level</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="stock in product.stocks" :key="stock.id">
                      <td>{{ stock.warehouseId || 'Default' }}</td>
                      <td :class="{
                        'text-danger': stock.availableQuantity <= 0,
                        'text-warning': stock.availableQuantity > 0 && stock.availableQuantity <= (stock.minStockLevel || 10),
                        'text-success': stock.availableQuantity > (stock.minStockLevel || 10)
                      }">
                        <strong>{{ stock.availableQuantity }}</strong>
                      </td>
                      <td>{{ stock.reservedQuantity || 0 }}</td>
                      <td>{{ stock.inTransitQuantity || 0 }}</td>
                      <td>{{ stock.committedQuantity || 0 }}</td>
                      <td>{{ stock.minStockLevel || '—' }}</td>
                      <td>{{ stock.reorderLevel || '—' }}</td>
                      <td>
                        <span :class="stock.stockAlert ? 'badge bg-danger' : 'badge bg-success'">
                          {{ stock.stockAlert ? 'Alert' : 'Normal' }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div v-else class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i>
                No stock information available.
                <router-link :to="`/product-stock/${product.id}`" class="alert-link">Manage stock</router-link>
              </div>
            </div>

            <!-- Attributes Tab -->
            <div class="tab-pane fade" id="attributes" role="tabpanel">
              <div class="row">
                <div class="col-md-6">
                  <table class="table">
                    <tbody>
                      <tr>
                        <th scope="row" style="width: 180px;">Product Type</th>
                        <td>{{ getProductTypeLabel(product.productType) }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Sales Unit</th>
                        <td>{{ getUnitName(product.salesUnitId) }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Purchase Unit</th>
                        <td>{{ getUnitName(product.purchaseUnitId) }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Conversion Factor</th>
                        <td>{{ product.unitConversionFactor || '1.00' }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div class="col-md-6">
                  <table class="table">
                    <tbody>
                      <tr>
                        <th scope="row" style="width: 180px;">Weight</th>
                        <td>{{ product.weight || '—' }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Volume</th>
                        <td>{{ product.volume || '—' }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Dimensions</th>
                        <td>{{ product.dimensions || '—' }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              <!-- Product Features/Flags -->
              <div class="card border-0 shadow-sm mt-4">
                <div class="card-body">
                  <h6 class="card-title mb-3">Product Features</h6>
                  <div class="row">
                    <div class="col-md-3 mb-2">
                      <div class="form-check">
                        <input class="form-check-input" type="checkbox" :checked="product.isBatchManaged" disabled>
                        <label class="form-check-label">Batch Managed</label>
                      </div>
                    </div>
                    <div class="col-md-3 mb-2">
                      <div class="form-check">
                        <input class="form-check-input" type="checkbox" :checked="product.isSerialized" disabled>
                        <label class="form-check-label">Serialized Item</label>
                      </div>
                    </div>
                    <div class="col-md-3 mb-2">
                      <div class="form-check">
                        <input class="form-check-input" type="checkbox" :checked="product.isComposite" disabled>
                        <label class="form-check-label">Composite Product</label>
                      </div>
                    </div>
                    <div class="col-md-3 mb-2">
                      <div class="form-check">
                        <input class="form-check-input" type="checkbox" :checked="product.hasVariants" disabled>
                        <label class="form-check-label">Has Variants</label>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Loading State -->
  <div v-else-if="loading" class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body p-xl-60 text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
      <p class="mt-3 text-muted">Loading product details...</p>
    </div>
  </div>

  <!-- Error State -->
  <div v-else class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body p-xl-60 text-center">
      <i class="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
      <h3 class="mb-3">Product Not Found</h3>
      <p class="text-muted mb-4">The product you're looking for doesn't exist or has been removed.</p>
      <router-link to="/products" class="btn btn-primary">
        <i class="fas fa-arrow-left me-2"></i> Back to Products
      </router-link>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { useProductStore } from '@/stores/productStore';
import { useCategoryStore } from '@/stores/categoryStore';
import { useBrandStore } from '@/stores/brandStore';
import { useUnitStore } from '@/stores/unitStore';
import type { ProductResponse } from '@/types/Product';
import { ProductStatus, ProductStatusLabels } from '@/enums/productStatus';
import { ProductType, ProductTypeLabels } from '@/enums/productType';
import defaultImage from '@/assets/img/products/default-product.jpg';

export default defineComponent({
  name: 'ProductDetails',
  setup() {
    const route = useRoute();
    const productStore = useProductStore();
    const categoryStore = useCategoryStore();
    const brandStore = useBrandStore();
    const unitStore = useUnitStore();

    const product = ref<ProductResponse | null>(null);
    const loading = ref(true);
    const currentImageIndex = ref(0);

    const fetchProductDetails = async () => {
      const id = Number(route.params.id);
      if (!id) return;

      loading.value = true;

      try {
        // Load necessary data
        await Promise.all([
          categoryStore.fetchCategories(),
          brandStore.fetchBrands(),
          unitStore.fetchUnits(),
        ]);

        const result = await productStore.fetchProductById(id);
        if (result) {
          product.value = result;
        }
      } catch (error) {
        console.error('Failed to fetch product details:', error);
      } finally {
        loading.value = false;
      }
    };

    // Get current image for display
    const currentImage = computed(() => {
      if (!product.value) return defaultImage;

      // If we have media images
      if (product.value.mediaImages && product.value.mediaImages.length > 0) {
        const media = product.value.mediaImages[currentImageIndex.value];
        return media.url || defaultImage;
      }

      // Fall back to legacy productImage field
      if (product.value.productImage) {
        return product.value.productImage;
      }

      // Default placeholder
      return defaultImage;
    });

    const changeImage = (index: number) => {
      if (product.value?.mediaImages && index < product.value.mediaImages.length) {
        currentImageIndex.value = index;
      }
    };

    const handleImageError = (event: Event) => {
      const img = event.target as HTMLImageElement;
      img.src = defaultImage;
    };

    const formatDate = (dateString: string): string => {
      try {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'short',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit'
        });
      } catch (error) {
        return dateString;
      }
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

    const categoryName = computed(() => {
      if (!product.value) return '—';

      // First try categoryName from product response
      if (product.value.categoryName) {
        return product.value.categoryName;
      }

      // Fall back to looking up by ID
      if (product.value.categoryId) {
        const category = categoryStore.categories.find(c => c.id === product.value!.categoryId);
        return category?.name || '—';
      }

      return '—';
    });

    const brandName = computed(() => {
      if (!product.value) return '—';

      // First try brandName from product response
      if (product.value.brandName) {
        return product.value.brandName;
      }

      // Fall back to looking up by ID
      if (product.value.brandId) {
        const brand = brandStore.brands.find(b => b.id === product.value!.brandId);
        return brand?.name || '—';
      }

      return '—';
    });

    const unitName = computed(() => {
      if (!product.value) return '—';

      // First try productUnitName from product response
      if (product.value.productUnitName) {
        return product.value.productUnitName;
      }

      // Fall back to looking up by ID
      if (product.value.productUnitId) {
        const unit = unitStore.units.find(u => u.id === product.value!.productUnitId);
        return unit ? `${unit.name} (${unit.shortName})` : '—';
      }

      return '—';
    });

    const getUnitName = (unitId?: number | null): string => {
      if (!unitId) return '—';
      const unit = unitStore.units.find(u => u.id === unitId);
      return unit ? `${unit.name} (${unit.shortName})` : '—';
    };

    // Initialize Bootstrap tabs
    onMounted(async () => {
      await fetchProductDetails();

      // Initialize Bootstrap tabs if available
      if (window.bootstrap) {
        const tabEl = document.querySelector('#productTabs button[data-bs-toggle="tab"]');
        if (tabEl) {
          new window.bootstrap.Tab(tabEl);
        }
      }
    });

    return {
      product,
      loading,
      currentImage,
      currentImageIndex,
      categoryName,
      brandName,
      unitName,
      formatDate,
      getStatusLabel,
      getProductTypeLabel,
      getStatusClass,
      getUnitName,
      handleImageError,
      changeImage,
    };
  },
});
</script>

<style scoped>
.single-product-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #495057;
}

.single-product-table td {
  vertical-align: middle;
}

.product-description {
  white-space: pre-line;
  line-height: 1.6;
}

.thumbnail-container {
  position: relative;
  border: 2px solid transparent;
  border-radius: 4px;
  transition: border-color 0.2s ease;
}

.thumbnail-container.active {
  border-color: #007bff;
}

.thumbnail-container:hover {
  border-color: #dee2e6;
}

.nav-tabs .nav-link {
  color: #6c757d;
  font-weight: 500;
  border: none;
  padding: 0.75rem 1rem;
}

.nav-tabs .nav-link.active {
  color: #007bff;
  border-bottom: 2px solid #007bff;
  background-color: transparent;
}

.table-hover tbody tr:hover {
  background-color: rgba(0, 123, 255, 0.05);
}

.badge {
  font-size: 0.75em;
  padding: 0.35em 0.65em;
  font-weight: 500;
}

.form-check-input:disabled {
  background-color: #e9ecef;
  border-color: #ced4da;
}

.form-check-input:disabled:checked {
  background-color: #0d6efd;
  border-color: #0d6efd;
}

@media (max-width: 768px) {
  .card-body {
    padding: 1.5rem !important;
  }

  .nav-tabs .nav-link {
    padding: 0.5rem;
    font-size: 0.875rem;
  }
}
</style>