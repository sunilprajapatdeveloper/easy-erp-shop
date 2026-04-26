package nextpos.app.nextpos.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.service.impl.ExchangeRateSyncService;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ExchangeRateScheduler {

    private final ExchangeRateSyncService syncService;

    // Run every day at 2:00 AM
    @Scheduled(cron = "0 0 2 * * *")
    public void syncGlobalRatesJob() {
        log.info("Running scheduled global exchange rate sync");
        syncService.syncGlobalRates();
    }
}