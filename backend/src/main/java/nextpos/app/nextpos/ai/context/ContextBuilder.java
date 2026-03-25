package nextpos.app.nextpos.ai.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContextBuilder {
    private final DataFetcher dataFetcher;

    public Map<String, Object> buildContext(TenantContext tenantContext, Map<String, Object> requestContext) {
        Map<String, Object> context = new HashMap<>();
        context.put("tenantId", tenantContext.getTenantId());
        context.put("userId", tenantContext.getUserId());

        if (requestContext != null) {
            if (requestContext.containsKey("productId")) {
                Long productId = Long.valueOf(requestContext.get("productId").toString());
                context.put("product", dataFetcher.getProductById(productId));
            }
            if (requestContext.containsKey("warehouseId")) {
                Long warehouseId = Long.valueOf(requestContext.get("warehouseId").toString());
                context.put("warehouse", dataFetcher.getWarehouse(warehouseId));
            }
            if (requestContext.containsKey("includeRecentSales")) {
                context.put("recentSales", dataFetcher.getRecentSales(tenantContext.getTenantId(), 10));
            }
        }
        return context;
    }
}