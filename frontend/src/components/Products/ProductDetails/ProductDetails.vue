<template>
  <div class="card border-0 shadow-none rounded-1 mb-40 overflow-hidden" v-if="product">
    <div class="card-body p-xl-40 p-lg-30 p-20">
      <!-- Header Section -->
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
        <div class="d-flex flex-wrap justify-content-between align-items-start gap-3">
          <div>
            <h1 class="h2 mb-2 fw-semibold">{{ product.name }}</h1>
            <div class="d-flex flex-wrap gap-2 mt-1">
              <span class="badge" :class="getStatusClass(product.status)">
                {{ getStatusLabel(product.status) }}
              </span>
              <span class="badge bg-secondary">{{ getProductTypeLabel(product.productType) }}</span>
              <span class="text-muted">ID: {{ product.id }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="row gx-xxl-6 gy-4">
        <!-- Left column: Images & Quick Info -->
        <div class="col-xl-4 col-lg-5">
          <!-- Image Gallery -->
          <div class="single-product-img mb-4">
            <div class="main-image-container text-center mb-3 bg-light rounded-1 p-3">
              <img :src="currentImage" :alt="product.name" class="img-fluid rounded-1"
                style="max-height: 350px; object-fit: contain;" @error="handleImageError" />
            </div>
            <div v-if="product.mediaImages && product.mediaImages.length > 1" class="image-thumbnails">
              <h6 class="fs-14 fw-semibold mb-3">Product Images ({{ product.mediaImages.length }})</h6>
              <div class="d-flex flex-wrap gap-2">
                <div v-for="(media, index) in product.mediaImages" :key="media.id"
                  class="thumbnail rounded-1 overflow-hidden border cursor-pointer"
                  :class="{ 'border-primary border-2': currentImageIndex === index }" @click="changeImage(index)"
                  style="width: 70px; height: 70px;">
                  <img :src="media.thumbnailUrl || media.url" :alt="media.originalFilename"
                    class="w-100 h-100 object-fit-cover" />
                </div>
              </div>
            </div>
            <div v-else-if="!product.mediaImages?.length" class="alert alert-light text-center py-4">
              <i class="fas fa-image fa-2x text-muted mb-2"></i>
              <p class="mb-2">No images uploaded</p>
              <router-link :to="`/edit-product/${product.id}`" class="btn btn-sm btn-outline-primary">
                <i class="fas fa-plus me-1"></i> Add Images
              </router-link>
            </div>
          </div>

          <!-- Enhanced Quick Info Card -->
          <div class="quick-info-card p-3 mb-4">
            <h6 class="quick-info-title mb-3">
              <i class="fas fa-bolt me-2"></i> Quick Info
            </h6>
            <div class="row g-3">
              <div class="col-6">
                <div class="info-item">
                  <div class="info-icon"><i class="fas fa-qrcode"></i></div>
                  <div class="info-label">Code</div>
                  <div class="info-value">{{ product.code }}</div>
                </div>
              </div>
              <div class="col-6">
                <div class="info-item">
                  <div class="info-icon"><i class="fas fa-barcode"></i></div>
                  <div class="info-label">SKU</div>
                  <div class="info-value">{{ product.sku || '—' }}</div>
                </div>
              </div>
              <div class="col-6">
                <div class="info-item">
                  <div class="info-icon"><i class="fas fa-upc"></i></div>
                  <div class="info-label">Barcode</div>
                  <div class="info-value">{{ product.barcode || '—' }}</div>
                </div>
              </div>
              <div class="col-6">
                <div class="info-item">
                  <div class="info-icon"><i class="fas fa-cubes"></i></div>
                  <div class="info-label">Base Unit</div>
                  <div class="info-value">{{ unitName }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Right column: Tabs -->
        <div class="col-xl-8 col-lg-7">
          <!-- Warehouse Info Alert (if selected) -->
          <div v-if="warehouseId" class="alert alert-info mb-4 py-2 d-flex align-items-center rounded-1">
            <i class="fas fa-warehouse me-2"></i>
            <span>Showing data for warehouse: <strong>{{ warehouseName || warehouseId }}</strong></span>
          </div>

          <!-- Tabs Navigation (responsive with scale underline) -->
          <ul class="nav table-tablist mb-4" role="tablist">
            <li class="nav-item" role="presentation">
              <button class="nav-link active" id="general-tab" data-bs-toggle="tab" data-bs-target="#general"
                type="button" role="tab">
                General Info
              </button>
            </li>
            <li v-if="warehouseId" class="nav-item" role="presentation">
              <button class="nav-link" id="price-tab" data-bs-toggle="tab" data-bs-target="#price" type="button"
                role="tab">
                Price
              </button>
            </li>
            <li v-if="warehouseId" class="nav-item" role="presentation">
              <button class="nav-link" id="stock-tab" data-bs-toggle="tab" data-bs-target="#stock" type="button"
                role="tab">
                Stock
              </button>
            </li>
            <li v-if="warehouseId" class="nav-item" role="presentation">
              <button class="nav-link" id="tax-tab" data-bs-toggle="tab" data-bs-target="#tax" type="button" role="tab">
                Tax
              </button>
            </li>
          </ul>

          <div class="tab-content">
            <!-- General Info Tab (all company-level fields) -->
            <div class="tab-pane fade show active" id="general" role="tabpanel">
              <div class="card border-0 shadow-none rounded-1">
                <div class="card-body p-0">
                  <div class="table-responsive">
                    <table class="table single-product-table mb-0">
                      <tbody>
                        <tr>
                          <th>Product Name</th>
                          <td>{{ product.name }}</td>
                        </tr>
                        <tr>
                          <th>Code</th>
                          <td>{{ product.code }}</td>
                        </tr>
                        <tr>
                          <th>SKU</th>
                          <td>{{ product.sku || '—' }}</td>
                        </tr>
                        <tr>
                          <th>Barcode</th>
                          <td>{{ product.barcode || '—' }}</td>
                        </tr>
                        <tr>
                          <th>Category</th>
                          <td>{{ categoryName }}</td>
                        </tr>
                        <tr>
                          <th>Brand</th>
                          <td>{{ brandName }}</td>
                        </tr>
                        <tr>
                          <th>Product Type</th>
                          <td>{{ getProductTypeLabel(product.productType) }}</td>
                        </tr>
                        <tr>
                          <th>Status</th>
                          <td>{{ getStatusLabel(product.status) }}</td>
                        </tr>
                        <tr>
                          <th>Base Unit</th>
                          <td>{{ unitName }}</td>
                        </tr>
                        <tr>
                          <th>Sales Unit</th>
                          <td>{{ getUnitName(product.salesUnitId) }}</td>
                        </tr>
                        <tr>
                          <th>Purchase Unit</th>
                          <td>{{ getUnitName(product.purchaseUnitId) }}</td>
                        </tr>
                        <tr>
                          <th>Unit Conversion Factor</th>
                          <td>{{ product.unitConversionFactor || '1.00' }}</td>
                        </tr>
                        <tr>
                          <th>Weight</th>
                          <td>{{ product.weight ? product.weight + ' kg' : '—' }}</td>
                        </tr>
                        <tr>
                          <th>Volume</th>
                          <td>{{ product.volume ? product.volume + ' m³' : '—' }}</td>
                        </tr>
                        <tr>
                          <th>Dimensions</th>
                          <td>{{ product.dimensions || '—' }}</td>
                        </tr>
                        <tr>
                          <th>Batch Managed</th>
                          <td>{{ product.isBatchManaged ? 'Yes' : 'No' }}</td>
                        </tr>
                        <tr>
                          <th>Serialized</th>
                          <td>{{ product.isSerialized ? 'Yes' : 'No' }}</td>
                        </tr>
                        <tr>
                          <th>Composite</th>
                          <td>{{ product.isComposite ? 'Yes' : 'No' }}</td>
                        </tr>
                        <tr>
                          <th>Has Variants</th>
                          <td>{{ product.hasVariants ? 'Yes' : 'No' }}</td>
                        </tr>
                        <tr>
                          <th>Description</th>
                          <td>{{ product.description || 'No description' }}</td>
                        </tr>
                        <tr>
                          <th>Created</th>
                          <td>{{ formatDate(product.createdAt) }} <span v-if="product.createdBy">(by User {{
                              product.createdBy }})</span></td>
                        </tr>
                        <tr>
                          <th>Last Updated</th>
                          <td>{{ formatDate(product.updatedAt) }} <span v-if="product.updatedBy">(by User {{
                              product.updatedBy }})</span></td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <div class="mt-4 text-end">
                    <router-link :to="`/edit-product/${product.id}`" class="btn style-two btn-sm">
                      <i class="fas fa-edit me-1"></i> Edit General Info
                    </router-link>
                  </div>
                </div>
              </div>
            </div>

            <!-- Price Tab (all fields) -->
            <div class="tab-pane fade" id="price" role="tabpanel">
              <div class="card border-0 shadow-none rounded-1">
                <div class="card-body p-0">
                  <div v-if="product.price" class="price-details">
                    <div class="table-responsive">
                      <table class="table single-product-table mb-0">
                        <tbody>
                          <tr>
                            <th>Price</th>
                            <td>{{ product.price.currencySymbol || '₹' }} {{ product.price.price.toFixed(2) }}</td>
                          </tr>
                          <tr>
                            <th>Cost Price</th>
                            <td>{{ product.price.currencySymbol || '₹' }} {{ product.price.cost?.toFixed(2) || '—' }}
                            </td>
                          </tr>
                          <tr>
                            <th>Min Price</th>
                            <td>{{ product.price.minPrice ? product.price.currencySymbol + ' ' + product.price.minPrice
                              : '—' }}</td>
                          </tr>
                          <tr>
                            <th>Max Price</th>
                            <td>{{ product.price.maxPrice ? product.price.currencySymbol + ' ' + product.price.maxPrice
                              : '—' }}</td>
                          </tr>
                          <tr>
                            <th>Currency</th>
                            <td>{{ product.price.currencyCode }} ({{ product.price.currencySymbol }})</td>
                          <tr>
                          <tr>
                            <th>Price List</th>
                            <td>{{ product.price.priceList || 'Default' }}</td>
                          </tr>
                          <tr>
                            <th>Channel</th>
                            <td>{{ product.price.channel || '—' }}</td>
                          </tr>
                          <tr>
                            <th>Customer Group</th>
                            <td>{{ product.price.customerGroup || '—' }}</td>
                          </tr>
                          <tr>
                            <th>Min Quantity</th>
                            <td>{{ product.price.minQuantity || 1 }}</td>
                          </tr>
                          <tr>
                            <th>Max Quantity</th>
                            <td>{{ product.price.maxQuantity || '∞' }}</td>
                          </tr>
                          <tr>
                            <th>Valid From</th>
                            <td>{{ product.price.validFrom ? formatDate(product.price.validFrom) : '—' }}</td>
                          </tr>
                          <tr>
                            <th>Valid To</th>
                            <td>{{ product.price.validTo ? formatDate(product.price.validTo) : '—' }}</td>
                          </tr>
                          <tr>
                            <th>Active</th>
                            <td>{{ product.price.isActive ? 'Yes' : 'No' }}</td>
                          </tr>
                          <tr>
                            <th>Created At</th>
                            <td>{{ product.price.createdAt ? formatDate(product.price.createdAt) : '—' }}</td>
                          </tr>
                          <tr>
                            <th>Updated At</th>
                            <td>{{ product.price.updatedAt ? formatDate(product.price.updatedAt) : '—' }}</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="mt-4 text-end">
                      <router-link
                        :to="{ path: '/products/manage-to-warehouse', query: { productId: product.id, warehouseId, event: 'price' } }"
                        class="btn style-two btn-sm">
                        <i class="fas fa-edit me-1"></i> Edit Price
                      </router-link>
                    </div>
                  </div>
                  <div v-else class="text-center py-5">
                    <i class="fas fa-tag fa-3x text-muted mb-3"></i>
                    <h5 class="fw-semibold">Price not configured</h5>
                    <p class="text-muted">No price record exists for this product in the selected warehouse.</p>
                    <router-link
                      :to="{ path: '/products/manage-to-warehouse', query: { productId: product.id, warehouseId, event: 'price' } }"
                      class="btn style-one mt-2">
                      <i class="fas fa-plus me-1"></i> Configure Price
                    </router-link>
                  </div>
                </div>
              </div>
            </div>

            <!-- Stock Tab (all fields) -->
            <div class="tab-pane fade" id="stock" role="tabpanel">
              <div class="card border-0 shadow-none rounded-1">
                <div class="card-body p-0">
                  <div v-if="product.stock" class="stock-details">
                    <div class="table-responsive">
                      <table class="table single-product-table mb-0">
                        <tbody>
                          <tr>
                            <th>On Hand Quantity</th>
                            <td
                              :class="{ 'text-danger': product.stock.quantity <= 0, 'text-success': product.stock.quantity > 0 }">
                              {{ product.stock.quantity }}</td>
                          </tr>
                          <tr>
                            <th>Available Quantity</th>
                            <td>{{ product.stock.availableQuantity }}</td>
                          </tr>
                          <tr>
                            <th>Reserved Quantity</th>
                            <td>{{ product.stock.reservedQuantity || 0 }}</td>
                          </tr>
                          <tr>
                            <th>In Transit Quantity</th>
                            <td>{{ product.stock.inTransitQuantity || 0 }}</td>
                          </tr>
                          <tr>
                            <th>Committed Quantity</th>
                            <td>{{ product.stock.committedQuantity || 0 }}</td>
                          </tr>
                          <tr>
                            <th>Min Stock Level</th>
                            <td>{{ product.stock.minStockLevel || '—' }}</td>
                          </tr>
                          <tr>
                            <th>Max Stock Level</th>
                            <td>{{ product.stock.maxStockLevel || '—' }}</td>
                          </tr>
                          <tr>
                            <th>Reorder Level</th>
                            <td>{{ product.stock.reorderLevel || '—' }}</td>
                          </tr>
                          <tr>
                            <th>Stock Alert</th>
                            <td>{{ product.stock.stockAlert ? 'Yes' : 'No' }}</td>
                          </tr>
                          <tr>
                            <th>Average Cost</th>
                            <td>{{ product.stock.averageCost ? '$' + product.stock.averageCost : '—' }}</td>
                          </tr>
                          <tr>
                            <th>Last Count Date</th>
                            <td>{{ product.stock.lastCountDate ? formatDate(product.stock.lastCountDate) : '—' }}</td>
                            </td>
                          </tr>
                          <tr>
                            <th>Next Count Date</th>
                            <td>{{ product.stock.nextCountDate ? formatDate(product.stock.nextCountDate) : '—' }}</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="mt-4 text-end">
                      <router-link
                        :to="{ path: '/products/manage-to-warehouse', query: { productId: product.id, warehouseId, event: 'stock' } }"
                        class="btn style-two btn-sm">
                        <i class="fas fa-edit me-1"></i> Edit Stock
                      </router-link>
                    </div>
                  </div>
                  <div v-else class="text-center py-5">
                    <i class="fas fa-boxes fa-3x text-muted mb-3"></i>
                    <h5 class="fw-semibold">Stock not configured</h5>
                    <p class="text-muted">No stock record exists for this product in the selected warehouse.</p>
                    <router-link
                      :to="{ path: '/products/manage-to-warehouse', query: { productId: product.id, warehouseId, event: 'stock' } }"
                      class="btn style-one mt-2">
                      <i class="fas fa-plus me-1"></i> Configure Stock
                    </router-link>
                  </div>
                </div>
              </div>
            </div>

            <!-- Tax Tab (all fields) -->
            <div class="tab-pane fade" id="tax" role="tabpanel">
              <div class="card border-0 shadow-none rounded-1">
                <div class="card-body p-0">
                  <div v-if="product.tax" class="tax-details">
                    <div class="table-responsive">
                      <table class="table single-product-table mb-0">
                        <tbody>
                          <tr>
                            <th>Tax Rate</th>
                            <td>{{ product.tax.taxRate }}%</td>
                          </tr>
                          <tr>
                            <th>Tax Type</th>
                            <td>{{ product.tax.taxType }}</td>
                          </tr>
                          <tr>
                            <th>Tax Code</th>
                            <td>{{ product.tax.taxCode }}</td>
                          </tr>
                          <tr>
                            <th>Tax Name</th>
                            <td>{{ product.tax.taxName }}</td>
                          </tr>
                          <tr>
                            <th>Is Inclusive</th>
                            <td>{{ product.tax.isInclusive ? 'Yes' : 'No' }}</td>
                          </tr>
                          <tr>
                            <th>Is Compound</th>
                            <td>{{ product.tax.isCompound ? 'Yes' : 'No' }}</td>
                          </tr>
                          <tr>
                            <th>Is Active</th>
                            <td>{{ product.tax.isActive ? 'Yes' : 'No' }}</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="mt-4 text-end">
                      <router-link
                        :to="{ path: '/products/manage-to-warehouse', query: { productId: product.id, warehouseId, event: 'tax' } }"
                        class="btn style-two btn-sm">
                        <i class="fas fa-edit me-1"></i> Edit Tax
                      </router-link>
                    </div>
                  </div>
                  <div v-else class="text-center py-5">
                    <i class="fas fa-percent fa-3x text-muted mb-3"></i>
                    <h5 class="fw-semibold">Tax not configured</h5>
                    <p class="text-muted">No tax record exists for this product in the selected warehouse.</p>
                    <router-link
                      :to="{ path: '/products/manage-to-warehouse', query: { productId: product.id, warehouseId, event: 'tax' } }"
                      class="btn style-one mt-2">
                      <i class="fas fa-plus me-1"></i> Configure Tax
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Loading & Error states -->
  <div v-else-if="loading" class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body text-center py-5">
      <div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div>
      <p class="mt-3 text-muted">Loading product details...</p>
    </div>
  </div>
  <div v-else class="card border-0 shadow-none rounded-1 mb-40">
    <div class="card-body text-center py-5">
      <i class="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
      <h3 class="mb-3">Product Not Found</h3>
      <p class="text-muted mb-4">The product you're looking for doesn't exist or has been removed.</p>
      <router-link to="/products" class="btn style-one"><i class="fas fa-arrow-left me-2"></i> Back to
        Products</router-link>
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
import { useWarehouseStore } from '@/stores/warehouseStore';
import type { ProductResponse } from '@/types/Product';
import { ProductStatus, ProductStatusLabels } from '@/enums/productStatus';
import { ProductType, ProductTypeLabels } from '@/enums/productType';
import defaultImage from '@/assets/img/products/default-product.jpg';

export default defineComponent({
  name: 'ProductDetails',
  props: {
    warehouseId: { type: Number, default: undefined },
  },
  setup(props) {
    const route = useRoute();
    const productStore = useProductStore();
    const categoryStore = useCategoryStore();
    const brandStore = useBrandStore();
    const unitStore = useUnitStore();
    const warehouseStore = useWarehouseStore();

    const product = ref<ProductResponse | null>(null);
    const loading = ref(true);
    const currentImageIndex = ref(0);
    const warehouseName = ref<string | null>(null);

    const fetchProductDetails = async () => {
      const id = Number(route.params.id);
      if (!id) return;

      loading.value = true;
      try {
        await Promise.all([
          categoryStore.fetchCategories(),
          brandStore.fetchBrands(),
          unitStore.fetchUnits(),
        ]);
        if (props.warehouseId) {
          await warehouseStore.fetchWarehouses();
          const wh = warehouseStore.warehouses.find(w => w.id === props.warehouseId);
          warehouseName.value = wh?.name || `Warehouse ${props.warehouseId}`;
        }
        const result = await productStore.fetchProductById(id, props.warehouseId);
        if (result) product.value = result;
      } catch (error) {
        console.error('Failed to fetch product details:', error);
      } finally {
        loading.value = false;
      }
    };

    const currentImage = computed(() => {
      if (!product.value) return defaultImage;
      if (product.value.mediaImages?.length) {
        const media = product.value.mediaImages[currentImageIndex.value];
        return media.url || defaultImage;
      }
      return product.value.productImage || defaultImage;
    });

    const changeImage = (index: number) => { currentImageIndex.value = index; };
    const handleImageError = (event: Event) => { (event.target as HTMLImageElement).src = defaultImage; };

    const formatDate = (dateString?: string | null): string => {
      if (!dateString) return '—';
      try {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
      } catch { return dateString; }
    };

    const getStatusLabel = (status: ProductStatus) => ProductStatusLabels[status] || status;
    const getProductTypeLabel = (type: ProductType) => ProductTypeLabels[type] || type;
    const getStatusClass = (status: ProductStatus) => {
      switch (status) {
        case ProductStatus.ACTIVE: return 'bg-success';
        case ProductStatus.INACTIVE: return 'bg-warning';
        case ProductStatus.DISCONTINUED: return 'bg-danger';
        default: return 'bg-secondary';
      }
    };

    const getUnitName = (unitId?: number | null): string => {
      if (!unitId) return '—';
      const unit = unitStore.units.find(u => u.id === unitId);
      return unit ? `${unit.name} (${unit.shortName})` : '—';
    };

    const categoryName = computed(() => product.value?.categoryName || categoryStore.categories.find(c => c.id === product.value?.categoryId)?.name || '—');
    const brandName = computed(() => product.value?.brandName || brandStore.brands.find(b => b.id === product.value?.brandId)?.name || '—');
    const unitName = computed(() => product.value?.productUnitName || unitStore.units.find(u => u.id === product.value?.productUnitId)?.name || '—');

    onMounted(() => { fetchProductDetails(); });

    return {
      product,
      loading,
      warehouseId: props.warehouseId,
      warehouseName,
      currentImage,
      currentImageIndex,
      categoryName,
      brandName,
      unitName,
      getUnitName,
      formatDate,
      getStatusLabel,
      getProductTypeLabel,
      getStatusClass,
      handleImageError,
      changeImage,
    };
  },
});
</script>

<style scoped>
/* ========== TABLIST (RESPONSIVE WITH SCALE UNDERLINE) ========== */
.table-tablist {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  border-bottom: 1px solid #dadada;
  padding-bottom: 0;
  margin-bottom: 1.5rem;
}

.table-tablist .nav-item {
  margin-right: 0;
}

.table-tablist .nav-link {
  display: inline-block;
  position: relative;
  color: #000;
  font-size: 13px;
  font-weight: 500;
  border-radius: 4px;
  background: #e8e9f5;
  line-height: 1.2;
  padding: 8px 18px;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.2s;
  overflow: hidden;
}

.table-tablist .nav-link:after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 3px;
  background: #4f46e5;
  transform: scaleX(0);
  transition: transform 0.2s ease;
}

.table-tablist .nav-link.active {
  background-color: transparent;
  border: 1px solid #4f46e5;
  box-shadow: 0px 4px 20px 0px rgba(79, 70, 229, 0.2);
}

.table-tablist .nav-link.active:after {
  transform: scaleX(1);
}

@media (min-width: 576px) {
  .table-tablist .nav-link {
    padding: 10px 24px;
    font-size: 14px;
  }

  .table-tablist .nav-link:after {
    height: 3px;
  }
}

@media (min-width: 992px) {
  .table-tablist .nav-link {
    padding: 13px 36px;
    font-size: 14px;
  }

  .table-tablist .nav-link:after {
    height: 4px;
  }
}

/* ========== TABLES INSIDE TABS (RESPONSIVE) ========== */
.single-product-table {
  width: 100%;
  min-width: 500px;
}

@media (max-width: 576px) {
  .single-product-table {
    min-width: 400px;
  }
}

.card-body .table-responsive {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

/* ========== QUICK INFO CARD (RESPONSIVE) ========== */
.quick-info-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  border-radius: 20px;
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05), 0 8px 10px -6px rgba(0, 0, 0, 0.02);
  transition: transform 0.2s, box-shadow 0.2s;
  border: 1px solid rgba(99, 102, 241, 0.1);
}

.quick-info-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 20px 30px -12px rgba(0, 0, 0, 0.1);
}

.quick-info-title {
  font-size: 1rem;
  font-weight: 700;
  color: #1e293b;
  border-left: 4px solid #4f46e5;
  padding-left: 12px;
  margin-bottom: 1rem;
  letter-spacing: -0.2px;
}

.info-item {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  padding: 10px 12px;
  transition: all 0.2s;
  backdrop-filter: blur(2px);
}

.info-item:hover {
  background: white;
  transform: translateY(-1px);
}

.info-icon {
  font-size: 1.25rem;
  color: #4f46e5;
  margin-bottom: 6px;
}

.info-label {
  font-size: 0.65rem;
  text-transform: uppercase;
  font-weight: 600;
  letter-spacing: 0.5px;
  color: #64748b;
  margin-bottom: 4px;
}

.info-value {
  font-size: 0.9rem;
  font-weight: 700;
  color: #0f172a;
  word-break: break-word;
  line-height: 1.3;
}

/* ========== OTHER EXISTING STYLES ========== */
.single-product-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #495057;
  width: 200px;
}

.thumbnail {
  cursor: pointer;
  transition: transform 0.2s, border-color 0.2s;
}

.thumbnail:hover {
  transform: scale(1.05);
}

.border-primary {
  border-color: #4f46e5 !important;
}

.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
}

@media (max-width: 768px) {
  .card-body {
    padding: 1.25rem !important;
  }

  .single-product-table th {
    width: 140px;
  }
}

.main-image-container img {
  max-width: 100%;
  height: auto;
}

@media (max-width: 576px) {
  .quick-info-card .row {
    --bs-gutter-x: 0.5rem;
  }

  .info-item {
    padding: 8px 10px;
  }

  .info-icon {
    font-size: 1rem;
  }

  .info-value {
    font-size: 0.8rem;
  }
}
</style>