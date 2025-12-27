<template>
    <div class="payment-section">
        <!-- Loader -->
        <PreLoader v-if="isLoading" />

        <!-- Total Amount -->
        <div class="total-header mb-3">
            <span>Total Amount</span>
            <h4 class="mb-0 text-primary">
                ₹{{ typeof props.grandTotal === 'number' ? props.grandTotal.toFixed(2) : '0.00' }}
            </h4>
        </div>

        <!-- Payment Rows -->
        <div class="payment-list">
            <div v-for="(payment, index) in payments" :key="index"
                class="payment-item d-flex align-items-center justify-content-between mb-3 p-3 rounded shadow-sm">
                <!-- Payment Method -->
                <div class="flex-grow-1 me-3">
                    <select class="form-select fw-semibold" v-model="payment.method" :disabled="payment.paid">
                        <option disabled value="">Select Payment</option>
                        <option v-for="method in paymentMethods" :key="method.value" :value="method.value">
                            {{ method.label }}
                        </option>
                    </select>
                </div>

                <!-- Amount -->
                <div class="me-3">
                    <input type="number" class="form-control text-end fw-bold" :min="1"
                        :max="remainingAmount + payment.amount" v-model.number="payment.amount"
                        :disabled="payment.paid" />
                </div>

                <!-- Pay Button -->
                <div>
                    <button type="button" class="btn pay-btn fw-semibold"
                        :disabled="!payment.method || payment.amount <= 0 || payment.paid"
                        @click="processPayment(index)">
                        {{ payment.paid ? 'Paid' : 'Pay' }}
                    </button>
                </div>
            </div>
        </div>

        <!-- Remaining Amount -->
        <div class="remaining-amount text-end mt-2 fw-bold"
            :class="remainingAmount > 0 ? 'text-warning' : 'text-success'">
            {{ remainingAmount > 0 ? `Remaining: ₹${remainingAmount}` : '✅ Payment Completed' }}
        </div>

        <button type="button" class="btn custom-theme-btn text-uppercase w-100 mt-3 fw-normal fs-14"
            @click="completeSale">
            COMPLETE SALE
        </button>
    </div>
</template>

<script setup lang="ts">
import { PaymentMethod } from "@/enums/paymentMethods";
import { ref, reactive, computed, watch, defineProps, defineEmits } from "vue";
import { buildSalePaymentRequest } from "@/utils/paymentUtils";
import { PaymentStatus } from "@/enums/paymentStatus";
import { usePaymentStore } from "@/stores/paymentStore";
import PreLoader from "@/components/Layouts/PreLoader.vue";

const paymentStore = usePaymentStore();
const isLoading = ref(false);

const props = defineProps<{ grandTotal: number }>();

interface PaymentCompletedEvent {
    method: string;
    amountPaid?: number;
    status: "paid" | "failed" | "pending";
    transactionReference: string;
}

const emit = defineEmits<{
    (e: "payment-processed", payload: { method: PaymentMethod | string; amount: number }): void;
    (e: "open-payment-modal", method: string): void;
    (e: "confirm-sale"): void;
    (e: "payment-completed", payload: PaymentCompletedEvent): void;
}>();

const paymentMethods = [
    { value: PaymentMethod.CASH, label: "💵 Cash" },
    { value: PaymentMethod.UPI, label: "📱 UPI" },
    { value: PaymentMethod.CARD, label: "💳 Card" },
    { value: PaymentMethod.WALLET, label: "👛 Wallet" },
];

interface PaymentRow {
    method: string;
    amount: number;
    paid: boolean;
}

// Initialize payments with grandTotal
const payments = reactive<PaymentRow[]>([
    { method: "", amount: props.grandTotal || 0, paid: false },
]);

// Compute remainingAmount dynamically
const remainingAmount = computed(() => {
    return Math.max(
        props.grandTotal -
        payments.reduce((sum, p) => sum + (p.paid ? p.amount : 0), 0),
        0
    );
});

// Update first row if grandTotal changes
watch(
    () => props.grandTotal,
    (newTotal) => {
        if (payments.length === 0) {
            payments.push({ method: "", amount: newTotal || 0, paid: false });
        } else {
            payments[0].amount = newTotal || 0;
        }
    },
    { immediate: true }
);

// Process payment
const processPayment = async (index: number) => {
    const payment = payments[index];
    if (!payment.method || payment.amount <= 0) return;

    const methodUpper = payment.method.toUpperCase() as PaymentMethod;

    isLoading.value = true;
    try {
        const paymentReq = buildSalePaymentRequest({
            amount: payment.amount,
            paymentMethod: methodUpper,
            status: PaymentStatus.PAID,
        });

        await paymentStore.addPayment(paymentReq);

        // Mark this payment as paid
        payment.paid = true;

        // Emit processed event
        emit("payment-processed", { method: payment.method, amount: payment.amount });

        if (methodUpper !== PaymentMethod.CASH) {
            emit("open-payment-modal", payment.method.toLowerCase());
        } else {
            emit("payment-completed", {
                method: "cash",
                amountPaid: payment.amount,
                status: "paid",
                transactionReference: "",
            });
        }

        // Add new payment row if remaining amount exists
        if (remainingAmount.value > 0 && index === payments.length - 1) {
            payments.push({
                method: "",
                amount: remainingAmount.value,
                paid: false,
            });
        }
    } catch (error) {
        console.error("Payment failed:", error);
    } finally {
        isLoading.value = false;
    }
};

// Reset payment section
const resetPaymentSection = () => {
    payments.splice(0, payments.length);
    payments.push({ method: "", amount: props.grandTotal || 0, paid: false });
};

// Complete sale
const completeSale = () => {
    emit("confirm-sale");
    resetPaymentSection();
};

// Automatically reset when all payments done
watch(remainingAmount, (value) => {
    if (value === 0) {
        resetPaymentSection();
    }
});
</script>

<style scoped>
.payment-section {
    background: #fff;
    border-radius: 10px;
}

.total-header {
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    padding: 12px 15px;
    border-radius: 8px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.payment-item {
    background: #fdfdfd;
    border: 1px solid #e5e7eb;
    transition: background 0.2s ease-in-out;
}

.payment-item:hover {
    background: #f9fafb;
}

select.form-select {
    border: 1px solid #d1d5db;
    padding: 6px 8px;
    border-radius: 6px;
}

input.form-control {
    width: 120px;
    border-radius: 6px;
}

.pay-btn {
    background: linear-gradient(135deg, #6366f1, #10b981);
    border: none;
    color: white;
    border-radius: 6px;
}

.pay-btn:disabled {
    background: #d1d5db;
    cursor: not-allowed;
}

.remaining-amount {
    font-size: 1rem;
}

.custom-theme-btn {
    color: #fff;
    background: var(--Purple-Gradient,
            linear-gradient(132deg, #4f46e5 4.27%, #6366f1 100%));
    transition: 0.3s;
}

.custom-theme-btn:hover {
    color: #fff;
    background: var(--Purple-Gradient,
            linear-gradient(132deg, #6366f1 4.27%, #4f46e5 100%));
}
</style>
