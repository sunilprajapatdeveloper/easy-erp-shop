export enum PurchaseStatus {
  PENDING = "PENDING",
  PROCESSING = "PROCESSING",
  SHIPPED = "SHIPPED",
  IN_TRANSIT = "IN_TRANSIT",
  DELIVERED = "DELIVERED",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
  RETURNED = "RETURNED",
}

export const PurchaseStatusLabels: Record<PurchaseStatus, string> = {
  [PurchaseStatus.PENDING]: "Pending",
  [PurchaseStatus.PROCESSING]: "Processing",
  [PurchaseStatus.SHIPPED]: "Shipped",
  [PurchaseStatus.IN_TRANSIT]: "In Transit",
  [PurchaseStatus.DELIVERED]: "Delivered",
  [PurchaseStatus.COMPLETED]: "Completed",
  [PurchaseStatus.CANCELLED]: "Cancelled",
  [PurchaseStatus.RETURNED]: "Returned",
};
