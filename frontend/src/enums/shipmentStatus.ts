export enum ShipmentStatus {
  PENDING = "PENDING",
  PROCESSING = "PROCESSING",
  SHIPPED = "SHIPPED",
  IN_TRANSIT = "IN_TRANSIT",
  DELIVERED = "DELIVERED",
  RETURNED = "RETURNED",
}

export const ShipmentStatusLabels: Record<ShipmentStatus, string> = {
  [ShipmentStatus.PENDING]: "Pending",
  [ShipmentStatus.PROCESSING]: "Processing",
  [ShipmentStatus.SHIPPED]: "Shipped",
  [ShipmentStatus.IN_TRANSIT]: "In Transit",
  [ShipmentStatus.DELIVERED]: "Delivered",
  [ShipmentStatus.RETURNED]: "Returned",
};
