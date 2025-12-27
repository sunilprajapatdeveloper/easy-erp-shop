<template>
    <!-- Success Popup -->
    <div ref="successPopup" class="success-popup offcanvas offcanvas-end border-0" tabindex="-1" id="successPopup">
        <div class="offcanvas-body p-0">
            <div class="create-success">
                <img src="@/assets/img/icons/tick-circle.svg" alt="Success" />
                <span class="text-white fw-medium">{{ successMessage }}</span>
            </div>
        </div>
    </div>

    <!-- Error Popup -->
    <div ref="errorPopup" class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
        <div class="offcanvas-body p-0">
            <div class="create-error">
                <img src="@/assets/img/icons/close-circle-2.svg" alt="Error" style="filter: brightness(0) invert(1);" />
                <span class="text-white fw-medium">{{ errorMessage }}</span>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from "vue";
import { Offcanvas } from "bootstrap";

let successInstance: Offcanvas | null = null;
let errorInstance: Offcanvas | null = null;

const successMessage = ref("");
const errorMessage = ref("");

export function showPopup(
    type: "success" | "error",
    message: string,
    duration = 3000
) {
    if (type === "success") {
        successMessage.value = message;
        successInstance?.show();
        setTimeout(() => successInstance?.hide(), duration);
    } else {
        errorMessage.value = message;
        errorInstance?.show();
        setTimeout(() => errorInstance?.hide(), duration);
    }
}

export default defineComponent({
    name: "AppPopup",
    setup() {
        const successPopup = ref<HTMLElement | null>(null);
        const errorPopup = ref<HTMLElement | null>(null);

        onMounted(() => {
            if (successPopup.value) {
                successInstance = new Offcanvas(successPopup.value);
            }
            if (errorPopup.value) {
                errorInstance = new Offcanvas(errorPopup.value);
            }
        });

        return {
            successPopup,
            errorPopup,
            successMessage,
            errorMessage
        };
    }
});
</script>
