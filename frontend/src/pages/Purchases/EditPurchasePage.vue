<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Edit Purchase" />

    <!-- Choose form -->
    <ChooseForm :warehouses="warehouses" :suppliers="suppliers" v-model:warehouseId="warehouseId"
      v-model:supplierId="supplierId" v-model:date="date" @add-product="addProduct" />

    <SelectedProducts v-if="products.length" v-model:products="products" />

    <SubmitPurchase v-model:orderTax="orderTax" v-model:discount="discount" v-model:shippingCost="shippingCost"
      v-model:note="note" v-model:status="purchaseStatus" :products="products" @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <!-- Success -->
  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Success" />
        <span class="text-white fw-medium">Purchase has been successfully updated</span>
      </div>
    </div>
  </div>
  <a id="triggerSuccessPopup" class="d-none" data-bs-toggle="offcanvas" href="#successPopup" role="button"></a>

  <!-- Error -->
  <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
    <div class="offcanvas-body p-0">
      <div class="create-error">
        <img src="../../assets/img/icons/close-circle-2.svg" alt="Error" style="filter: brightness(0) invert(1);" />
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

import type { SelectedPurchaseProduct, UpdatePurchaseRequest } from "@/types/Purchase";
import type { Supplier } from "@/types/Supplier";
import type { Warehouse } from "@/types/Warehouse";

import { usePurchaseStore } from "@/stores/purchaseStore";
import { useSupplierStore } from "@/stores/supplierStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useProductStore } from "@/stores/productStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/Purchases/CreatePurchase/ChooseForm.vue";
import SelectedProducts from "@/components/Purchases/CreatePurchase/SelectedProducts.vue";
import SubmitPurchase from "@/components/Purchases/CreatePurchase/SubmitPurchase.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";
import { PurchaseStatus } from "@/enums/purchaseStatus";
import { ShipmentStatus } from "@/types/Transfer";

// route
const route = useRoute();
const router = useRouter();
const purchaseId = Number(route.params.id);

// stores
const purchaseStore = usePurchaseStore();
const supplierStore = useSupplierStore();
const warehouseStore = useWarehouseStore();
const productStore = useProductStore();

// state
const date = ref<string>("");
const warehouseId = ref<number | null>(null);
const supplierId = ref<number | null>(null);
const orderTax = ref("0");
const discount = ref("0");
const shippingCost = ref("0");
const purchaseStatus = ref<PurchaseStatus>(PurchaseStatus.PENDING);
const shipmentStatus = ref<ShipmentStatus>(ShipmentStatus.PENDING);
const note = ref("");
const errorMessage = ref("Something went wrong. Please try again.");

const warehouses = ref<Warehouse[]>([]);
const suppliers = ref<Supplier[]>([]);
const products = ref<SelectedPurchaseProduct[]>([]);

// fetch data
onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  suppliers.value = await supplierStore.fetchSuppliers();

  try {
    await purchaseStore.fetchPurchaseById(purchaseId);
    const purchase = purchaseStore.selectedPurchase;
    if (!purchase) {
      router.push("/not-found");
      return;
    }

    // pre-fill data
    date.value = purchase.date;
    warehouseId.value = purchase.warehouseId;
    supplierId.value = purchase.supplierId;
    orderTax.value = purchase.orderTax;
    discount.value = purchase.discount;
    shippingCost.value = purchase.shippingCost;
    purchaseStatus.value = purchase.purchaseStatus;
    note.value = purchase.note ?? "";

    // products
    products.value = await Promise.all(
      purchase.products.map(async (p) => {
        const cost = parseFloat(p.productUnitCost ?? "0");
        const discount = parseFloat(p.productDiscount ?? "0");
        const tax = parseFloat(p.productTax ?? "0");
        const qty = p.purchaseQty ?? 0;
        const subTotal = ((cost - discount + tax) * qty).toFixed(2);

        // fetch real stock
        const realProduct = await productStore.fetchProductById(p.productId);
        const stock = realProduct?.quantity ?? 0;

        return {
          productId: p.productId,
          productName: p.productName,
          code: p.productCode,
          cost: p.productUnitCost,
          discount: p.productDiscount,
          tax: p.productTax,
          taxType: "EXCLUSIVE" as const,
          purchaseQty: qty,
          subTotal,
          stock,
        };
      })
    );
  } catch (err) {
    errorMessage.value = "Purchase not found.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(), 3000);
    router.push("/purchase-list");
  }
});

// add product
const addProduct = (product: SelectedPurchaseProduct) => {
  if (!products.value.some(p => p.productId === product.productId)) {
    const qty = product.purchaseQty ?? 1;
    const cost = parseFloat(product.cost ?? "0");
    const discount = parseFloat(product.discount ?? "0");
    const tax = parseFloat(product.tax ?? "0");
    const subTotal = ((cost - discount + tax) * qty).toFixed(2);

    products.value.push({
      ...product,
      purchaseQty: qty,
      subTotal,
    });
  }
};

// submit
const handleSubmit = async () => {
  if (!date.value || !warehouseId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : "Please add at least one product.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(), 3000);
    return;
  }

  const payload: UpdatePurchaseRequest = {
    date: date.value,
    supplierId: supplierId.value!,
    warehouseId: warehouseId.value!,
    products: products.value.map(p => ({
      productId: p.productId,
      productUnitCost: p.cost,
      purchaseQty: p.purchaseQty,
      productDiscount: p.discount,
      productTax: p.tax,
      subTotal: p.subTotal,
    })),
    orderTax: orderTax.value,
    discount: discount.value,
    shippingCost: shippingCost.value,
    totalAmount: products.value
      .reduce((sum, p) => sum + parseFloat(p.subTotal || "0"), 0)
      .toFixed(2),
    shippingStatus: shipmentStatus.value,
    purchaseStatus: purchaseStatus.value,
    note: note.value,
  };

  try {
    await purchaseStore.editPurchase(purchaseId, payload);
    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(() => {
      Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!)?.hide();
      router.push("/purchase-list");
    }, 3000);
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message ?? "Failed to update purchase.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(), 3000);
  }
};
</script>
