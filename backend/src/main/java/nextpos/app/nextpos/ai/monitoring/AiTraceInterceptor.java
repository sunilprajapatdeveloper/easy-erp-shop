package nextpos.app.nextpos.ai.monitoring;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AiTraceInterceptor {
    private final Tracer tracer;

    @Around("execution(* nextpos.app.nextpos.ai.orchestrator.AiOrchestrator.orchestrate(..))")
    public Object traceOrchestrator(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.spanBuilder("ai.orchestrate").startSpan();
        try (Scope scope = span.makeCurrent()) {
            return joinPoint.proceed();
        } catch (Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }
    }

    // Additional pointcuts for provider calls, tools, etc.
}