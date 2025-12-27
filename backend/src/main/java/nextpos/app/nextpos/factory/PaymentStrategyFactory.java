package nextpos.app.nextpos.factory;

import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.strategy.*;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentStrategyFactory {

    private final Map<PaymentMethod, PaymentStrategy> strategyMap = new EnumMap<>(PaymentMethod.class);

    public PaymentStrategyFactory(List<PaymentStrategy> strategies) {
        for (PaymentStrategy strategy : strategies) {
            PaymentMethod method = resolveMethod(strategy);
            if (strategyMap.containsKey(method)) {
                throw new IllegalStateException("Duplicate strategy for method: " + method);
            }
            strategyMap.put(method, strategy);
        }
    }

    public PaymentStrategy getStrategy(PaymentMethod method) {
        if (method == null || !strategyMap.containsKey(method)) {
            throw new IllegalArgumentException("Unsupported or null payment method: " + method);
        }
        return strategyMap.get(method);
    }

    private PaymentMethod resolveMethod(PaymentStrategy strategy) {
        if (strategy instanceof CardPaymentStrategy)
            return PaymentMethod.CARD;
        if (strategy instanceof UpiPaymentStrategy)
            return PaymentMethod.UPI;
        if (strategy instanceof PaypalPaymentStrategy)
            return PaymentMethod.PAYPAL;
        if (strategy instanceof CashPaymentStrategy)
            return PaymentMethod.CASH;
        if (strategy instanceof ChequePaymentStrategy)
            return PaymentMethod.CHEQUE;
        if (strategy instanceof GiftCardPaymentStrategy)
            return PaymentMethod.GIFT_CARD;

        throw new IllegalStateException("Unknown strategy implementation: " + strategy.getClass());
    }
}
