package nextpos.app.nextpos.ai.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.response.WarehouseResponse;
import nextpos.app.nextpos.service.interf.WarehouseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseTool implements Tool {
    private final WarehouseService warehouseService;

    @Override
    public String getName() {
        return "warehouse_tool";
    }

    @Override
    public String getDescription() {
        return "Get warehouse details. Provide warehouseId or leave blank to list all warehouses.";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "warehouseId", Map.of("type", "integer", "description", "Optional warehouse ID")),
                "required", List.of());
    }

    @Override
    public Object execute(Map<String, Object> arguments) {
        try {
            if (arguments.containsKey("warehouseId")) {
                Long warehouseId = Long.valueOf(arguments.get("warehouseId").toString());
                WarehouseResponse warehouse = warehouseService.getWarehouseById(warehouseId);
                return Map.of(
                        "id", warehouse.getId(),
                        "name", warehouse.getName(),
                        "address", warehouse.getAddressLine1(),
                        "isDefault", warehouse.isDefault());
            } else {
                List<WarehouseResponse> warehouses = warehouseService.getAllWarehouses();
                return warehouses.stream()
                        .map(w -> Map.of(
                                "id", w.getId(),
                                "name", w.getName()))
                        .toList();
            }
        } catch (Exception e) {
            log.error("Error fetching warehouse", e);
            return Map.of("error", "Failed to retrieve warehouse: " + e.getMessage());
        }
    }
}