package nextpos.app.nextpos.security.onboarding;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class OnboardingTokenService {

    private static final String PURPOSE = "company-onboarding";
    private final SecretKey key;
    private final long expirationMs;

    public OnboardingTokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${onboarding.token.expiration-ms:1800000}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String issue(Long companyId, String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(companyId.toString())
                .claim("purpose", PURPOSE)
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    public OnboardingContext verify(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            if (!PURPOSE.equals(claims.get("purpose", String.class))) {
                throw new BadCredentialsException("Invalid onboarding context");
            }
            return new OnboardingContext(Long.valueOf(claims.getSubject()), claims.get("email", String.class));
        } catch (RuntimeException exception) {
            throw new BadCredentialsException("Invalid or expired onboarding context", exception);
        }
    }
}
