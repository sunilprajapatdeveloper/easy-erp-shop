<template>
    <component :is="currentComponent" :key="method + '-' + renderKey" :amount="totalAmount"
        @payment-completed="$emit('payment-completed', $event)" @close="$emit('close')" />
</template>

<script lang="ts">
import { defineComponent, computed, ref } from "vue";
import CardPayment from "./methods/CardPayment.vue";
import CashPayment from "./methods/CashPayment.vue";
import PaypalPayment from "./methods/PaypalPayment.vue";
import ChequePayment from "./methods/ChequePayment.vue";
import GiftCardPayment from "./methods/GiftCardPayment.vue";
import UpiPayment from "./methods/UPIPayment.vue";

const componentMap = {
    card: CardPayment,
    cash: CashPayment,
    paypal: PaypalPayment,
    cheque: ChequePayment,
    giftcard: GiftCardPayment,
    upi: UpiPayment,
} as const;

export default defineComponent({
    name: "PaymentGateway",
    props: {
        method: { type: String, required: true },
        totalAmount: { type: Number, required: true },
    },
    emits: ["payment-completed", "close"],
    setup(props) {
        const currentComponent = computed(() =>
            componentMap[props.method as keyof typeof componentMap] || null
        );

        // this key will be updated from parent (using :key on <PaymentGateway>)
        const renderKey = ref(0);

        return { currentComponent, renderKey };
    },
});
</script>
