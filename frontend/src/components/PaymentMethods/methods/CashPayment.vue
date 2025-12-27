<template>
    <div class="cash-payment">
        <h5 class="mb-3 text-center text-title">Cash Payment</h5>
        <form @submit.prevent="submitPayment">
            <div class="mb-3">
                <label class="form-label text-black">Enter Cash Received</label>
                <input v-model.number="cashReceived" type="number" class="form-control" placeholder="0.00" min="0"
                    required />
            </div>

            <div class="d-flex justify-content-between mb-2">
                <strong>Total: ₹{{ totalAmount?.toFixed(2) }}</strong>
                <strong>Change: ₹{{ changeAmount.toFixed(2) }}</strong>
            </div>

            <div class="text-end">
                <button type="submit" class="btn btn-success" :disabled="cashReceived < totalAmount">
                    Complete Payment
                </button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from "vue";

export default defineComponent({
    name: "CashPayment",
    props: {
        totalAmount: {
            type: Number,
            required: true,
        },
    },
    emits: ["payment-completed"],
    setup(props, { emit }) {
        const cashReceived = ref(0);

        const changeAmount = computed(() => {
            return cashReceived.value > props.totalAmount
                ? cashReceived.value - props.totalAmount
                : 0;
        });

        const submitPayment = () => {
            if (cashReceived.value < props.totalAmount) {
                alert("Cash received is less than the total amount.");
                return;
            }

            emit("payment-completed", {
                method: "cash",
                amountPaid: props.totalAmount,
                status: "paid",
                transactionReference: cashReceived.value
            });
        };

        return {
            cashReceived,
            changeAmount,
            submitPayment,
        };
    },
});
</script>

<style scoped>
.cash-payment {
    padding: 1rem;
}

.cash-payment input {
    height: 45px;
}

.cash-payment button {
    min-width: 150px;
}
</style>
