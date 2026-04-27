import { TaxInclusionType } from "@/enums/TaxInclusionType";
import { Product } from "@/types/Product";
import { SelectedSaleProduct } from "@/types/Sale";

/**
 * Converts an amount from one currency to another using provided exchange rates.
 * Rates are expressed as 1 unit of 'fromCurrency' = X units of 'toCurrency'.
 */
export function convertCurrency(
  amount: number,
  fromCurrency: string,
  toCurrency: string,
  rateMap: Record<string, number>,
): number {
  if (fromCurrency === toCurrency) return amount;
  const rate = rateMap[`${fromCurrency}_${toCurrency}`];
  if (!rate)
    throw new Error(
      `No exchange rate found from ${fromCurrency} to ${toCurrency}`,
    );
  return amount * rate;
}

/**
 * Prepares a SelectedSaleProduct object from a raw product, applying currency conversion.
 */
export function buildSelectedSaleProduct(params: {
  product: Product;
  saleCurrencyCode: string;
  exchangeRateMap: Record<string, number>;
  warehouseId: number;
  saleQty?: number;
}): SelectedSaleProduct {
  const {
    product,
    saleCurrencyCode,
    exchangeRateMap,
    warehouseId,
    saleQty = 1,
  } = params;

  // Original price and currency from the product's price list
  const originalCurrencyCode = product.price?.currencyCode ?? "USD";
  const originalUnitPrice = product.price?.price ?? 0;

  // Convert to sale currency
  const convertedPrice = convertCurrency(
    originalUnitPrice,
    originalCurrencyCode,
    saleCurrencyCode,
    exchangeRateMap,
  );

  return {
    productId: product.id,
    productName: product.name,
    code: product.code,
    productUnitPrice: convertedPrice.toFixed(2),
    discount: "0",
    stock: product.stock?.availableQuantity ?? product.stock?.quantity ?? 0,
    tax: "0",
    inclusionType: TaxInclusionType.EXCLUSIVE,
    saleQty,
    subTotal: (convertedPrice * saleQty).toFixed(2),
    originalCurrencyCode,
    originalUnitPrice: originalUnitPrice.toString(),
    originalToSaleExchangeRate: (convertedPrice / originalUnitPrice).toFixed(8),
  };
}
