<template>
  <form @submit.prevent="submit" class="pb-60">
    <div class="row">
      <div class="col-xxl-9 col-xl-8 col-lg-8 pe-xxl-6 mb-md-25">
        <div class="row gx-xxl-6">
          <!-- Product Name with AI button -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Product Name</label>
              <div class="d-flex gap-2">
                <input v-model="product.name" type="text"
                  class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter Product Name"
                  required />
                <button type="button" class="btn btn-outline-primary d-flex align-items-center gap-1"
                  @click="generateWithAI" :disabled="!product.name || aiGenerating" style="white-space: nowrap;">
                  <span v-if="aiGenerating" class="spinner-border spinner-border-sm" role="status"
                    aria-hidden="true"></span>
                  <i v-else class="ri-robot-line"></i>
                  <span class="d-none d-md-inline">Generate with AI</span>
                </button>
              </div>
            </div>
          </div>

          <!-- Product Code -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Code Product</label>
              <input v-model="product.code" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Scan Code"
                required />
            </div>
          </div>

          <!-- SKU -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">SKU</label>
              <input v-model="product.sku" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter SKU" />
            </div>
          </div>

          <!-- Barcode with Lookup button -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Barcode</label>
              <div class="d-flex gap-2">
                <input v-model="product.barcode" type="text"
                  class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter Barcode" />
                <button type="button" class="btn btn-outline-secondary d-flex align-items-center" @click="lookupBarcode"
                  :disabled="!product.barcode || lookupLoading">
                  <span v-if="lookupLoading" class="spinner-border spinner-border-sm" role="status"
                    aria-hidden="true"></span>
                  <i v-else class="ri-search-line"></i>
                </button>
              </div>
            </div>
          </div>

          <!-- Category -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Category</label>
              <select v-model.number="product.categoryId" class="bg-white border-0 rounded-1 fs-14 text-optional"
                required>
                <option disabled :value="0">Choose Category</option>
                <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
              </select>
            </div>
          </div>

          <!-- Brand -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Brand</label>
              <select v-model.number="product.brandId" class="bg-white border-0 rounded-1 fs-14 text-optional">
                <option disabled :value="null">Choose Brand</option>
                <option v-for="brand in brands" :key="brand.id" :value="brand.id">{{ brand.name }}</option>
              </select>
            </div>
          </div>

          <!-- Product Type -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Product Type</label>
              <select v-model="product.productType" class="bg-white border-0 rounded-1 fs-14 text-optional" required>
                <option disabled value="">Choose Product Type</option>
                <option v-for="(label, key) in ProductTypeLabels" :key="key" :value="key">
                  {{ label }}
                </option>
              </select>
            </div>
          </div>

          <!-- Status -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Status</label>
              <select v-model="product.status" class="bg-white border-0 rounded-1 fs-14 text-optional" required>
                <option disabled value="">Choose Status</option>
                <option v-for="(label, key) in ProductStatusLabels" :key="key" :value="key">
                  {{ label }}
                </option>
              </select>
            </div>
          </div>

          <!-- Units -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Product Unit</label>
              <select v-model.number="product.productUnitId" class="bg-white border-0 rounded-1 fs-14 text-optional"
                required>
                <option disabled :value="0">Choose Product Unit</option>
                <option v-for="unit in units" :key="unit.id" :value="unit.id">
                  {{ unit.name }} ({{ unit.shortName }})
                </option>
              </select>
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Sales Unit</label>
              <select v-model.number="product.salesUnitId" class="bg-white border-0 rounded-1 fs-14 text-optional">
                <option disabled :value="null">Choose Sales Unit</option>
                <option v-for="unit in units" :key="unit.id" :value="unit.id">
                  {{ unit.name }} ({{ unit.shortName }})
                </option>
              </select>
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Purchase Unit</label>
              <select v-model.number="product.purchaseUnitId" class="bg-white border-0 rounded-1 fs-14 text-optional">
                <option disabled :value="null">Choose Purchase Unit</option>
                <option v-for="unit in units" :key="unit.id" :value="unit.id">
                  {{ unit.name }} ({{ unit.shortName }})
                </option>
              </select>
            </div>
          </div>

          <!-- Flags -->
          <div class="col-lg-12 mb-25">
            <label class="d-block fs-14 text-black mb-2">Product Features</label>
            <div class="d-flex flex-wrap gap-3">
              <div class="form-check form-check-inline">
                <input class="form-check-input" type="checkbox" v-model="product.isBatchManaged" id="flagBatch" />
                <label class="form-check-label" for="flagBatch">Batch Managed</label>
              </div>

              <div class="form-check form-check-inline">
                <input class="form-check-input" type="checkbox" v-model="product.isSerialized" id="flagSerialized" />
                <label class="form-check-label" for="flagSerialized">Serialized Item</label>
              </div>

              <div class="form-check form-check-inline">
                <input class="form-check-input" type="checkbox" v-model="product.isComposite" id="flagComposite" />
                <label class="form-check-label" for="flagComposite">Composite Product</label>
              </div>

              <div class="form-check form-check-inline">
                <input class="form-check-input" type="checkbox" v-model="product.hasVariants" id="flagVariants" />
                <label class="form-check-label" for="flagVariants">Has Variants</label>
              </div>
            </div>
          </div>

          <!-- Physical -->
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Weight</label>
              <input v-model="product.weight" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Weight" />
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Volume</label>
              <input v-model="product.volume" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Volume" />
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Dimensions</label>
              <input v-model="product.dimensions" type="text"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="LxWxH" />
            </div>
          </div>

          <!-- Description (AI can fill this) -->
          <div class="col-lg-12">
            <div class="form-group mb-25">
              <label class="d-block fs-14 text-black mb-2">Description</label>
              <textarea v-model="product.description" rows="3"
                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                placeholder="Add description"></textarea>
            </div>
          </div>

          <!-- AI Suggestion Preview (optional) -->
          <div v-if="aiSuggestions" class="col-12 mb-25">
            <div class="alert alert-info">
              <h6 class="fw-semibold">✨ AI Suggestions</h6>
              <p><strong>Category:</strong> {{ aiSuggestions.categoryName || 'Not suggested' }}</p>
              <p><strong>Brand:</strong> {{ aiSuggestions.brandName || 'Not suggested' }}</p>
              <p><strong>Description:</strong> {{ aiSuggestions.description }}</p>
              <button type="button" class="btn btn-sm btn-outline-primary" @click="applyAISuggestions">
                Apply All
              </button>
            </div>
          </div>

          <!-- Submit -->
          <div class="col-12">
            <button class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16"
              type="submit" :disabled="saving">
              {{ isEditMode ? 'Update Product' : 'Submit Product' }}
            </button>
          </div>
        </div>
      </div>

      <div class="col-xxl-3 col-xl-4 col-lg-4">
        <AddImages :product-id="product.id || undefined" :existing-media="product.mediaImages || []"
          @images-uploaded="handleImagesUploaded" @image-deleted="handleImageDeleted" />
      </div>
    </div>
  </form>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import AddImages from './AddImages.vue';
import { useProductStore } from '@/stores/productStore';
import { useCategoryStore } from '@/stores/categoryStore';
import { useBrandStore } from '@/stores/brandStore';
import { useUnitStore } from '@/stores/unitStore';
import type { CreateProductRequest, UpdateProductRequest, ProductResponse } from '@/types/Product';
import { ProductType, ProductTypeLabels } from '@/enums/productType';
import { ProductStatus, ProductStatusLabels } from '@/enums/productStatus';

export default defineComponent({
  name: 'CreateProduct',
  components: { AddImages },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const productStore = useProductStore();
    const categoryStore = useCategoryStore();
    const brandStore = useBrandStore();
    const unitStore = useUnitStore();

    const productId = route.params.id ? Number(route.params.id) : null;
    const isEditMode = computed(() => !!productId);
    const saving = ref(false);
    const aiGenerating = ref(false);
    const lookupLoading = ref(false);
    const aiSuggestions = ref<any>(null); // store AI-generated suggestions

    // Initialize product
    const product = ref<Partial<ProductResponse>>({
      name: '',
      code: '',
      sku: '',
      barcode: '',
      categoryId: 0,
      brandId: null,
      productType: ProductType.STOCK,
      status: ProductStatus.ACTIVE,
      productUnitId: 0,
      salesUnitId: null,
      purchaseUnitId: null,
      isBatchManaged: false,
      isSerialized: false,
      isComposite: false,
      hasVariants: false,
      weight: '',
      volume: '',
      dimensions: '',
      description: '',
      productImage: '',
      imageUrls: [],
      mediaImages: [],
      isDeleted: false,
    });

    // AI generation method
    const generateWithAI = async () => {
      if (!product.value.name) return;
      aiGenerating.value = true;
      aiSuggestions.value = null;

      try {
        // Mock AI call – replace with actual backend endpoint
        // In production, you might send the product name and receive suggestions
        await new Promise(resolve => setTimeout(resolve, 1500)); // simulate delay

        // Example mock response
        const mockSuggestions = {
          categoryName: 'Electronics',
          brandName: 'Samsung',
          description: 'High-quality electronic device with advanced features.',
          // optionally include categoryId, brandId if you can map
        };

        // Find matching category and brand IDs (if available)
        const matchedCategory = categoryStore.categories.find(
          c => c.name.toLowerCase().includes(mockSuggestions.categoryName.toLowerCase())
        );
        const matchedBrand = brandStore.brands.find(
          b => b.name.toLowerCase().includes(mockSuggestions.brandName.toLowerCase())
        );

        aiSuggestions.value = {
          categoryId: matchedCategory?.id || null,
          brandId: matchedBrand?.id || null,
          categoryName: matchedCategory?.name || mockSuggestions.categoryName,
          brandName: matchedBrand?.name || mockSuggestions.brandName,
          description: mockSuggestions.description,
        };
      } catch (error) {
        console.error('AI generation failed:', error);
        alert('Failed to generate suggestions. Please try again.');
      } finally {
        aiGenerating.value = false;
      }
    };

    // Apply AI suggestions to product
    const applyAISuggestions = () => {
      if (!aiSuggestions.value) return;
      if (aiSuggestions.value.categoryId) product.value.categoryId = aiSuggestions.value.categoryId;
      if (aiSuggestions.value.brandId) product.value.brandId = aiSuggestions.value.brandId;
      if (aiSuggestions.value.description) product.value.description = aiSuggestions.value.description;
      aiSuggestions.value = null; // clear after applying
    };

    // Barcode lookup
    const lookupBarcode = async () => {
      if (!product.value.barcode) return;
      lookupLoading.value = true;

      try {
        // Mock external API call – replace with real service like upcitemdb.com
        await new Promise(resolve => setTimeout(resolve, 1000));

        // Mock response
        const mockProductInfo = {
          name: 'Sample Product from Barcode',
          brand: 'Generic',
          description: 'This product was found via barcode lookup.',
          category: 'Miscellaneous',
        };

        // Optionally update product fields
        if (!product.value.name) product.value.name = mockProductInfo.name;
        if (!product.value.description) product.value.description = mockProductInfo.description;

        // Try to match category
        const matchedCategory = categoryStore.categories.find(
          c => c.name.toLowerCase().includes(mockProductInfo.category.toLowerCase())
        );
        if (matchedCategory && !product.value.categoryId) {
          product.value.categoryId = matchedCategory.id;
        }

        alert('Barcode lookup successful!');
      } catch (error) {
        console.error('Barcode lookup failed:', error);
        alert('Failed to lookup barcode. Please try again.');
      } finally {
        lookupLoading.value = false;
      }
    };

    // Existing methods (unchanged)
    const handleImagesUploaded = (uploadedMedia: any[]) => {
      if (!product.value.mediaImages) product.value.mediaImages = [];
      product.value.mediaImages = [...product.value.mediaImages, ...uploadedMedia];
    };

    const handleImageDeleted = (mediaId: string) => {
      if (product.value.mediaImages) {
        product.value.mediaImages = product.value.mediaImages.filter(img => img.id !== mediaId);
      }
    };

    const loadProduct = async () => {
      if (productId) {
        try {
          const fetched = await productStore.fetchProductById(productId);
          if (fetched) {
            product.value = { ...product.value, ...fetched, mediaImages: fetched.mediaImages || [] };
          }
        } catch (error) {
          console.error('Failed to load product:', error);
          alert('Failed to load product details');
        }
      }
    };

    const submit = async () => {
      if (saving.value) return;
      saving.value = true;

      try {
        if (isEditMode.value && productId) {
          const { mediaImages, ...updateDataWithoutMedia } = product.value;
          const updateData: UpdateProductRequest = {
            id: productId,
            name: product.value.name!,
            code: product.value.code!,
            sku: product.value.sku!,
            barcode: product.value.barcode,
            categoryId: product.value.categoryId!,
            brandId: product.value.brandId || null,
            productType: product.value.productType!,
            status: product.value.status!,
            productUnitId: product.value.productUnitId!,
            salesUnitId: product.value.salesUnitId || null,
            purchaseUnitId: product.value.purchaseUnitId || null,
            isBatchManaged: product.value.isBatchManaged || false,
            isSerialized: product.value.isSerialized || false,
            isComposite: product.value.isComposite || false,
            hasVariants: product.value.hasVariants || false,
            weight: product.value.weight,
            volume: product.value.volume,
            dimensions: product.value.dimensions,
            description: product.value.description,
            productImage: product.value.productImage,
            imageUrls: product.value.imageUrls,
            isDeleted: product.value.isDeleted || false,
          };
          await productStore.updateProduct(productId, updateData);
          alert('Product updated successfully!');
        } else {
          const { mediaImages, ...createDataWithoutMedia } = product.value;
          const createData: CreateProductRequest = {
            name: product.value.name!,
            code: product.value.code!,
            sku: product.value.sku!,
            barcode: product.value.barcode,
            categoryId: product.value.categoryId!,
            brandId: product.value.brandId || null,
            productType: product.value.productType!,
            status: product.value.status!,
            productUnitId: product.value.productUnitId!,
            salesUnitId: product.value.salesUnitId || null,
            purchaseUnitId: product.value.purchaseUnitId || null,
            isBatchManaged: product.value.isBatchManaged || false,
            isSerialized: product.value.isSerialized || false,
            isComposite: product.value.isComposite || false,
            hasVariants: product.value.hasVariants || false,
            weight: product.value.weight,
            volume: product.value.volume,
            dimensions: product.value.dimensions,
            description: product.value.description,
            productImage: product.value.productImage,
            imageUrls: product.value.imageUrls || [],
            isDeleted: false,
          };
          const newProduct = await productStore.addProduct(createData);
          alert('Product created successfully!');
          if (newProduct?.id) router.push(`/products/edit/${newProduct.id}`);
        }
      } catch (err: any) {
        console.error('Save failed:', err);
        alert(`Failed to save product: ${err.message || 'Unknown error'}`);
      } finally {
        saving.value = false;
      }
    };

    const categories = computed(() => categoryStore.categories);
    const brands = computed(() => brandStore.brands);
    const units = computed(() => unitStore.units);

    onMounted(async () => {
      await Promise.all([
        categoryStore.fetchCategories(),
        brandStore.fetchBrands(),
        unitStore.fetchUnits(),
        loadProduct(),
      ]);
    });

    return {
      product,
      submit,
      categories,
      brands,
      units,
      isEditMode,
      saving,
      handleImagesUploaded,
      handleImageDeleted,
      ProductTypeLabels,
      ProductStatusLabels,
      generateWithAI,
      aiGenerating,
      aiSuggestions,
      applyAISuggestions,
      lookupBarcode,
      lookupLoading,
    };
  },
});
</script>