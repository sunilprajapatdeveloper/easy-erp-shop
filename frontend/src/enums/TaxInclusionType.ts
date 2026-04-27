export enum TaxInclusionType {
  INCLUSIVE = "INCLUSIVE",
  EXCLUSIVE = "EXCLUSIVE",
}

export const TaxInclusionTypeLabels: Record<TaxInclusionType, string> = {
  [TaxInclusionType.INCLUSIVE]: "Inclusive",
  [TaxInclusionType.EXCLUSIVE]: "Exclusive",
};
