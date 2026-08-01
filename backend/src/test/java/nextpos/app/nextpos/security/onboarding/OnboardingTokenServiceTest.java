package nextpos.app.nextpos.security.onboarding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

class OnboardingTokenServiceTest {

    private static final String SECRET = "test-secret-that-is-at-least-thirty-two-bytes";

    @Test
    void tokenCryptographicallyBindsCompanyAndEmail() {
        OnboardingTokenService service = new OnboardingTokenService(SECRET, 60_000);
        OnboardingContext context = service.verify(service.issue(42L, "owner@example.test"));

        assertThat(context.companyId()).isEqualTo(42L);
        assertThat(context.email()).isEqualTo("owner@example.test");
    }

    @Test
    void rejectsExpiredAndTamperedTokens() {
        // JWT timestamps are serialized with second precision; keep the expiry safely
        // behind the current second so this assertion cannot race the clock boundary.
        OnboardingTokenService expiredService = new OnboardingTokenService(SECRET, -2_000);
        assertThatThrownBy(() -> expiredService.verify(expiredService.issue(42L, "owner@example.test")))
                .isInstanceOf(BadCredentialsException.class);

        OnboardingTokenService service = new OnboardingTokenService(SECRET, 60_000);
        String token = service.issue(42L, "owner@example.test");
        String tampered = token.substring(0, token.length() - 1) + (token.endsWith("a") ? "b" : "a");
        assertThatThrownBy(() -> service.verify(tampered)).isInstanceOf(BadCredentialsException.class);
    }
}
