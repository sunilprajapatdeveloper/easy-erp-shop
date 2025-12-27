package nextpos.app.nextpos.service.impl;

import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencySeedServiceImpl {

    private final CurrencyRepository currencyRepository;

    public CurrencySeedServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Transactional
    public void seedCurrencies() {
        List<Currency> currencies = List.of(
                // Major World Currencies
                Currency.builder().name("US Dollar").code("USD").symbol("$").build(),
                Currency.builder().name("Euro").code("EUR").symbol("€").build(),
                Currency.builder().name("British Pound").code("GBP").symbol("£").build(),
                Currency.builder().name("Japanese Yen").code("JPY").symbol("¥").build(),
                Currency.builder().name("Swiss Franc").code("CHF").symbol("CHF").build(),
                Currency.builder().name("Canadian Dollar").code("CAD").symbol("C$").build(),
                Currency.builder().name("Australian Dollar").code("AUD").symbol("A$").build(),
                Currency.builder().name("New Zealand Dollar").code("NZD").symbol("NZ$").build(),

                // Asian Currencies
                Currency.builder().name("Chinese Yuan").code("CNY").symbol("¥").build(),
                Currency.builder().name("Hong Kong Dollar").code("HKD").symbol("HK$").build(),
                Currency.builder().name("Singapore Dollar").code("SGD").symbol("S$").build(),
                Currency.builder().name("South Korean Won").code("KRW").symbol("₩").build(),
                Currency.builder().name("Indian Rupee").code("INR").symbol("₹").build(),
                Currency.builder().name("Thai Baht").code("THB").symbol("฿").build(),
                Currency.builder().name("Malaysian Ringgit").code("MYR").symbol("RM").build(),
                Currency.builder().name("Indonesian Rupiah").code("IDR").symbol("Rp").build(),
                Currency.builder().name("Philippine Peso").code("PHP").symbol("₱").build(),
                Currency.builder().name("Vietnamese Dong").code("VND").symbol("₫").build(),
                Currency.builder().name("Pakistani Rupee").code("PKR").symbol("₨").build(),
                Currency.builder().name("Bangladeshi Taka").code("BDT").symbol("৳").build(),

                // Middle Eastern Currencies
                Currency.builder().name("Saudi Riyal").code("SAR").symbol("﷼").build(),
                Currency.builder().name("UAE Dirham").code("AED").symbol("د.إ").build(),
                Currency.builder().name("Qatari Riyal").code("QAR").symbol("﷼").build(),
                Currency.builder().name("Kuwaiti Dinar").code("KWD").symbol("د.ك").build(),
                Currency.builder().name("Israeli New Shekel").code("ILS").symbol("₪").build(),
                Currency.builder().name("Turkish Lira").code("TRY").symbol("₺").build(),

                // African Currencies
                Currency.builder().name("South African Rand").code("ZAR").symbol("R").build(),
                Currency.builder().name("Egyptian Pound").code("EGP").symbol("£").build(),
                Currency.builder().name("Nigerian Naira").code("NGN").symbol("₦").build(),
                Currency.builder().name("Moroccan Dirham").code("MAD").symbol("د.م.").build(),
                Currency.builder().name("Kenyan Shilling").code("KES").symbol("KSh").build(),

                // European Currencies
                Currency.builder().name("Swedish Krona").code("SEK").symbol("kr").build(),
                Currency.builder().name("Norwegian Krone").code("NOK").symbol("kr").build(),
                Currency.builder().name("Danish Krone").code("DKK").symbol("kr").build(),
                Currency.builder().name("Polish Zloty").code("PLN").symbol("zł").build(),
                Currency.builder().name("Hungarian Forint").code("HUF").symbol("Ft").build(),
                Currency.builder().name("Czech Koruna").code("CZK").symbol("Kč").build(),
                Currency.builder().name("Russian Ruble").code("RUB").symbol("₽").build(),
                Currency.builder().name("Ukrainian Hryvnia").code("UAH").symbol("₴").build(),
                Currency.builder().name("Romanian Leu").code("RON").symbol("lei").build(),

                // American Currencies
                Currency.builder().name("Mexican Peso").code("MXN").symbol("$").build(),
                Currency.builder().name("Brazilian Real").code("BRL").symbol("R$").build(),
                Currency.builder().name("Argentine Peso").code("ARS").symbol("$").build(),
                Currency.builder().name("Chilean Peso").code("CLP").symbol("$").build(),
                Currency.builder().name("Colombian Peso").code("COP").symbol("$").build(),
                Currency.builder().name("Peruvian Sol").code("PEN").symbol("S/.").build(),

                // Additional Important Currencies
                Currency.builder().name("Danish Krone").code("DKK").symbol("kr").build(),
                Currency.builder().name("Croatian Kuna").code("HRK").symbol("kn").build(),
                Currency.builder().name("Bulgarian Lev").code("BGN").symbol("лв").build(),
                Currency.builder().name("Icelandic Króna").code("ISK").symbol("kr").build());

        for (Currency currency : currencies) {
            currencyRepository.findByCode(currency.getCode())
                    .orElseGet(() -> currencyRepository.save(currency));
        }

        System.out.println("Global currencies seeding completed!");
    }
}
