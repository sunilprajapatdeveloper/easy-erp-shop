<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Edit Sales" />

    <ChooseForm :warehouses="warehouses" :customers="customers" v-model:warehouseId="warehouseId"
      v-model:customerId="customerId" v-model:date="date" @add-product="addProduct" />

    <SelectedProducts v-if="products.length" v-model:products="products" />

    <SubmitPurchase v-model:orderTax="orderTax" v-model:discount="discount" v-model:shippingCost="shippingCost"
      v-model:note="note" v-model:status="saleStatus" :products="products" @submit="handleSubmit" />

    <div class="flex-grow-1"></div>
    <MainFooter />
  </div>

  <div class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
    <div class="offcanvas-body p-0">
      <div class="create-success">
        <img src="../../assets/img/icons/tick-circle.svg" alt="Success" />
        <span class="text-white fw-medium">Sale has been successfully updated</span>
      </div>
    </div>
  </div>
  <a id="triggerSuccessPopup" class="d-none" data-bs-toggle="offcanvas" href="#successPopup" role="button"></a>

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
import type { SelectedSaleProduct, CreateSaleRequest } from "@/types/Sale";
import type { Customer } from "@/types/Customer";
import type { Warehouse } from "@/types/Warehouse";

import { useSaleStore } from "@/stores/saleStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCustomerStore } from "@/stores/customerStore";
import { useProductStore } from "@/stores/productStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/Sales/CreateSales/ChooseForm.vue";
import SelectedProducts from "@/components/Sales/CreateSales/SelectedProducts.vue";
import SubmitPurchase from "@/components/Sales/CreateSales/SubmitPurchase.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";
import { SaleStatus } from "@/enums/saleStatus";

const route = useRoute();
const router = useRouter();
const saleId = Number(route.params.id);

const saleStore = useSaleStore();
const warehouseStore = useWarehouseStore();
const customerStore = useCustomerStore();
const productStore = useProductStore();

const date = ref<string>("");
const warehouseId = ref<number | null>(null);
const customerId = ref<number | null>(null);
const orderTax = ref("0");
const discount = ref("0");
const shippingCost = ref("0");
const saleStatus = ref<SaleStatus>(SaleStatus.PENDING);
const note = ref("");
const errorMessage = ref("Something went wrong. Please try again.");

const warehouses = ref<Warehouse[]>([]);
const customers = ref<Customer[]>([]);
const products = ref<SelectedSaleProduct[]>([]);

onMounted(async () => {
  warehouses.value = await warehouseStore.fetchWarehouses();
  customers.value = await customerStore.fetchCustomers();

  try {
    await saleStore.fetchSaleById(saleId);
    const sale = saleStore.selectedSale;
    if (!sale) {
      router.push("/not-found");
      return;
    }

    date.value = sale.date;
    warehouseId.value = sale.warehouseId;
    customerId.value = sale.customerId;
    orderTax.value = sale.orderTax;
    discount.value = sale.discount;
    shippingCost.value = sale.shippingCost;
    saleStatus.value = sale.saleStatus;
    note.value = sale.note ?? "";

    products.value = await Promise.all(
      sale.products.map(async (p) => {
        const unitPrice = parseFloat(p.productUnitPrice ?? "0");
        const discount = parseFloat(p.productDiscount ?? "0");
        const tax = parseFloat(p.productTax ?? "0");
        const qty = p.saleQty ?? 0;

        const subTotal = ((unitPrice - discount + tax) * qty).toFixed(2);

        // Fetch real stock
        const realProduct = await productStore.fetchProductById(p.productId);
        const stock = realProduct?.quantity ?? 0;

        return {
          productId: p.productId,
          productName: p.productName,
          code: p.productCode,
          price: p.productUnitPrice,
          discount: p.productDiscount,
          tax: p.productTax,
          taxType: "EXCLUSIVE" as const,
          saleQty: qty,
          subTotal,
          stock,
        };
      })
    );
  } catch (err) {
    errorMessage.value = "Sale not found.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(), 3000);
    router.push("/sale-list");
  }
});

const addProduct = (product: SelectedSaleProduct) => {
  if (!products.value.some(p => p.productId === product.productId)) {
    const qty = product.saleQty ?? 1;
    const price = parseFloat(product.price ?? "0");
    const discount = parseFloat(product.discount ?? "0");
    const tax = parseFloat(product.tax ?? "0");

    const subTotal = ((price - discount + tax) * qty).toFixed(2);

    products.value.push({
      ...product,
      saleQty: qty,
      subTotal,
    });
  }
};

const handleSubmit = async () => {
  if (!date.value || !warehouseId.value || products.value.length === 0) {
    errorMessage.value = !warehouseId.value
      ? "Please select a warehouse."
      : "Please add at least one product.";

    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(), 3000);
    return;
  }

  const payload: CreateSaleRequest = {
    date: date.value,
    warehouseId: warehouseId.value,
    customerId: customerId.value,
    products: products.value.map(p => ({
      productId: p.productId,
      productName: p.productName,
      productCode: p.code,
      productUnitPrice: p.price,
      saleQty: p.saleQty,
      productDiscount: p.discount,
      productTax: p.tax,
      subTotal: p.subTotal,
    })),
    orderTax: orderTax.value,
    discount: discount.value,
    shippingCost: shippingCost.value,
    saleStatus: saleStatus.value,
    note: note.value,
  };

  try {
    await saleStore.editSale(saleId, payload);
    document.getElementById("triggerSuccessPopup")?.click();
    setTimeout(() => {
      Offcanvas.getOrCreateInstance(document.getElementById("successPopup")!)?.hide();
      router.push("/sale-list");
    }, 3000);
  } catch (error: any) {
    errorMessage.value = error?.response?.data?.message ?? "Failed to update sale.";
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!)?.hide(), 3000);
  }
};
</script>