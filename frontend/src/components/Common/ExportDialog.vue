<template>
    <div class="modal fade" id="exportModal" tabindex="-1" aria-labelledby="exportModalLabel" aria-hidden="true"
        ref="modalRef">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title text-title" id="exportModalLabel">
                        Export {{ moduleName }}
                    </h5>
                    <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
                        <img src="../../assets/img/icons/close-circle-2.svg" alt="Close" />
                    </button>
                </div>
                <div class="modal-body">
                    <form @submit.prevent="submitExport">
                        <div class="row">
                            <div class="col-12">
                                <div class="form-group mb-20">
                                    <label class="d-block fs-14 fw-semibold text-title mb-8">Format</label>
                                    <select v-model="format" class="modern-select">
                                        <option value="EXCEL">Excel (XLSX)</option>
                                        <option value="CSV">CSV</option>
                                        <option value="PDF">PDF</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="form-group mb-20">
                                    <label class="d-block fs-14 fw-semibold text-title mb-8">
                                        Filters (optional)
                                    </label>
                                    <input type="text" class="modern-input" placeholder="e.g., status=ACTIVE"
                                        v-model="filters.status" />
                                </div>
                            </div>

                            <div class="col-lg-6 mb-md-15">
                                <button type="button" class="btn modern-btn-outline w-100" data-bs-dismiss="modal">
                                    Cancel
                                </button>
                            </div>
                            <div class="col-lg-6">
                                <button type="submit" class="btn modern-btn-primary w-100" :disabled="submitting">
                                    <span v-if="submitting" class="spinner-border spinner-border-sm me-2"></span>
                                    Export
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Progress section -->
                    <ImportProgress v-if="store.currentJob" :job="store.currentJob" />
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch, onMounted, onUnmounted } from 'vue';
import { Modal } from 'bootstrap';
import { useImportExportStore } from '@/stores/importExportStore';
import ImportProgress from './ImportProgress.vue';
import type { ExportFormat } from '@/types/importExport';

export default defineComponent({
    name: 'ExportDialog',
    components: { ImportProgress },
    props: {
        module: { type: String, required: true },
        moduleName: { type: String, required: true },
    },
    emits: ['exported', 'refresh'],
    setup(props, { emit }) {
        const store = useImportExportStore();
        const format = ref<ExportFormat>('EXCEL');
        const filters = ref<any>({});
        const modalRef = ref<HTMLElement | null>(null);
        const submitting = ref(false);
        const currentJobType = ref<string | null>(null);
        let modalInstance: Modal | null = null;

        onMounted(() => {
            if (modalRef.value) {
                modalInstance = new Modal(modalRef.value);
            }
        });

        onUnmounted(() => {
            if (modalInstance) {
                modalInstance.hide();
                modalInstance = null;
            }
        });

        const submitExport = async () => {
            submitting.value = true;
            try {
                const jobId = await store.startExport({
                    module: props.module,
                    format: format.value,
                    filters: filters.value,
                });
                currentJobType.value = 'EXPORT';
                emit('exported', jobId);
            } catch (error) {
                console.error('Export start failed:', error);
                alert('Failed to start export. Please try again.');
                if (modalInstance) modalInstance.hide();
            } finally {
                submitting.value = false;
            }
        };

        watch(
            () => store.currentJob,
            (newJob) => {
                if (
                    newJob &&
                    currentJobType.value === 'EXPORT' &&
                    (newJob.status === 'COMPLETED' || newJob.status === 'FAILED')
                ) {
                    if (newJob.status === 'COMPLETED') {
                        alert('Export completed. You can download the file using the button below.');
                    } else {
                        alert(`Export failed.\nError: ${newJob.errorSummary || 'Unknown error'}`);
                    }
                    emit('refresh');
                }
            }
        );

        return {
            store,
            format,
            filters,
            submitting,
            submitExport,
        };
    },
});
</script>

<style scoped>
/* ===== MODAL OVERRIDES (same as ImportDialog) ===== */
.modal-content {
    border: none;
    border-radius: 24px;
    background: linear-gradient(145deg, #ffffff 0%, #fefefe 100%);
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    overflow: hidden;
    animation: modalSlideIn 0.3s ease-out;
}

@keyframes modalSlideIn {
    from {
        opacity: 0;
        transform: translateY(30px) scale(0.96);
    }

    to {
        opacity: 1;
        transform: translateY(0) scale(1);
    }
}

.modal-header {
    border-bottom: 1px solid rgba(99, 102, 241, 0.15);
    padding: 20px 28px;
    background: rgba(255, 255, 255, 0.8);
    backdrop-filter: blur(8px);
}

.modal-title {
    font-size: 1.5rem;
    font-weight: 700;
    background: linear-gradient(135deg, #1e293b 0%, #4f46e5 100%);
    background-clip: text;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    margin: 0;
}

.btn-close {
    background: none;
    opacity: 0.7;
    transition: all 0.2s;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
}

.btn-close:hover {
    opacity: 1;
    background: #f1f5f9;
    transform: rotate(90deg);
}

.btn-close img {
    width: 20px;
    height: 20px;
}

.modal-body {
    padding: 28px;
}

/* ===== FORM ELEMENTS ===== */
.form-group {
    margin-bottom: 1.25rem;
}

label {
    font-weight: 600;
    color: #0f172a;
    margin-bottom: 0.5rem;
    display: block;
    letter-spacing: -0.2px;
}

/* Modern select & input */
.modern-select,
.modern-input {
    width: 100%;
    padding: 12px 18px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 16px;
    font-size: 14px;
    color: #0f172a;
    transition: all 0.2s;
}

.modern-select {
    appearance: none;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='%236366f1' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'%3E%3C/polyline%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-position: right 18px center;
    cursor: pointer;
}

.modern-input::placeholder {
    color: #94a3b8;
}

.modern-select:focus,
.modern-input:focus {
    outline: none;
    border-color: #6366f1;
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
    background-color: white;
}

/* ===== BUTTONS (same as ImportDialog) ===== */
.modern-btn-primary,
.modern-btn-outline {
    padding: 12px 20px;
    font-weight: 600;
    border-radius: 40px;
    transition: all 0.2s;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    border: none;
    cursor: pointer;
    font-size: 14px;
}

.modern-btn-primary {
    background: linear-gradient(105deg, #4f46e5 0%, #6366f1 100%);
    color: white;
    box-shadow: 0 4px 12px rgba(79, 70, 229, 0.25);
}

.modern-btn-primary:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(79, 70, 229, 0.35);
}

.modern-btn-primary:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

.modern-btn-outline {
    background: transparent;
    border: 1px solid #cbd5e1;
    color: #475569;
}

.modern-btn-outline:hover {
    background: #f8fafc;
    border-color: #94a3b8;
    transform: translateY(-1px);
}

/* Responsive */
@media (max-width: 576px) {
    .modal-body {
        padding: 20px;
    }

    .modal-header {
        padding: 16px 20px;
    }

    .modal-title {
        font-size: 1.25rem;
    }

    .modern-btn-primary,
    .modern-btn-outline {
        padding: 10px 16px;
    }
}
</style>