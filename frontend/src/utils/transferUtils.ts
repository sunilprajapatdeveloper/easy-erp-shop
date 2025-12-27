import type { SelectedTransferProduct } from "@/types/Transfer";

export function calculateSubTotal(product: SelectedTransferProduct): string {
  const cost = Number(product.cost) || 0;
  const qty = Number(product.transferredQty ?? 1);
  const discount = Number(product.discount) || 0;
  const tax = Number(product.tax) || 0;

  const base = cost * qty;
  const discountAmt = (base * discount) / 100;
  const discounted = base - discountAmt;

  if (product.taxType === "INCLUSIVE") {
    return discounted.toFixed(2);
  } else {
    const taxAmt = (discounted * tax) / 100;
    return (discounted + taxAmt).toFixed(2);
  }
}