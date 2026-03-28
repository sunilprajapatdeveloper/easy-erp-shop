<template>
    <label class="base-checkbox" :class="[
        `base-checkbox--${size}`,
        `base-checkbox--${theme}`,
        { 'base-checkbox--disabled': disabled }
    ]">
        <input ref="inputRef" type="checkbox" :checked="checked" :disabled="disabled" :value="value"
            class="base-checkbox__input" @change="onChange" @keydown.space.prevent="toggle" />
        <span class="base-checkbox__control">
            <svg v-if="checked" class="base-checkbox__check-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="20 6 9 17 4 12" />
            </svg>
        </span>
        <span v-if="label" class="base-checkbox__label">{{ label }}</span>
    </label>
</template>

<script lang="ts" setup>
import { computed, ref, watch, onMounted } from 'vue'

// Props definition
const props = withDefaults(
    defineProps<{
        modelValue: boolean | any[] | undefined
        label?: string
        disabled?: boolean
        indeterminate?: boolean
        size?: 'sm' | 'md' | 'lg'
        theme?: 'primary' | 'success' | 'warning' | 'danger' | 'purple'
        value?: any // optional value for array binding
    }>(),
    {
        size: 'md',
        theme: 'primary',
        disabled: false,
        indeterminate: false
    }
)

// Emits
const emit = defineEmits<{
    (e: 'update:modelValue', value: boolean | any[]): void
    (e: 'change', value: boolean | any[]): void
}>()

// Refs
const inputRef = ref<HTMLInputElement | null>(null)

// Computed checked state
const checked = computed(() => {
    // If value is provided (group mode), check if modelValue includes value
    if (props.value !== undefined) {
        return Array.isArray(props.modelValue) && props.modelValue.includes(props.value)
    }
    // Otherwise, treat as boolean, coerce undefined to false
    return Boolean(props.modelValue)
})

// Watch for indeterminate prop and sync to DOM
watch(
    () => props.indeterminate,
    (val) => {
        if (inputRef.value) {
            inputRef.value.indeterminate = val
        }
    },
    { immediate: true }
)

// On mount, ensure indeterminate state is set
onMounted(() => {
    if (inputRef.value && props.indeterminate) {
        inputRef.value.indeterminate = true
    }
})

// Handle change event
function onChange(event: Event) {
    const target = event.target as HTMLInputElement
    const isChecked = target.checked

    if (props.value !== undefined) {
        // Group mode: update array
        let newValue = Array.isArray(props.modelValue) ? [...props.modelValue] : []
        if (isChecked) {
            if (!newValue.includes(props.value)) {
                newValue.push(props.value)
            }
        } else {
            newValue = newValue.filter((v) => v !== props.value)
        }
        emit('update:modelValue', newValue)
        emit('change', newValue)
    } else {
        // Simple boolean mode
        emit('update:modelValue', isChecked)
        emit('change', isChecked)
    }
}

// Toggle on space key (accessibility)
function toggle() {
    if (props.disabled) return
    inputRef.value?.click()
}
</script>

<style scoped>
.base-checkbox {
    display: inline-flex;
    align-items: center;
    cursor: pointer;
    user-select: none;
    transition: color 0.2s ease;
}

.base-checkbox--disabled {
    cursor: not-allowed;
    opacity: 0.6;
}

.base-checkbox__input {
    position: absolute;
    opacity: 0;
    width: 0;
    height: 0;
    pointer-events: none;
}

.base-checkbox__control {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;
    border: 2px solid var(--checkbox-border, #d1d5db);
    background-color: var(--checkbox-bg, white);
    transition: all 0.2s ease;
    box-shadow: var(--checkbox-shadow, none);
}

.base-checkbox--sm .base-checkbox__control {
    width: 16px;
    height: 16px;
}

.base-checkbox--md .base-checkbox__control {
    width: 20px;
    height: 20px;
}

.base-checkbox--lg .base-checkbox__control {
    width: 24px;
    height: 24px;
}

.base-checkbox__check-icon {
    width: 70%;
    height: 70%;
    color: white;
    stroke-width: 2.5;
    display: block;
    transition: transform 0.1s ease;
}

.base-checkbox__label {
    margin-left: 8px;
    font-size: 0.875rem;
    color: var(--label-color, #1f2937);
}

/* Hover & focus states */
.base-checkbox:hover .base-checkbox__control {
    border-color: var(--checkbox-hover-border, #9ca3af);
    background-color: var(--checkbox-hover-bg, #f9fafb);
}

.base-checkbox__input:focus-visible+.base-checkbox__control {
    outline: 2px solid var(--focus-ring, #3b82f6);
    outline-offset: 2px;
}

/* Checked state */
.base-checkbox .base-checkbox__input:checked+.base-checkbox__control {
    background-color: var(--checkbox-checked-bg, var(--theme-color));
    border-color: var(--checkbox-checked-border, var(--theme-color));
}

/* Theme colors (CSS variables) */
.base-checkbox--primary {
    --theme-color: #4f46e5;
}

.base-checkbox--success {
    --theme-color: #10b981;
}

.base-checkbox--warning {
    --theme-color: #f59e0b;
}

.base-checkbox--danger {
    --theme-color: #ef4444;
}

.base-checkbox--purple {
    --theme-color: #8b5cf6;
}

.base-checkbox--primary .base-checkbox__input:checked+.base-checkbox__control {
    background-color: #4f46e5;
    border-color: #4f46e5;
}

.base-checkbox--success .base-checkbox__input:checked+.base-checkbox__control {
    background-color: #10b981;
    border-color: #10b981;
}

.base-checkbox--warning .base-checkbox__input:checked+.base-checkbox__control {
    background-color: #f59e0b;
    border-color: #f59e0b;
}

.base-checkbox--danger .base-checkbox__input:checked+.base-checkbox__control {
    background-color: #ef4444;
    border-color: #ef4444;
}

.base-checkbox--purple .base-checkbox__input:checked+.base-checkbox__control {
    background-color: #8b5cf6;
    border-color: #8b5cf6;
}
</style>