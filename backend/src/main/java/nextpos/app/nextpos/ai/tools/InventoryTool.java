package nextpos.app.nextpos.ai.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.response.ProductStockResponse;
import nextpos.app.nextpos.service.interf.ProductStockService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryTool implements Tool {
    private final ProductStockService stockService;

    @Override
    public String getName() {
        return "inventory_tool";
    }

    @Override
    public String getDescription() {
        return "Get current stock levels for a product. Provide productId and optionally warehouseId.";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "productId", Map.of("type", "integer", "description", "Product ID"),
                        "warehouseId", Map.of("type", "integer", "description", "Optional warehouse ID")),
                "required", List.of("productId"));
    }

    @Override
    public Object execute(Map<String, Object> arguments) {
        Long productId = Long.valueOf(arguments.get("productId").toString());
        Long warehouseId = arguments.containsKey("warehouseId") ? Long.valueOf(arguments.get("warehouseId").toString())
                : null;

        try {
            if (warehouseId != null) {
                ProductStockResponse stock = stockService.getByProductAndWarehouse(productId, warehouseId);
                if (stock == null) {
                    return Map.of("error",
                            "No stock record found for product " + productId + " in warehouse " + warehouseId);
                }
                return Map.of(
                        "productId", stock.getProductId(),
                        "warehouseId", stock.getWarehouseId(),
                        "quantity", stock.getQuantity());
            } else {
                List<ProductStockResponse> stocks = stockService.listStocksByProduct(productId);
                return stocks.stream()
                        .map(s -> Map.of(
                                "warehouseId", s.getWarehouseId(),
                                "quantity", s.getQuantity()))
                        .toList();
            }
        } catch (Exception e) {
            log.error("Error fetching inventory", e);
            return Map.of("error", "Failed to retrieve stock: " + e.getMessage());
        }
    }
}