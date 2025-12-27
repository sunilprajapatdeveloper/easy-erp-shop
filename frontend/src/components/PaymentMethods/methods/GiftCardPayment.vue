<template>
    <div class="giftcard-payment">
        <h5 class="mb-3 text-center text-title">Gift Card Payment</h5>
        <form @submit.prevent="submitPayment">
            <div class="mb-3">
                <label class="form-label text-black">Gift Card Number</label>
                <input v-model="giftCardNumber" type="text" class="form-control" placeholder="Enter Gift Card Number"
                    required />
            </div>

            <div class="mb-3">
                <label class="form-label text-black">PIN (if applicable)</label>
                <input v-model="pin" type="password" class="form-control" placeholder="Optional PIN" />
            </div>

            <div class="d-flex justify-content-between align-items-center">
                <strong>Total: ₹{{ totalAmount?.toFixed(2) }}</strong>
                <button type="submit" class="btn btn-primary">Apply Gift Card</button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
    name: "GiftCardPayment",
    props: {
        totalAmount: {
            type: Number,
            required: true,
        },
    },
    emits: ["payment-completed"],
    setup(props, { emit }) {
        const giftCardNumber = ref("");
        const pin = ref("");

        const submitPayment = () => {
            if (!giftCardNumber.value) {
                alert("Please enter the gift card number.");
                return;
            }

            emit("payment-completed", {
                method: "gift_card",
                giftCardNumber: giftCardNumber.value,
                pin: pin.value || null,
                amountPaid: props.totalAmount,
                status: "success",
            });
        };

        return {
            giftCardNumber,
            pin,
            submitPayment,
        };
    },
});
</script>

<style scoped>
.giftcard-payment {
    padding: 1rem;
}

.giftcard-payment input {
    height: 45px;
}

.giftcard-payment button {
    min-width: 150px;
}
</style>
