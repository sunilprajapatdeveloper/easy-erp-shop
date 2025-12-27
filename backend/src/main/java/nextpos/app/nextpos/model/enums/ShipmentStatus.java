package nextpos.app.nextpos.model.enums;

public enum ShipmentStatus {
    PENDING,       // Shipping is created but not yet processed
    PROCESSING,    // Shipping is being processed/prepared
    SHIPPED,       // Shipment is dispatched
    IN_TRANSIT,    // Shipment is on the way
    DELIVERED,     // Shipment delivered to destination
    CANCELLED,     // Shipment was cancelled
    RETURNED       // Shipment was returned
}