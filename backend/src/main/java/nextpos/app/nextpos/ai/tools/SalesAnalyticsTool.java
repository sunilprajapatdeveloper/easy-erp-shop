package nextpos.app.nextpos.ai.tools;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.service.interf.SaleService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SalesAnalyticsTool implements Tool {
    private final SaleService saleService;

    @Override
    public String getName() {
        return "sales_analytics";
    }

    @Override
    public String getDescription() {
        return "Get sales summary for a given period";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "period",
                        Map.of("type", "string", "enum", List.of("today", "week", "month"), "description",
                                "Time period"),
                        "warehouseId", Map.of("type", "integer", "description", "Optional warehouse ID")),
                "required", List.of("period"));
    }

    @Override
    public Object execute(Map<String, Object> arguments) {
        String period = (String) arguments.get("period");
        Long warehouseId = arguments.containsKey("warehouseId") ? Long.valueOf(arguments.get("warehouseId").toString())
                : null;
        return saleService.getSalesSummary(period, warehouseId);
    }
}