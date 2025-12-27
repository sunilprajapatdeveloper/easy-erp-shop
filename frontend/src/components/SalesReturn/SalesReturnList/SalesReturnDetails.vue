<template>
  <div class="modal fade" id="detailsModal" tabindex="-1" aria-labelledby="detailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-xxl modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <!-- Modal Header -->
        <div class="modal-header">
          <h5 class="modal-title text-title" id="detailsModalLabel">
            Sales Return Details
          </h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Close" />
          </button>
        </div>

        <!-- Modal Body -->
        <div class="modal-body pb-40">
          <div class="row">
            <!-- Invoice Details -->
            <div class="col-lg-4">
              <h6 class="fs-16 fw-bold text-title mb-20">Invoice Details:</h6>
              <ul class="details-title list-style mb-40">
                <li class="fs-14 fw-medium text-title lh-1">
                  DATE :<span class="fw-semibold ms-1">{{ formattedDate }}</span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  TIME :<span class="fw-semibold ms-1">{{ formattedTime }}</span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  REFERENCE :<span class="fw-semibold ms-1">{{ saleReturn?.referenceNumber }}</span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  WAREHOUSE :<span class="fw-semibold ms-1">{{ warehouseName }}</span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  RETURN STATUS :
                  <span class="badge" :class="{
                    'badge-outline-green': saleReturn?.returnStatus === 'COMPLETED',
                    'badge-outline-red': saleReturn?.returnStatus !== 'COMPLETED'
                  }">
                    {{ saleReturn?.returnStatus }}
                  </span>
                </li>
              </ul>
            </div>

            <!-- Customer Info -->
            <div class="col-lg-4 ps-xxl-6">
              <h6 class="fs-16 fw-bold text-title mb-20">Customer info:</h6>
              <ul class="details-title list-style mb-40">
                <li class="fs-14 fw-semibold text-title lh-1">
                  NAME :<span class="ms-1 text-optional">{{ customerName }}</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  MAIL :<span class="text-optional ms-1">{{ customerEmail }}</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  PHONE :<span class="text-optional ms-1">{{ customerPhone }}</span>
                </li>
                <!-- <li class="fs-14 fw-semibold text-title lh-1">
                  ADDRESS :<span class="text-optional ms-1">{{ customerAddress }}</span>
                </li> -->
              </ul>
            </div>

            <!-- Company Info -->
            <div class="col-lg-4 ps-xxl-6">
              <h6 class="fs-16 fw-bold text-title mb-20">Company info:</h6>
              <ul class="details-title list-style mb-40">
                <li class="fs-14 fw-semibold text-title lh-1">
                  NAME :<span class="ms-1 text-optional">{{ companyName }}</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  MAIL :<span class="text-optional ms-1">{{ companyEmail }}</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  PHONE :<span class="text-optional ms-1">{{ companyPhone }}</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  ADDRESS :<span class="text-optional ms-1">{{ companyAddress }}</span>
                </li>
              </ul>
            </div>
          </div>

          <!-- Products Table -->
          <div class="table-responsive style-two">
            <table class="table text-nowrap align-middle mb-0 border-0">
              <thead>
                <tr class="bg_mild">
                  <th>NO.</th>
                  <th>PRODUCT</th>
                  <th>CODE</th>
                  <th>UNIT PRICE</th>
                  <th>QUANTITY</th>
                  <th>DISCOUNT</th>
                  <th>TAX</th>
                  <th>SUBTOTAL</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in saleReturn?.products || []" :key="item.productId" class="br-s-1">
                  <td class="fs-14 fw-semibold">{{ index + 1 }}.</td>
                  <td class="fs-14 fw-normal">{{ item.productName }}</td>
                  <td class="fs-14 fw-normal">{{ item.productCode }}</td>
                  <td class="fs-14 fw-normal">{{ formatCurrency(item.productUnitPrice) }}</td>
                  <td class="fs-14 fw-normal">{{ item.returnQty }}</td>
                  <td class="fs-14 fw-normal">{{ formatCurrency(item.returnDiscount) }}</td>
                  <td class="fs-14 fw-normal">{{ formatCurrency(item.returnTax) }}</td>
                  <td class="fs-14 fw-normal">{{ formatCurrency(calculateSubTotal(item)) }}</td>
                </tr>

                <!-- Totals -->
                <tr>
                  <td colspan="7" class="text-title text-end fw-normal">RETURN TAX</td>
                  <td>{{ formatCurrency(saleReturn?.returnTax) }}</td>
                </tr>
                <tr>
                  <td colspan="7" class="text-title text-end fw-normal">RETURN DISCOUNT</td>
                  <td>{{ formatCurrency(saleReturn?.returnDiscount) }}</td>
                </tr>
                <tr>
                  <td colspan="7" class="text-title text-end fw-normal">SHIPPING</td>
                  <td>{{ formatCurrency(saleReturn?.shippingCost) }}</td>
                </tr>
                <tr>
                  <td colspan="7" class="text-title text-end fw-semibold">GRAND TOTAL</td>
                  <td class="fw-black text-purple">{{ formatCurrency(grandTotal) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Modal Footer -->
        <div class="modal-footer pt-25 pb-35 d-flex flex-wrap justify-content-end me-xxl-3">
          <button class="btn style-six">SMS</button>
          <button class="btn style-seven">EMAIL</button>
          <a href="#" class="btn style-eight">PDF</a>
          <a class="btn style-five upload-btn px-xxl-6" @click="printInvoice">
            Print
            <img src="../../../assets/img/icons/download.svg" alt="Download" />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch, onMounted } from "vue";
import { useUserStore } from "@/stores/userStore";
import { useCustomerStore } from "@/stores/customerStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCompanyStore } from "@/stores/companyStore";
import { SaleReturn, SaleReturnProduct } from "@/types/saleReturn";
import { CompanyDetail } from "@/types/Company";

export default defineComponent({
  name: "SaleReturnDetails",
  props: {
    saleReturn: {
      type: Object as () => SaleReturn,
      required: false,
      default: null,
    },
  },
  setup(props) {
    const customerStore = useCustomerStore();
    const warehouseStore = useWarehouseStore();
    const companyStore = useCompanyStore();
    const userStore = useUserStore();

    // Local company ref
    const company = ref({
      companyName: "-",
      email: "-",
      phone: "-",
      address: "-",
    });

    // Fetch company based on saleReturn.companyId or currentUser.companyId
    const fetchCompany = async () => {
      const companyId = props.saleReturn?.companyId || userStore.currentUser?.companyId;
      if (!companyId) return;

      // Try to find in cache first
      let c: CompanyDetail | undefined = companyStore.companyDetails.get(companyId);

      // If not found, fetch from backend
      if (!c) {
        const fetched = await companyStore.fetchCompanyDetail(companyId);
        c = fetched ?? undefined; // normalize null -> undefined
      }

      // Map only the required fields for modal
      if (c) {
        company.value = {
          companyName: c.companyName || "-",
          email: c.email || "-",
          phone: c.phone || "-",
          address: c.address || "-",
        };
      }
    };

    // Watch saleReturn changes
    watch(
      () => props.saleReturn?.companyId,
      () => fetchCompany(),
      { immediate: true }
    );

    // Computed fields for template
    const customer = computed(() => props.saleReturn?.customerId
      ? customerStore.customers.find(c => c.id === props.saleReturn!.customerId)
      : null
    );
    const warehouse = computed(() => props.saleReturn?.warehouseId
      ? warehouseStore.warehouses.find(w => w.id === props.saleReturn!.warehouseId)
      : null
    );

    // Template aliases
    const warehouseName = computed(() => warehouse.value?.name || "-");
    const customerName = computed(() => customer.value?.name || "-");
    const customerEmail = computed(() => customer.value?.email || "-");
    const customerPhone = computed(() => customer.value?.phone || "-");

    const companyName = computed(() => company.value.companyName || "-");
    const companyEmail = computed(() => company.value.email || "-");
    const companyPhone = computed(() => company.value.phone || "-");
    const companyAddress = computed(() => company.value.address || "-");

    // Date & Time
    const formattedDate = computed(() => props.saleReturn?.date ? new Date(props.saleReturn.date).toLocaleDateString() : "-");
    const formattedTime = computed(() => props.saleReturn?.date ? new Date(props.saleReturn.date).toLocaleTimeString() : "-");

    // Currency formatting
    const formatCurrency = (amount: string | number | undefined) => {
      if (!amount) return "$ 0.00";
      const value = typeof amount === "string" ? parseFloat(amount) : amount;
      return `$ ${value.toFixed(2)}`;
    };

    // Product subtotal
    const calculateSubTotal = (item: SaleReturnProduct) => {
      const unitPrice = parseFloat(item.productUnitPrice || "0");
      const qty = item.returnQty || 0;
      const discount = parseFloat(item.returnDiscount || "0");
      const tax = parseFloat(item.returnTax || "0");
      return unitPrice * qty - discount + tax;
    };

    // Grand total
    const grandTotal = computed(() => {
      if (!props.saleReturn) return 0;
      const productTotal = props.saleReturn.products.reduce((sum, item) => sum + calculateSubTotal(item), 0);
      const shipping = parseFloat(props.saleReturn.shippingCost || "0");
      return productTotal + shipping;
    });

    // Print
    const printInvoice = () => window.print();

    return {
      warehouseName,
      customerName,
      customerEmail,
      customerPhone,
      companyName,
      companyEmail,
      companyPhone,
      companyAddress,
      formattedDate,
      formattedTime,
      formatCurrency,
      calculateSubTotal,
      grandTotal,
      printInvoice,
    };
  },
});
</script>
