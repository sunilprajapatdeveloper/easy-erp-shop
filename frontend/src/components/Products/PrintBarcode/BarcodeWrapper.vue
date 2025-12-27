<template>
  <div class="barcode-wrapper mb-30" id="barcode-print-area">
    <div class="d-flex justify-content-between align-items-center mb-30 no-print">
      <h6 class="fw-bold fs-16">Barcode Preview</h6>
      <div class="d-flex gap-2">
        <select v-model="selectedLayout" class="form-select form-select-sm w-auto">
          <option v-for="option in layoutOptions" :key="option.value" :value="option.value">
            {{ option.label }}
          </option>
        </select>
        <button class="btn style-five upload-btn px-xxl-6" @click="printBarcode">
          Print
            <img src="../../../assets/img/icons/download.svg" alt="Image" />
        </button>
      </div>
    </div>

    <div class="barcode-sheet" :class="selectedLayout">
      <div v-for="(product, index) in expandedProducts" :key="index" class="barcode-item">
        <div class="barcode-box">
          <div class="barcode-name">{{ product.name }}</div>
          <div class="barcode-code">{{ product.barcode || product.code }}</div>
          <svg class="barcode-svg"></svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, nextTick, defineProps } from "vue";
import JsBarcode from "jsbarcode";
import type { Product } from "@/types/Product";

const props = defineProps<{
  products: (Product & { quantity?: number })[];
}>();

// --- Layout options ---
const layoutOptions = [
  { label: "30 per sheet (A4)", value: "layout-30" },
  { label: "35 per sheet (A4)", value: "layout-35" },
  { label: "40 per sheet (A4)", value: "layout-40" },
];

const selectedLayout = ref("layout-30");

// --- Expanded list based on quantity ---
const expandedProducts = computed(() => {
  const list: (Product & { quantity?: number })[] = [];
  props.products.forEach((p) => {
    const qty = p.quantity ?? 1;
    for (let i = 0; i < qty; i++) list.push(p);
  });
  return list;
});

const renderBarcodes = () => {
  nextTick(() => {
    const elements = document.querySelectorAll(".barcode-svg");
    elements.forEach((el, i) => {
      const product = expandedProducts.value[i];
      if (product) {
        // Prefer backend barcode if available, otherwise fallback to code
        const barcodeValue = product.barcode || product.code;

        JsBarcode(el as HTMLElement, barcodeValue, {
          format: "CODE128",
          width:
            selectedLayout.value === "layout-40"
              ? 1
              : selectedLayout.value === "layout-35"
              ? 1.5
              : 2,
          height: selectedLayout.value === "layout-40" ? 25 : 40,
          displayValue: false,
          background: "transparent",
          margin: 0,
        });
      }
    });
  });
};

onMounted(renderBarcodes);
watch([expandedProducts, selectedLayout], renderBarcodes);

// --- Print only the barcode area ---
const printBarcode = () => {
  const area = document.getElementById("barcode-print-area");
  if (!area) return;

  // Clone the area and remove the header (no-print)
  const clone = area.cloneNode(true) as HTMLElement;
  clone.querySelectorAll(".no-print").forEach(el => el.remove());

  const printWindow = window.open("", "_blank", "width=900,height=650");
  if (printWindow) {
    printWindow.document.write(`
      <html>
        <head>
          <title>Barcode Print</title>
          <style>
            body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }
            .barcode-sheet { display: grid; gap: 8px; justify-content: center; }
            .layout-30 { grid-template-columns: repeat(3, 1fr); max-width: 210mm; margin: auto; }
            .layout-35 { grid-template-columns: repeat(5, 1fr); max-width: 210mm; margin: auto; }
            .layout-40 { grid-template-columns: repeat(8, 1fr); max-width: 210mm; margin: auto; }
            .barcode-item { display: flex; justify-content: center; align-items: center; border: none; padding: 4px; }
            .barcode-name { font-size: 10px; font-weight: 600; text-align: center; }
            .barcode-code { font-size: 9px; text-align: center; margin-bottom: 2px; }
          </style>
        </head>
        <body>
          ${clone.innerHTML}
        </body>
      </html>
    `);
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
    printWindow.close();
  }
};
</script>

<style scoped lang="scss">
.barcode-wrapper {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  max-width: 100%;
  overflow-x: hidden;
}

.barcode-sheet {
  display: grid;
  justify-content: center;
  margin: 0 auto;
  gap: 8px;
}

/* Layout presets */
.layout-30 {
  grid-template-columns: repeat(3, 1fr);
  max-width: 210mm;
}

.layout-35 {
  grid-template-columns: repeat(5, 1fr);
  max-width: 210mm;
}

.layout-40 {
  grid-template-columns: repeat(8, 1fr);
  max-width: 210mm;

  .barcode-item {
    padding: 4px;
  }
}

.barcode-item {
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #ccc;
  padding: 6px;
  border-radius: 6px;
  background: #fafafa;
}

.barcode-box {
  text-align: center;
}

.barcode-name {
  font-size: 10px;
  font-weight: 600;
  color: #333;
  margin-bottom: 2px;
}

.barcode-code {
  font-size: 9px;
  color: #666;
  margin-bottom: 4px;
}

/* Hide these while printing */
.no-print {
  display: block;
}

@media print {
  .no-print {
    display: none !important;
  }
}
</style>
