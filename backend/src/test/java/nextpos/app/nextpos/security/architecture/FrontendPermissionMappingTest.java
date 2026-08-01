package nextpos.app.nextpos.security.architecture;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nextpos.app.nextpos.model.enums.PermissionType;
import org.junit.jupiter.api.Test;

class FrontendPermissionMappingTest {

    private static final Pattern PERMISSION = Pattern.compile("permission:\\s*\"([A-Z_]+)\"");

    @Test
    void routerPermissionsExistInBackendPermissionModel() throws IOException {
        Path router = Path.of("../frontend/src/router/index.ts");
        assertThat(router).exists();

        Matcher matcher = PERMISSION.matcher(Files.readString(router));
        Set<String> configured = new HashSet<>();
        while (matcher.find()) configured.add(matcher.group(1));

        Set<String> backend = new HashSet<>();
        for (PermissionType permission : PermissionType.values()) backend.add(permission.name());

        assertThat(configured).isSubsetOf(backend);
    }
}
