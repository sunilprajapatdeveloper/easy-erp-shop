<template>
  <MainHeader />
  <MainSidebar />
  <div class="main-content bg_gray d-flex flex-column transition overflow-hidden">
    <BreadcrumbMenu pageTitle="Edit Sales" />

    <ChooseForm :warehouses="warehouses" :customers="customers" v-model:warehouseId="warehouseId"
      v-model:customerId="customerId" v-model:date="date" @add-product="addProduct" />

    <SelectedProducts v-if="products.length" v-model:products="products" />

    <SubmitSale v-model:manualDiscountValue="manualDiscountValue" v-model:manualDiscountType="manualDiscountType"
      v-model:manualDiscountReason="manualDiscountReason" v-model:appliedDiscountId="appliedDiscountId"
      v-model:couponCode="couponCode" v-model:shippingCost="shippingCost" v-model:paidAmount="paidAmount"
      v-model:paymentStatus="paymentStatus" v-model:saleStatus="saleStatus" v-model:note="note" :products="products"
      :availableDiscounts="availableDiscounts" @submit="handleSubmit" />

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
import type { SelectedSaleProduct, UpdateSaleRequest } from "@/types/Sale";
import type { Customer } from "@/types/Customer";
import type { WarehouseListItem } from "@/types/Warehouse";
import { DiscountType } from "@/enums/discountType";
import { SaleStatus } from "@/enums/saleStatus";
import { PaymentStatus } from "@/enums/paymentStatus";
import { SaleSource } from "@/enums/SaleSource";

import { useSaleStore } from "@/stores/saleStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCustomerStore } from "@/stores/customerStore";
import { useProductStore } from "@/stores/productStore";
import { useDiscountStore } from "@/stores/discountStore";

import MainHeader from "@/components/Layouts/MainHeader.vue";
import MainSidebar from "@/components/Layouts/MainSidebar.vue";
import BreadcrumbMenu from "@/components/Common/BreadcrumbMenu.vue";
import ChooseForm from "@/components/Sales/CreateSales/ChooseForm.vue";
import SelectedProducts from "@/components/Sales/CreateSales/SelectedProducts.vue";
import SubmitSale from "@/components/Sales/CreateSales/SubmitSale.vue";
import MainFooter from "@/components/Layouts/MainFooter.vue";
import { DiscountItem } from "@/types/Discount";

const route = useRoute();
const router = useRouter();
const saleId = Number(route.params.id);

const saleStore = useSaleStore();
const warehouseStore = useWarehouseStore();
const customerStore = useCustomerStore();
const productStore = useProductStore();
const discountStore = useDiscountStore();

const date = ref<string>("");
const warehouseId = ref<number | null>(null);
const customerId = ref<number | null>(null);

const manualDiscountValue = ref("0");
const manualDiscountType = ref<DiscountType | null>(null);
const manualDiscountReason = ref("");
const appliedDiscountId = ref<number | null>(null);
const couponCode = ref("");

const shippingCost = ref("0");
const paidAmount = ref("0");
const paymentStatus = ref<PaymentStatus>(PaymentStatus.PENDING);
const saleStatus = ref<SaleStatus>(SaleStatus.PENDING);
const note = ref("");
const errorMessage = ref("Something went wrong. Please try again.");

const warehouses = ref<WarehouseListItem[]>([]);
const customers = ref<Customer[]>([]);
const products = ref<SelectedSaleProduct[]>([]);
const availableDiscounts = ref<DiscountItem[]>([]);

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
    customerId.value = sale.customerId ?? null;

    // Populate discount fields from loaded sale
    if (sale.discountSource === "MANUAL") {
      manualDiscountValue.value = String(sale.orderDiscountValue ?? sale.orderDiscount ?? 0);
      manualDiscountType.value = sale.orderDiscountType ?? null;
      manualDiscountReason.value = sale.discountDescription ?? "";
    } else if (sale.discountSource === "SYSTEM") {
      appliedDiscountId.value = sale.appliedDiscountId ?? null;
    }
    couponCode.value = sale.promotionCouponCode ?? "";
    shippingCost.value = String(sale.shippingCost ?? 0);
    paidAmount.value = String(sale.paidAmountTxnCurrency ?? 0);
    paymentStatus.value = sale.paymentStatus ?? PaymentStatus.PENDING;
    saleStatus.value = sale.saleStatus;
    note.value = sale.note ?? "";

    // Load available discounts for the warehouse
    availableDiscounts.value = await discountStore.fetchActiveOrderDiscounts(sale.warehouseId);

    // Map products
    products.value = await Promise.all(
      sale.products.map(async (p) => {
        const realProduct = await productStore.fetchProductById(p.productId);
        const stock = realProduct?.stock?.availableQuantity ?? realProduct?.stock?.quantity ?? 0;

        return {
          productId: p.productId,
          productName: p.productName ?? realProduct?.name ?? "",
          code: p.productCode ?? realProduct?.code ?? "",
          productUnitPrice: p.productUnitPrice,
          quantity: p.quantity,
          stock,
          taxName: p.taxName,
          taxCategory: p.taxCategory,
          taxRate: p.taxRate,
          taxInclusionType: p.taxInclusionType,
          taxApplicationOrder: p.taxApplicationOrder,
          lineDiscountAmount: p.lineDiscountAmount,
          lineNetAmount: p.lineNetAmount,
          lineTaxAmount: p.lineTaxAmount,
          lineGrossAmount: p.lineGrossAmount,
        } satisfies SelectedSaleProduct;
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
  if (!products.value.some((p) => p.productId === product.productId)) {
    products.value.push({ ...product, quantity: product.quantity ?? 1 });
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

  const payload: UpdateSaleRequest = {
    date: date.value,
    warehouseId: warehouseId.value,
    customerId: customerId.value,
    products: products.value.map((p) => ({
      productId: p.productId,
      quantity: p.quantity,
      unitPriceOverride: undefined,
    })),
    manualDiscountValue: parseFloat(manualDiscountValue.value || "0") || undefined,
    manualDiscountType: manualDiscountType.value ?? undefined,
    manualDiscountReason: manualDiscountReason.value || undefined,
    appliedDiscountId: appliedDiscountId.value ?? undefined,
    couponCode: couponCode.value || undefined,
    shippingCost: parseFloat(shippingCost.value || "0"),
    roundingAmount: 0,
    saleStatus: saleStatus.value,
    paymentStatus: paymentStatus.value,
    source: SaleSource.WEB,
    note: note.value || undefined,
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