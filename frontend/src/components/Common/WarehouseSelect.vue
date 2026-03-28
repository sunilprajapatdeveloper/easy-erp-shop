<template>
    <div class="warehouse-select">
        <label class="d-block fs-14 text-black mb-2">Warehouse</label>
        <select :value="selectedValue" @change="onChange"
            class="bg-white border-0 rounded-1 fs-14 text-optional w-100 h-55" :disabled="disabled" required>
            <option :value="0">Choose Warehouse</option>
            <option v-for="w in warehouses" :key="w.id" :value="w.id">
                {{ w.name }}
            </option>
        </select>
    </div>
</template>

<script lang="ts" setup>
import { computed, onMounted } from 'vue';
import { useWarehouseStore } from '@/stores/warehouseStore';

const props = defineProps<{
    modelValue: number | null;
    disabled?: boolean;
}>();

const emit = defineEmits<{
    (e: 'update:modelValue', value: number | null): void;
}>();

const warehouseStore = useWarehouseStore();
const warehouses = computed(() => warehouseStore.warehouses);

// Convert modelValue (null) to 0 for the select binding
const selectedValue = computed(() => props.modelValue ?? 0);

onMounted(async () => {
    await warehouseStore.fetchWarehouses();
});

function onChange(event: Event) {
    const target = event.target as HTMLSelectElement;
    const value = target.value === '' ? null : Number(target.value);
    // If the user selects the placeholder (value 0), emit null; otherwise emit the number
    emit('update:modelValue', value === 0 ? null : value);
}
</script>