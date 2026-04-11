<template>
    <div class="pagination-wrapper" v-if="totalPages > 0">
        <div class="pagination-info">
            <span class="text-muted">
                Showing {{ startRecord }} - {{ endRecord }} of {{ totalElements }} items
            </span>
        </div>
        <div class="pagination-controls">
            <!-- Page size selector -->
            <div class="page-size-selector">
                <select v-model="localSize" @change="onSizeChange" class="form-select form-select-sm">
                    <option value="10">10 per page</option>
                    <option value="20">20 per page</option>
                    <option value="50">50 per page</option>
                    <option value="100">100 per page</option>
                </select>
            </div>

            <!-- Pagination buttons -->
            <ul class="pagination">
                <!-- First page -->
                <li class="page-item" :class="{ disabled: !hasPrevious }">
                    <a class="page-link" href="#" @click.prevent="onFirst" aria-label="First">
                        <i class="fas fa-angle-double-left"></i>
                    </a>
                </li>
                <!-- Previous page -->
                <li class="page-item" :class="{ disabled: !hasPrevious }">
                    <a class="page-link" href="#" @click.prevent="onPrev" aria-label="Previous">
                        <i class="fas fa-chevron-left"></i>
                    </a>
                </li>

                <!-- Page numbers with ellipsis -->
                <template v-for="page in visiblePages" :key="typeof page === 'number' ? page : 'ellipsis'">
                    <li v-if="page === '...'" class="page-item disabled">
                        <span class="page-link">...</span>
                    </li>
                    <li v-else class="page-item" :class="{ active: page === currentPage }">
                        <a class="page-link" href="#" @click.prevent="onPageChange(page)">
                            {{ page + 1 }}
                        </a>
                    </li>
                </template>

                <!-- Next page -->
                <li class="page-item" :class="{ disabled: !hasNext }">
                    <a class="page-link" href="#" @click.prevent="onNext" aria-label="Next">
                        <i class="fas fa-chevron-right"></i>
                    </a>
                </li>
                <!-- Last page -->
                <li class="page-item" :class="{ disabled: !hasNext }">
                    <a class="page-link" href="#" @click.prevent="onLast" aria-label="Last">
                        <i class="fas fa-angle-double-right"></i>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, computed, ref, watch } from 'vue';

export default defineComponent({
    name: 'Pagination',
    props: {
        currentPage: { type: Number, required: true },
        totalPages: { type: Number, required: true },
        totalElements: { type: Number, required: true },
        pageSize: { type: Number, required: true },
        hasNext: { type: Boolean, required: true },
        hasPrevious: { type: Boolean, required: true },
    },
    emits: ['update:page', 'update:size'],
    setup(props, { emit }) {
        const localSize = ref(props.pageSize);
        watch(() => props.pageSize, (val) => { localSize.value = val; });

        // Calculate displayed page numbers with ellipsis
        const visiblePages = computed<(number | '...')[]>(() => {
            const current = props.currentPage;
            const total = props.totalPages;
            const delta = 2; // number of pages to show on each side of current
            const range: number[] = [];
            const result: (number | '...')[] = [];

            // Build full range
            for (let i = 0; i < total; i++) range.push(i);

            // Add ellipsis
            for (let i = 0; i < range.length; i++) {
                if (i === 0 || i === range.length - 1 || Math.abs(i - current) <= delta) {
                    result.push(range[i]);
                } else if (result[result.length - 1] !== '...') {
                    result.push('...');
                }
            }
            return result;
        });

        const startRecord = computed(() => props.currentPage * props.pageSize + 1);
        const endRecord = computed(() => Math.min((props.currentPage + 1) * props.pageSize, props.totalElements));

        const onPageChange = (page: number) => emit('update:page', page);
        const onPrev = () => { if (props.hasPrevious) emit('update:page', props.currentPage - 1); };
        const onNext = () => { if (props.hasNext) emit('update:page', props.currentPage + 1); };
        const onFirst = () => { if (props.hasPrevious) emit('update:page', 0); };
        const onLast = () => { if (props.hasNext) emit('update:page', props.totalPages - 1); };
        const onSizeChange = () => emit('update:size', localSize.value);

        return {
            localSize,
            visiblePages,
            startRecord,
            endRecord,
            onPageChange,
            onPrev,
            onNext,
            onFirst,
            onLast,
            onSizeChange,
        };
    },
});
</script>

<style scoped>
/* (styles remain unchanged from previous answer) */
.pagination-wrapper {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
    margin-top: 1.5rem;
    padding-top: 1rem;
    border-top: 1px solid #e9ecef;
}

.pagination-info {
    font-size: 0.875rem;
    color: #6c757d;
}

.pagination-controls {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 1rem;
}

.page-size-selector .form-select {
    width: auto;
    border-radius: 0.375rem;
    border-color: #dee2e6;
    background-color: #fff;
    font-size: 0.875rem;
    padding: 0.375rem 2rem 0.375rem 0.75rem;
    cursor: pointer;
}

.pagination {
    display: flex;
    list-style: none;
    gap: 0.25rem;
    margin: 0;
    padding: 0;
}

.page-item {
    display: inline-block;
}

.page-link {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 2rem;
    height: 2rem;
    padding: 0 0.5rem;
    border-radius: 0.375rem;
    border: 1px solid #dee2e6;
    background-color: #fff;
    color: #495057;
    font-size: 0.875rem;
    text-decoration: none;
    transition: all 0.2s ease-in-out;
}

.page-link i {
    font-size: 0.75rem;
}

.page-item:not(.disabled) .page-link:hover {
    background-color: #f8f9fa;
    border-color: #dee2e6;
    color: #7367f0;
    transform: translateY(-1px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.page-item.active .page-link {
    background-color: #7367f0;
    border-color: #7367f0;
    color: white;
    font-weight: 500;
}

.page-item.disabled .page-link {
    background-color: #f8f9fa;
    color: #adb5bd;
    cursor: not-allowed;
    opacity: 0.6;
}

/* Responsive */
@media (max-width: 768px) {
    .pagination-wrapper {
        flex-direction: column;
        align-items: stretch;
        gap: 0.75rem;
    }

    .pagination-controls {
        justify-content: center;
    }

    .pagination {
        flex-wrap: wrap;
        justify-content: center;
    }

    .page-link {
        min-width: 1.75rem;
        height: 1.75rem;
        font-size: 0.75rem;
    }
}
</style>