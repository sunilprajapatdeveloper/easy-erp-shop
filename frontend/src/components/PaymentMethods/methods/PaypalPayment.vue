<template>
    <div class="paypal-payment">
        <h5 class="mb-3 text-center text-title">PayPal Payment</h5>
        <form @submit.prevent="submitPayment">
            <div class="mb-3">
                <label class="form-label text-black">PayPal Email</label>
                <input v-model="paypalEmail" type="email" class="form-control" placeholder="user@example.com"
                    required />
            </div>

            <div class="mb-3">
                <label class="form-label text-black">Password</label>
                <input v-model="paypalPassword" type="password" class="form-control" placeholder="Enter PayPal Password"
                    required />
            </div>

            <div class="d-flex justify-content-between align-items-center">
                <strong>Total: ₹{{ totalAmount?.toFixed(2) }}</strong>
                <button type="submit" class="btn btn-primary">Pay with PayPal</button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
    name: "PaypalPayment",
    props: {
        totalAmount: {
            type: Number,
            required: true,
        },
    },
    emits: ["payment-completed"],
    setup(props, { emit }) {
        const paypalEmail = ref("");
        const paypalPassword = ref("");

        const submitPayment = () => {
            if (!paypalEmail.value || !paypalPassword.value) {
                alert("Please fill in both email and password.");
                return;
            }

            emit("payment-completed", {
                method: "paypal",
                email: paypalEmail.value,
                amountPaid: props.totalAmount,
                status: "success",
            });
        };

        return {
            paypalEmail,
            paypalPassword,
            submitPayment,
        };
    },
});
</script>

<style scoped>
.paypal-payment {
    padding: 1rem;
}

.paypal-payment input {
    height: 45px;
}

.paypal-payment button {
    min-width: 150px;
}
</style>
