import type { SelectedPosProduct } from "@/types/Pos";

export function calculateSubTotal(product: SelectedPosProduct): number {
  const price = Number(product.price) || 0;
  const qty = Number(product.saleQty ?? 1);
  const discount = Number(product.discount) || 0;
  const tax = Number(product.tax) || 0;

  const base = price * qty;
  const discountAmt = (base * discount) / 100;
  const discounted = base - discountAmt;

  return Number(discounted.toFixed(2));
  // if (product.taxType === "INCLUSIVE") {
  // } else {
  //   const taxAmt = (discounted * tax) / 100;
  //   return Number((discounted + taxAmt).toFixed(2));
  // }
}
