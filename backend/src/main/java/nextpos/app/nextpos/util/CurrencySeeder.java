package nextpos.app.nextpos.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import nextpos.app.nextpos.service.impl.CurrencySeedServiceImpl;

@Component
public class CurrencySeeder implements CommandLineRunner {

    private final CurrencySeedServiceImpl currencySeederServiceImpl;

    public CurrencySeeder(CurrencySeedServiceImpl currencySeederServiceImpl) {
        this.currencySeederServiceImpl = currencySeederServiceImpl;
    }

    @Override
    public void run(String... args) throws Exception {
        currencySeederServiceImpl.seedCurrencies();
        System.out.println("Global currencies seeding completed!");
    }
}