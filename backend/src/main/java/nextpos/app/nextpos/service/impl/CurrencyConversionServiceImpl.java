package nextpos.app.nextpos.service.impl;

import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.service.interf.CurrencyConversionService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

        @Override
        public void convert(Sale sale) {
                BigDecimal rate = sale.getExchangeRate();
                sale.setSubtotalAmountBaseCurrency(
                                convert(sale.getSubtotalAmountTxnCurrency(), rate));
                sale.setTotalAmountBaseCurrency(
                                convert(sale.getTotalAmountTxnCurrency(), rate));
                sale.setGrandTotalBaseCurrency(
                                convert(sale.getGrandTotalTxnCurrency(), rate));
                sale.setPaidAmountBaseCurrency(
                                convert(sale.getPaidAmountTxnCurrency(), rate));
                sale.setDueAmountBaseCurrency(
                                convert(sale.getDueAmountTxnCurrency(), rate));
        }

        private BigDecimal convert(BigDecimal amount, BigDecimal rate) {
                if (amount == null)
                        return BigDecimal.ZERO;
                return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        }
}