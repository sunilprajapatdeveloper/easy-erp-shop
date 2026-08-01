package nextpos.app.nextpos.security.authorization;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class BusinessPermissionRulesTest {

    private static final Set<String> PUBLIC_CONTROLLERS = Set.of(
            "/api/v1/verifications", "/api/v1/webhooks/razorpay");

    @Test
    void everyBusinessControllerMethodHasBackendPermissionMapping() throws Exception {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

        for (var bean : scanner.findCandidateComponents("nextpos.app.nextpos")) {
            Class<?> controller = Class.forName(bean.getBeanClassName());
            RequestMapping mapping = controller.getAnnotation(RequestMapping.class);
            if (mapping == null || mapping.value().length == 0) continue;
            String base = mapping.value()[0];
            if (!base.startsWith("/api/") || PUBLIC_CONTROLLERS.contains(base)) continue;

            for (Method method : controller.getDeclaredMethods()) {
                String httpMethod = mappedHttpMethod(method);
                if (httpMethod == null) continue;
                assertThat(BusinessPermissionRules.requiredAuthorities(base, httpMethod))
                        .as("permission mapping for %s %s (%s#%s)", httpMethod, base,
                                controller.getSimpleName(), method.getName())
                        .isNotEmpty();
            }
        }
    }

    @Test
    void unknownBusinessPathsAndMethodsFailClosed() {
        assertThat(BusinessPermissionRules.requiredAuthorities("/api/v1/unmapped", "GET")).isEmpty();
        assertThat(BusinessPermissionRules.requiredAuthorities("/api/v1/products", "TRACE"))
                .containsExactly("__DENY__");
    }

    private String mappedHttpMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) return HttpMethod.GET.name();
        if (method.isAnnotationPresent(PostMapping.class)) return HttpMethod.POST.name();
        if (method.isAnnotationPresent(PutMapping.class)) return HttpMethod.PUT.name();
        if (method.isAnnotationPresent(PatchMapping.class)) return HttpMethod.PATCH.name();
        if (method.isAnnotationPresent(DeleteMapping.class)) return HttpMethod.DELETE.name();
        return null;
    }
}
