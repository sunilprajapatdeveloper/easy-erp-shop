package nextpos.app.nextpos.importexport.strategy;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ImportExportStrategyRegistry {

    private final Map<String, ImportExportStrategy> strategies = new HashMap<>();
    private final List<ImportExportStrategy> strategyList;

    public ImportExportStrategyRegistry(List<ImportExportStrategy> strategyList) {
        this.strategyList = strategyList;
    }

    @PostConstruct
    public void init() {
        for (ImportExportStrategy strategy : strategyList) {
            strategies.put(strategy.getModuleName(), strategy);
        }
    }

    public ImportExportStrategy getStrategy(String module) {
        return strategies.get(module);
    }
}