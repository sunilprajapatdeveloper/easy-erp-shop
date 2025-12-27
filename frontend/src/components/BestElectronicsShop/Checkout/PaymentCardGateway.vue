<template>
    <div class="card border-0 shadow-sm p-4">
        <h5 class="fw-semibold mb-4 text-center">Pay with Card</h5>

        <div class="mb-3">
            <label class="form-label">Card Number</label>
            <div class="input-group">
                <span class="input-group-text"><i class="bi bi-credit-card-2-front"></i></span>
                <input type="text" class="form-control" v-model="cardNumber" placeholder="1234 5678 9012 3456"
                    maxlength="19" />
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Expiry Date</label>
                <input type="text" class="form-control" v-model="expiry" placeholder="MM/YY" maxlength="5" />
            </div>
            <div class="col-md-6">
                <label class="form-label">CVV</label>
                <input type="password" class="form-control" v-model="cvv" placeholder="123" maxlength="4" />
            </div>
        </div>

        <div class="mb-4">
            <label class="form-label">Cardholder Name</label>
            <input type="text" class="form-control" v-model="name" placeholder="Full Name" />
        </div>

        <button class="btn btn-primary w-100" :disabled="loading" @click="submitPayment">
            <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status"></span>
            {{ loading ? "Processing..." : "Pay Now" }}
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
    name: "PaymentCardGateway",
    setup() {
        const cardNumber = ref("");
        const expiry = ref("");
        const cvv = ref("");
        const name = ref("");
        const loading = ref(false);

        const submitPayment = () => {
            loading.value = true;
            // Simulate API call
            setTimeout(() => {
                loading.value = false;
                alert("Payment successful (simulated)!");
            }, 2000);
        };

        return {
            cardNumber,
            expiry,
            cvv,
            name,
            loading,
            submitPayment,
        };
    },
});
</script>

<style scoped>
.input-group-text i {
    font-size: 1.2rem;
    color: #6c757d;
}
</style>
