<template>
    <div class="cheque-payment">
        <h5 class="mb-3 text-center text-title">Cheque Payment</h5>
        <form @submit.prevent="submitPayment">
            <div class="mb-3">
                <label class="form-label text-black">Cheque Number</label>
                <input v-model="chequeNumber" type="text" class="form-control" placeholder="Enter Cheque Number"
                    required />
            </div>

            <div class="mb-3">
                <label class="form-label text-black">Bank Name</label>
                <input v-model="bankName" type="text" class="form-control" placeholder="Enter Bank Name" required />
            </div>

            <div class="mb-3">
                <label class="form-label text-black">Cheque Date</label>
                <input v-model="chequeDate" type="date" class="form-control" required />
            </div>

            <div class="d-flex justify-content-between align-items-center">
                <strong>Total: ₹{{ totalAmount?.toFixed(2) }}</strong>
                <button type="submit" class="btn btn-primary">Submit Cheque</button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
    name: "ChequePayment",
    props: {
        totalAmount: {
            type: Number,
            required: true,
        },
    },
    emits: ["payment-completed"],
    setup(props, { emit }) {
        const chequeNumber = ref("");
        const bankName = ref("");
        const chequeDate = ref("");

        const submitPayment = () => {
            if (!chequeNumber.value || !bankName.value || !chequeDate.value) {
                alert("Please fill in all cheque details.");
                return;
            }

            emit("payment-completed", {
                method: "cheque",
                amountPaid: props.totalAmount,
                chequeNumber: chequeNumber.value,
                bankName: bankName.value,
                chequeDate: chequeDate.value,
                status: "pending",
            });
        };

        return {
            chequeNumber,
            bankName,
            chequeDate,
            submitPayment,
        };
    },
});
</script>

<style scoped>
.cheque-payment {
    padding: 1rem;
}

.cheque-payment input {
    height: 45px;
}

.cheque-payment button {
    min-width: 150px;
}
</style>
