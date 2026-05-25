export enum DiscountSource {
  MANUAL = "MANUAL",
  AUTOMATIC = "AUTOMATIC",
  STAFF = "STAFF",
  WHOLESALE = "WHOLESALE",
  LOYALTY = "LOYALTY",
  SYSTEM = "SYSTEM",
}

export const DiscountSourceLabels: Record<DiscountSource, string> = {
  [DiscountSource.MANUAL]: "Manual",
  [DiscountSource.AUTOMATIC]: "Automatic",
  [DiscountSource.STAFF]: "Staff",
  [DiscountSource.WHOLESALE]: "Wholesale",
  [DiscountSource.LOYALTY]: "Loyalty",
  [DiscountSource.SYSTEM]: "System",
};
