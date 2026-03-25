package nextpos.app.nextpos.ai.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiMetrics {
    private final MeterRegistry meterRegistry;

    public void recordRequest(String tenant, String provider, String status) {
        Counter.builder("ai.requests.total")
                .tag("tenant", tenant)
                .tag("provider", provider)
                .tag("status", status)
                .register(meterRegistry)
                .increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTimer(Timer.Sample sample, String tenant, String provider) {
        sample.stop(Timer.builder("ai.requests.latency")
                .tags("tenant", tenant, "provider", provider)
                .register(meterRegistry));
    }

    public void recordTokens(String tenant, String type, long tokens) {
        Counter.builder("ai.tokens.total")
                .tag("tenant", tenant)
                .tag("type", type)
                .register(meterRegistry)
                .increment(tokens);
    }

    public void recordCacheHit(String tenant) {
        Counter.builder("ai.cache.hit")
                .tag("tenant", tenant)
                .register(meterRegistry)
                .increment();
    }

    public void recordCacheMiss(String tenant) {
        Counter.builder("ai.cache.miss")
                .tag("tenant", tenant)
                .register(meterRegistry)
                .increment();
    }

    public void recordQuotaRemaining(String tenant, long remaining) {
        meterRegistry.gauge("ai.quota.remaining", remaining);
    }

    public void recordError(String tenant, String errorType) {
        Counter.builder("ai.errors")
                .tag("tenant", tenant)
                .tag("type", errorType)
                .register(meterRegistry)
                .increment();
    }
}