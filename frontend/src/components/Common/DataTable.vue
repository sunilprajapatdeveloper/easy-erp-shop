<template>
    <div v-if="showBulkActions && selectedIds.length > 0"
        class="bulk-action-bar d-flex align-items-center justify-content-between mb-3 p-2 bg-light rounded">
        <div>
            <span class="fw-semibold">{{ selectedIds.length }}</span> {{ itemName }}(s) selected
        </div>
        <div>
            <button v-if="canDelete" class="btn btn-danger me-1" @click="emitBulkDelete">
                <img src="@/assets/img/icons/delete-bin-line-light.svg" alt="Delete" /> Delete Selected
            </button>
            <button class="btn btn-secondary" @click="clearSelection">Cancel</button>
        </div>
    </div>

    <div class="card border-0 shadow-none rounded-1 mb-25">
        <div class="card-body p-xl-40">
            <div class="table-responsive">
                <table class="table text-nowrap align-middle mb-0">
                    <thead>
                        <tr>
                            <th v-if="showBulkActions" scope="col" class="text-title fw-normal fs-14 pt-0 ps-0"
                                style="min-width: 120px;">
                                <div class="form-check checkbox">
                                    <input class="form-check-input" type="checkbox" :checked="isAllSelected"
                                        @change="toggleSelectAll" :id="selectAllId" />
                                    <label class="form-check-label" :for="selectAllId"> {{ selectionLabel }} </label>
                                </div>
                            </th>

                            <th v-for="col in columns" :key="col.field" scope="col"
                                :class="['text-title fw-normal fs-14 pt-0', col.headerClass]"
                                :style="{ minWidth: col.minWidth }"
                                @click="col.sortable ? handleSort(col.field) : undefined">
                                {{ col.label }}
                                <img v-if="col.sortable" class="ms-2 sort-icon-img"
                                    src="@/assets/img/icons/up-down-aroow.svg" alt="Sort" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="row in rows" :key="getRowId(row)">
                            <td v-if="showBulkActions" class="shadow-none fw-normal text-black title ps-0">
                                <div class="d-flex align-items-center product-item">
                                    <div class="form-check checkbox style-three me-25">
                                        <input class="form-check-input" type="checkbox" :value="getRowId(row)"
                                            v-model="selectedIds" :id="`row_${getRowId(row)}`" />
                                        <label class="form-check-label" :for="`row_${getRowId(row)}`"></label>
                                    </div>
                                    <slot name="checkbox-add-on" :row="row"></slot>
                                </div>
                            </td>

                            <td v-for="col in columns" :key="col.field"
                                :class="['shadow-none lh-1 fs-14 fw-normal text-paragraph', col.cellClass]">
                                <div v-if="col.field === 'actions'"
                                    class="button-group d-flex flex-wrap align-items-center">
                                    <slot :name="`cell(${col.field})`" :row="row" :value="row[col.field]"></slot>
                                </div>
                                <slot v-else :name="`cell(${col.field})`" :row="row" :value="row[col.field]">
                                    {{ row[col.field] ?? '—' }}
                                </slot>
                            </td>
                        </tr>

                        <tr v-if="loading">
                            <td :colspan="colspan" class="text-center py-4">
                                <div class="spinner-border text-primary" role="status"><span
                                        class="visually-hidden">Loading...</span></div>
                                <p class="mt-2 text-muted">Loading {{ itemName }}...</p>
                            </td>
                        </tr>
                        <tr v-if="!loading && rows.length === 0">
                            <td :colspan="colspan" class="text-center py-4">
                                <div class="text-muted">
                                    <i class="fas fa-box-open fa-2x mb-3"></i>
                                    <p class="mb-0">No {{ itemName }} found</p>
                                    <slot name="empty-action" />
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <Pagination :current-page="pagination.page" :total-pages="pagination.totalPages"
                :total-elements="pagination.totalElements" :page-size="pagination.size" :has-next="pagination.hasNext"
                :has-previous="pagination.hasPrevious" @update:page="$emit('page-change', $event)"
                @update:size="$emit('size-change', $event)" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import Pagination from '@/components/Common/Pagination.vue';

const props = defineProps({
    rows: { type: Array as any, required: true },
    loading: { type: Boolean, default: false },
    pagination: { type: Object as any, required: true },
    columns: { type: Array as any, required: true },
    sortBy: { type: String, default: '' },
    sortOrder: { type: String as any, default: 'asc' },
    showBulkActions: { type: Boolean, default: false },
    selectionLabel: { type: String, default: '' },
    canDelete: { type: Boolean, default: false },
    itemName: { type: String, default: 'item' },
    rowIdField: { type: String, default: 'id' },
});

const emit = defineEmits(['page-change', 'size-change', 'sort-change', 'bulk-delete', 'selection-change']);

const selectAllId = `selectAll_${Math.random().toString(36).substring(2, 10)}`;
const selectedIds = ref<any[]>([]);

watch(() => props.rows, () => { selectedIds.value = []; }, { deep: true });
watch(selectedIds, (newVal) => { emit('selection-change', newVal); });

const getRowId = (row: any) => row[props.rowIdField];
const isAllSelected = computed(() => props.rows.length > 0 && selectedIds.value.length === props.rows.length);
const colspan = computed(() => props.columns.length + (props.showBulkActions ? 1 : 0));

const handleSort = (field: string) => {
    let newOrder: 'asc' | 'desc' = (props.sortBy === field && props.sortOrder === 'asc') ? 'desc' : 'asc';
    emit('sort-change', { sortBy: field, sortOrder: newOrder });
};

const toggleSelectAll = (event: Event) => {
    const target = event.target as HTMLInputElement;
    selectedIds.value = target.checked ? props.rows.map((row: any) => getRowId(row)) : [];
};

const clearSelection = () => { selectedIds.value = []; };
const emitBulkDelete = () => {
    if (selectedIds.value.length === 0) return;
    emit('bulk-delete', [...selectedIds.value]);
    clearSelection();
};
</script>

<style scoped>
.table th {
    border-top: none;
    border-bottom: 1px solid #DEE2F1;
    cursor: pointer;
    user-select: none;
}

.table td {
    vertical-align: middle;
}

:deep(.btn) {
    padding: 0.25rem 0.5rem;
    font-size: 0.75rem;
    border-radius: 8px;
    transition: all 0.2s ease;
}

:deep(.btn-sm) {
    width: 32px;
    height: 32px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
}

.button-group .btn {
    margin-right: 0.5rem;
    background: transparent;
    border-width: 1.5px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
}

.button-group .btn:last-child {
    margin-right: 0;
}

/* View button (secondary) */
.button-group .btn-outline-secondary {
    border-color: #cbd5e1;
    color: #475569;
}

.button-group .btn-outline-secondary:hover {
    background-color: #f1f5f9;
    border-color: #94a3b8;
    color: #1e293b;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
}

/* Edit button (primary) */
.button-group .btn-outline-primary {
    border-color: #c7d2fe;
    color: #4f46e5;
}

.button-group .btn-outline-primary:hover {
    background: linear-gradient(135deg, #eef2ff, #e0e7ff);
    border-color: #6366f1;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(79, 70, 229, 0.15);
}

/* Delete button (danger) */
.button-group .btn-outline-danger {
    border-color: #fecaca;
    color: #dc2626;
}

.button-group .btn-outline-danger:hover {
    background: #fee2e2;
    border-color: #f87171;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(220, 38, 38, 0.1);
}

/* ========== STATUS BADGES – MODERN PILLS ========== */
:deep(.badge) {
    padding: 6px 14px;
    font-weight: 500;
    border-radius: 40px;
    font-size: 12px;
    letter-spacing: 0.3px;
    transition: all 0.2s;
    display: inline-flex;
    align-items: center;
    gap: 6px;
}

/* Active status */
:deep(.bg-success) {
    background: linear-gradient(135deg, #10b98115, #05966910) !important;
    color: #059669;
    border: 1px solid #10b98130;
}

/* Inactive status */
:deep(.bg-warning) {
    background: linear-gradient(135deg, #f59e0b15, #d9770610) !important;
    color: #d97706;
    border: 1px solid #f59e0b30;
}

/* Discontinued / Danger status */
:deep(.bg-danger) {
    background: linear-gradient(135deg, #ef444415, #dc262610) !important;
    color: #dc2626;
    border: 1px solid #ef444430;
}

/* Default / Secondary status */
:deep(.bg-secondary) {
    background: linear-gradient(135deg, #64748b15, #47556910) !important;
    color: #475569;
    border: 1px solid #64748b30;
}

/* Optional: add a subtle hover effect on badges */
:deep(.badge:hover) {
    transform: translateY(-1px);
    filter: brightness(0.98);
}

.table-responsive {
    scrollbar-width: thin;
    scrollbar-color: #cbd5e1 #f1f5f9;
}

.table-responsive::-webkit-scrollbar {
    height: 8px;
    width: 8px;
}

.table-responsive::-webkit-scrollbar-track {
    background: #f1f5f9;
    border-radius: 10px;
}

.table-responsive::-webkit-scrollbar-thumb {
    background: #cbd5e1;
    border-radius: 10px;
    transition: background 0.2s;
}

.table-responsive::-webkit-scrollbar-thumb:hover {
    background: #94a3b8;
}

.sort-icon-img {
    cursor: pointer;
    width: 12px;
    height: 12px;
    transition: opacity 0.2s;
}

.bulk-action-bar {
    background-color: #f8f9fa;
    border-left: 4px solid #0d6efd;
}

.product-item .form-check {
    margin-right: 10px;
}

@media (max-width: 768px) {
    .table-responsive {
        font-size: 0.875rem;
    }
}
</style>