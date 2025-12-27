export enum ProductStatus {
  ACTIVE = "ACTIVE",
  INACTIVE = "INACTIVE",
  DISCONTINUED = "DISCONTINUED",
  OBSOLETE = "OBSOLETE",
}

export const ProductStatusLabels: Record<ProductStatus, string> = {
  [ProductStatus.ACTIVE]: "Active",
  [ProductStatus.INACTIVE]: "Inactive",
  [ProductStatus.DISCONTINUED]: "Discontinued",
  [ProductStatus.OBSOLETE]: "Obsolete",
};
