<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Edit Purchase Return" />

    <!-- Form -->
    <ChooseForm :warehouses="warehouses" :suppliers="suppliers" v-model:warehouseId="warehouseId"
      v-model:supplierId="supplierId" v-model:date="date" v-model:originalPurchaseId="originalPurchaseId"
      :isCreateMode="false" @add-product="addProduct" @load-purchase="loadPurchase" />

    <!-- Selected Products -->
    <SelectedProducts v-if="products.length > 0" v-model:products="products" />

    <!-- Submit Section -->
    <SubmitPurchase v-model:returnTax="returnTax" v-model:returnDiscount="returnDiscount"
      v-model:shippingCost="shippingCost" v-model:returnStatus="returnStatus" v-model:shipmentStatus="shipmentStatus"
      :products="products" v-model:note="note" @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- Success Popup -->
  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Image" />
        <span class="text-white fw-medium">Purchase return has been successfully updated</span>
      </div>
    </div>
  </div>
  <a id="triggerSuccessPopup" class="d-none" data-bs-toggle="offcanvas" href="#successPopup" role="button"></a>

  <!-- Error Popup -->
  <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
    <div class="offcanvas-body p-0">
      <div class="create-error">
        <img src="../../assets/img/icons/close-circle-2.svg" alt="Image" style="filter: brightness(0) invert(1);" />
        <span class="text-white fw-medium">{{ errorMessage }}</span>
      </div>
    </div>
  </div>
  <a id="triggerErrorPopup" class="d-none" data-bs-toggle="offcanvas" href="#errorPopup" role="button"></a>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { Offcanvas } from "bootstrap";

import type { Warehouse } from "@/types/Warehouse";
import type { Supplier } from "@/types/Supplier";
import type { Purchase, PurchaseProduct } from "@/types/Purchase";
import type { SelectedPurchaseReturnProduct, UpdatePurchaseReturnRequest } from "@/types/PurchaseReturn";

import { useWarehouseStore } from "@/stores/warehouseStore";
import { useSupplierStore } from "@/stores/supplierStore";
import { usePurchaseReturnStore } from "@/stores/purchaseReturnStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/PurchaseReturn/CreatePurchaseReturn/ChooseForm.vue";
import SelectedProducts from "@/components/PurchaseReturn/CreatePurchaseReturn/SelectedProducts.vue";
import SubmitPurchase from "@/components/PurchaseReturn/CreatePurchaseReturn/SubmitPurchase.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";

import { PurchaseStatus } from "@/enums/purchaseStatus";
import { ShipmentStatus } from "@/enums/shipmentStatus";

const route = useRoute();
const router = useRouter();
const returnId = Number(route.params.id);

const warehouseStore = useWarehouseStore();
const supplierStore = useSupplierStore();
const purchaseReturnStore = usePurchaseReturnStore();

// Form data
const warehouses = ref<Warehouse[]>([]);
const suppliers = ref<Supplier[]>([]);
const date = ref<string>(new Date().toISOString().split("T")[0]);
const warehouseId = ref<number | null>(null);
const supplierId = ref<number | null>(null);
const originalPurchaseId = ref<number | null>(null);
const products = ref<SelectedPurchaseReturnProduct[]>([]);

const returnTax = ref("0");
const returnDiscount = ref("0");
const shippingCost = ref("0");
const returnStatus = ref<PurchaseStatus>(PurchaseStatus.PENDING);
const shipmentStatus = ref<ShipmentStatus>(ShipmentStatus.PENDING);
const note = ref("");
const errorMessage = ref("Something went wrong. Please try again.");

// Fetch Warehouses and Suppliers
onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  suppliers.value = await supplierStore.fetchSuppliers();

  // Load purchase return details
  try {
    const purchaseReturn = await purchaseReturnStore.fetchPurchaseReturnById(returnId);

    if (!purchaseReturn) {
      router.push("/purchase-return-list");
      return;
    }

    date.value = purchaseReturn.date;
    warehouseId.value = purchaseReturn.warehouseId;
    supplierId.value = purchaseReturn.supplierId;
    originalPurchaseId.value = purchaseReturn.originalPurchaseId;
    returnTax.value = purchaseReturn.returnTax;
    returnDiscount.value = purchaseReturn.returnDiscount;
    shippingCost.value = purchaseReturn.shippingCost;
    returnStatus.value = purchaseReturn.returnStatus;
    shipmentStatus.value = purchaseReturn.shipmentStatus;
    note.value = purchaseReturn.note ?? "";

    // Map products
    products.value = purchaseReturn.products.map((p) => {
      const cost = parseFloat(p.productUnitCost ?? "0");
      const discount = parseFloat(p.productDiscount ?? "0");
      const tax = parseFloat(p.productTax ?? "0");
      const qty = p.returnQty ?? 0;

      const subTotal = ((cost - discount + tax) * qty).toFixed(2);

      return {
        productId: p.productId,
        productName: p.productName,
        productCode: p.productCode,
        unitCost: p.productUnitCost,
        discount: p.productTax,
        tax: p.productTax,
        subTotal,
        returnQty: p.returnQty,
        stock: qty,
        taxType: "EXCLUSIVE",
      } as SelectedPurchaseReturnProduct;
    });
  } catch (error) {
    errorMessage.value = "Failed to load purchase return.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() =>
      Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(),
      3000
    );
  }
});

// Load Original Purchase (for ChooseForm)
const loadPurchase = (purchase: Purchase) => {
  originalPurchaseId.value = purchase.id;
  warehouseId.value = purchase.warehouseId;
  supplierId.value = purchase.supplierId;
  returnTax.value = purchase.orderTax ?? "0";
  returnDiscount.value = purchase.discount ?? "0";
  shippingCost.value = purchase.shippingCost ?? "0";

  products.value = purchase.products.map((p: PurchaseProduct) => {
    const cost = parseFloat(p.productUnitCost ?? "0");
    const discount = parseFloat(p.productDiscount ?? "0");
    const tax = parseFloat(p.productTax ?? "0");

    const subTotal = ((cost - discount + tax) * p.purchaseQty).toFixed(2);

    return {
      productId: p.productId,
      productName: p.productName,
      productCode: p.productCode,
      unitCost: p.productUnitCost,
      discount: p.productDiscount,
      tax: p.productTax,
      stock: p.purchaseQty,
      subTotal,
      returnQty: p.purchaseQty,
      taxType: "EXCLUSIVE",
    } as SelectedPurchaseReturnProduct;
  });
};

// Add or Update Product
const addProduct = (product: SelectedPurchaseReturnProduct) => {
  const existing = products.value.find(p => p.productId === product.productId);

  if (existing) {
    existing.returnQty += product.returnQty;
    const cost = parseFloat(existing.unitCost ?? "0");
    const discount = parseFloat(existing.discount ?? "0");
    const tax = parseFloat(existing.tax ?? "0");
    existing.subTotal = ((cost - discount + tax) * existing.returnQty).toFixed(2);
  } else {
    const qty = product.returnQty ?? 1;
    const cost = parseFloat(product.unitCost ?? "0");
    const discount = parseFloat(product.discount ?? "0");
    const tax = parseFloat(product.tax ?? "0");
    const subTotal = ((cost - discount + tax) * qty).toFixed(2);

    products.value.push({ ...product, returnQty: qty, subTotal });
  }
};

// Submit Updated Purchase Return
const handleSubmit = async () => {
  if (!date.value || !warehouseId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : "Please add at least one product.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() =>
      Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(),
      3000
    );
    return;
  }

  const payload: UpdatePurchaseReturnRequest = {
    date: date.value ?? "",
    warehouseId: warehouseId.value ?? 0,
    supplierId: supplierId.value ?? 0,
    originalPurchaseId: originalPurchaseId.value ?? 0,
    products: products.value.map(p => {
      const unitCost = parseFloat(p.unitCost ?? "0");
      const discount = parseFloat(p.discount ?? "0");
      const tax = parseFloat(p.tax ?? "0");
      const qty = p.returnQty ?? 0;

      const subTotal = ((unitCost - discount + tax) * qty).toFixed(2);

      return {
        productId: p.productId,
        productUnitCost: p.unitCost ?? "0",
        returnQty: qty,
        productDiscount: p.discount ?? "0",
        productTax: p.tax ?? "0",
        subTotal,
      };
    }),
    orderTax: returnTax.value ?? "0",
    discount: returnDiscount.value ?? "0",
    shippingCost: shippingCost.value ?? "0",
    returnStatus: returnStatus.value ?? "PENDING",
    shipmentStatus: shipmentStatus.value ?? "PENDING",
    note: note.value ?? "",
  };

  try {
    await purchaseReturnStore.editPurchaseReturn(returnId, payload);
    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(() => {
      Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!)?.hide();
      router.push("/purchase-return-list");
    }, 3000);
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message ?? "Failed to update purchase return.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() =>
      Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(),
      3000
    );
  }
};
</script>
