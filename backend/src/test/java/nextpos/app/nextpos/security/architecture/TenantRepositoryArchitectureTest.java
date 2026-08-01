package nextpos.app.nextpos.security.architecture;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/** Prevents tenant-owned aggregates from regressing to unscoped JpaRepository access. */
class TenantRepositoryArchitectureTest {

    private static final Set<String> TENANT_REPOSITORIES = Set.of(
            "adjustmentRepository", "adjustmentTypeRepository", "barcodeScannerRepository",
            "brandRepository", "categoryRepository", "customerRepository", "discountRepository",
            "expensesRepository", "loyaltySettingsRepository", "mediaRepository", "paymentRepository",
            "posRepo", "productJpaRepository", "productPriceRepository", "productRepository",
            "productStockRepository", "productTaxRepository", "productUnitRepository",
            "promotionRepository", "purchaseRepository", "purchaseReturnRepository", "quotationRepository",
            "roleRepository", "saleRepository", "saleReturnRepository", "scannerRepository",
            "securitySettingsRepository", "shippingProviderSettingsRepository", "smtpSettingsRepository",
            "socialMediaSettingsRepository", "supplierRepository", "transferRepository", "userRepository",
            "warehouseCurrencyRepository", "warehouseRepository");

    private static final Pattern GENERIC_OPERATION = Pattern.compile(
            "\\b(" + String.join("|", TENANT_REPOSITORIES) + ")\\s*"
                    + "\\.(findById|findAll|existsById|deleteById|getReferenceById|findAllById)\\s*\\(");

    @Test
    void tenantRepositoriesNeverUseGenericJpaLookupOperations() throws IOException {
        Path sourceRoot = Path.of("src/main/java");
        List<String> violations = new ArrayList<>();

        try (var files = Files.walk(sourceRoot)) {
            files.filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> !path.toString().contains("/repository/")
                            && !path.toString().contains("\\repository\\"))
                    .forEach(path -> inspect(path, violations));
        }

        assertThat(violations)
                .as("generic repository calls can bypass tenant isolation")
                .isEmpty();
    }

    private void inspect(Path path, List<String> violations) {
        try {
            String source = Files.readString(path);
            Matcher matcher = GENERIC_OPERATION.matcher(source);
            while (matcher.find()) {
                long line = source.substring(0, matcher.start()).lines().count();
                violations.add(path + ":" + line + " " + matcher.group());
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to inspect " + path, exception);
        }
    }
}
