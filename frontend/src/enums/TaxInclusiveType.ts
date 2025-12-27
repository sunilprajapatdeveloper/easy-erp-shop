export enum TaxInclusiveType {
  INCLUSIVE = "INCLUSIVE",
  EXCLUSIVE = "EXCLUSIVE",
}

export const TaxInclusiveTypeLabels: Record<TaxInclusiveType, string> = {
  [TaxInclusiveType.INCLUSIVE]: "Inclusive",
  [TaxInclusiveType.EXCLUSIVE]: "Exclusive",
};
