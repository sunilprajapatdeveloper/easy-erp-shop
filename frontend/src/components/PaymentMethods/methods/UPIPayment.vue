<template>
    <div class="upi-payment text-center">
        <h5 class="mb-3 text-title">UPI Payment</h5>

        <!-- QR Code and Payment Info -->
        <template v-if="payment && !paymentDone">
            <div class="qr-container mb-3">
                <QrcodeVue :value="upiLink" :size="200" level="H" />
            </div>

            <h6 class="mb-2">
                Amount to Pay: ₹{{ Number(payment.amount).toFixed(2) }}
            </h6>
            <p class="text-black">Scan using PhonePe, Google Pay, Paytm, etc.</p>
            <p class="text-muted">or pay to this UPI ID:</p>
            <strong class="d-block mb-3">{{ upiId }}</strong>
        </template>

        <!-- Payment Done Message -->
        <div v-else-if="paymentDone" class="alert alert-success mt-4" role="alert">
            Payment of ₹{{ Number(payment?.amount ?? 0).toFixed(2) }} was marked as
            completed successfully!
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, defineEmits } from "vue";
import { usePaymentStore } from "@/stores/paymentStore";
import { storeToRefs } from "pinia";
import QrcodeVue from "qrcode.vue";

const emit = defineEmits<{
    (e: "payment-completed", payload: unknown): void;
    (e: "close"): void;
}>();

const paymentStore = usePaymentStore();
const { tempPayment: payment } = storeToRefs(paymentStore);

const paymentDone = ref(false);

const upiLink = computed(() => payment.value?.paymentMetadata?.upiLink ?? "");
const upiId = computed(() => payment.value?.paymentMetadata?.upiId ?? "merchant@upi");

const resetState = () => {
    paymentDone.value = false;
    paymentStore.clearTempPayment();
    paymentStore.stopPollingPaymentStatus();
};

const startPolling = () => {
    console.log("Payment ID: " + payment.value?.id);
    if (!payment.value?.id) return;

    paymentStore.startPollingPaymentStatus(payment.value.id, (status) => {
        if (status === "PAID") {
            paymentDone.value = true;
            emit("payment-completed", {
                method: "upi",
                amountPaid: payment.value?.amount,
                status: "paid",
                transactionReference: payment.value?.transactionReference ?? "",
            });

            closeAndClear();
        }
    });

    // Auto-stop after 60 seconds
    setTimeout(() => {
        paymentStore.stopPollingPaymentStatus();
    }, 60000);
};

const closeAndClear = () => {
    resetState();
    emit("close");
};

// reset when mounted (new modal open)
onMounted(() => {
    console.log("Start polling...");
    startPolling();
});

// cleanup when unmounted
onUnmounted(() => {
    resetState();
});
</script>

<style scoped>
.qr-container {
    display: flex;
    justify-content: center;
}
</style>
