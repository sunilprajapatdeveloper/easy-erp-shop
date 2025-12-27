export enum SaleStatus {
  PENDING = "PENDING",
  PROCESSING = "PROCESSING",
  SHIPPED = "SHIPPED",
  IN_TRANSIT = "IN_TRANSIT",
  DELIVERED = "DELIVERED",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
  RETURNED = "RETURNED",
}

export const SaleStatusLabels: Record<SaleStatus, string> = {
  [SaleStatus.PENDING]: "Pending",
  [SaleStatus.PROCESSING]: "Processing",
  [SaleStatus.SHIPPED]: "Shipped",
  [SaleStatus.IN_TRANSIT]: "In Transit",
  [SaleStatus.DELIVERED]: "Delivered",
  [SaleStatus.COMPLETED]: "Completed",
  [SaleStatus.CANCELLED]: "Cancelled",
  [SaleStatus.RETURNED]: "Returned",
};
