import { TaxInclusionType } from "@/enums/TaxInclusionType";
import type { SelectedSaleProduct } from "@/types/Sale";

/**
 * Local preview calculation – mirrors backend logic for display only.
 * These values are NEVER sent to the backend.
 */
export function calculateSaleLine(product: SelectedSaleProduct) {
  const unitPrice = product.productUnitPrice;
  const qty = product.quantity;
  const lineTotalBeforeDiscount = unitPrice * qty;

  // Frontend does NOT apply line discounts; backend handles them.
  const lineDiscountAmount = 0;

  const taxableBase = lineTotalBeforeDiscount; // simplified for preview

  const taxRate = product.taxRate;
  let lineNetAmount: number;
  let lineTaxAmount: number;
  let lineGrossAmount: number;

  if (taxRate > 0) {
    if (product.taxInclusionType === TaxInclusionType.INCLUSIVE) {
      lineGrossAmount = taxableBase;
      lineTaxAmount = lineGrossAmount - lineGrossAmount / (1 + taxRate / 100);
      lineNetAmount = lineGrossAmount - lineTaxAmount;
    } else {
      lineNetAmount = taxableBase;
      lineTaxAmount = lineNetAmount * (taxRate / 100);
      lineGrossAmount = lineNetAmount + lineTaxAmount;
    }
  } else {
    lineNetAmount = taxableBase;
    lineTaxAmount = 0;
    lineGrossAmount = taxableBase;
  }

  return {
    lineDiscountAmount: round(lineDiscountAmount),
    lineNetAmount: round(lineNetAmount),
    lineTaxAmount: round(lineTaxAmount),
    lineGrossAmount: round(lineGrossAmount),
  };
}

export function calculateLineTotal(product: SelectedSaleProduct): number {
  return calculateSaleLine(product).lineGrossAmount;
}

function round(n: number): number {
  return Math.round(n * 100) / 100;
}
