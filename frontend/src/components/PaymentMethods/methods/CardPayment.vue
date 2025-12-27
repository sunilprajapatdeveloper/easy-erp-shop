<template>
    <div class="card-payment">
        <h5 class="mb-3 text-center text-title">Card Payment</h5>
        <form @submit.prevent="submitPayment">
            <div class="mb-3">
                <label class="form-label text-black">Card Number</label>
                <input v-model="cardNumber" type="text" class="form-control" placeholder="1234 5678 9012 3456"
                    maxlength="19" required />
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label text-black">Expiry Date</label>
                    <input v-model="expiry" type="text" class="form-control" placeholder="MM/YY" maxlength="5"
                        required />
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label text-black">CVV</label>
                    <input v-model="cvv" type="password" class="form-control" placeholder="***" maxlength="4"
                        required />
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label text-black">Card Holder Name</label>
                <input v-model="cardHolder" type="text" class="form-control" placeholder="John Doe" required />
            </div>

            <div class="d-flex justify-content-between align-items-center">
                <strong>Total: ₹{{ totalAmount?.toFixed(2) }}</strong>
                <button type="submit" class="btn btn-primary">Process Payment</button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
    name: "CardPayment",
    props: {
        totalAmount: {
            type: Number,
            required: true,
        },
    },
    emits: ["payment-completed"],
    setup(props, { emit }) {
        const cardNumber = ref("");
        const expiry = ref("");
        const cvv = ref("");
        const cardHolder = ref("");

        const submitPayment = () => {
            if (!cardNumber.value || !expiry.value || !cvv.value || !cardHolder.value) {
                alert("Please fill all fields.");
                return;
            }

            emit("payment-completed", {
                method: "card",
                cardNumber: cardNumber.value,
                cardHolder: cardHolder.value,
                last4: cardNumber.value.slice(-4),
                amountPaid: props.totalAmount,
                status: "success",
            });
        };

        return {
            cardNumber,
            expiry,
            cvv,
            cardHolder,
            submitPayment,
        };
    },
});
</script>

<style scoped>
.card-payment {
    padding: 1rem;
}

.card-payment input {
    height: 45px;
}

.card-payment button {
    min-width: 150px;
}
</style>
