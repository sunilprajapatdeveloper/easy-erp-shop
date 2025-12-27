<template>
    <div class="mt-3">
        <h6 class="fw-semibold mb-2">Enter UPI ID</h6>
        <input type="text" placeholder="example@upi" class="form-control mb-2" />
        <button class="btn btn-success w-100">Pay via UPI</button>
    </div>
</template>

<script lang="ts">
export default {
    name: "PaymentUPIGateway",
};
</script>
