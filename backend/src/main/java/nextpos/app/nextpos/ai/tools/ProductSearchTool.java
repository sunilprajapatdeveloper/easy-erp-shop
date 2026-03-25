package nextpos.app.nextpos.ai.tools;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.service.interf.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductSearchTool implements Tool {
    private final ProductService productService;

    @Override
    public String getName() {
        return "product_search";
    }

    @Override
    public String getDescription() {
        return "Search for products by name, SKU, or category";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "query", Map.of("type", "string", "description", "Search term"),
                        "limit", Map.of("type", "integer", "description", "Max results", "default", 10)),
                "required", List.of("query"));
    }

    @Override
    public Object execute(Map<String, Object> arguments) {
        String query = (String) arguments.get("query");
        int limit = (int) arguments.getOrDefault("limit", 10);
        return productService.searchProducts(query, limit);
    }
}