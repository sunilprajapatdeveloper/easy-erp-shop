<template>
    <div class="modal fade" id="importModal" tabindex="-1" aria-labelledby="importModalLabel" aria-hidden="true"
        ref="modalRef">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title text-title" id="importModalLabel">
                        Import {{ moduleName }}
                    </h5>
                    <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
                        <img src="../../assets/img/icons/close-circle-2.svg" alt="Close" />
                    </button>
                </div>
                <div class="modal-body">
                    <form @submit.prevent="submitImport">
                        <div class="row">
                            <div class="col-12">
                                <div class="form-group mb-20">
                                    <label class="d-block fs-14 fw-semibold text-title mb-8">File (Excel or CSV)</label>
                                    <div class="file-upload-wrapper">
                                        <input type="file" class="file-input" accept=".xlsx,.xls,.csv"
                                            @change="onFileSelected" required ref="fileInputRef" />
                                        <div class="file-upload-label">
                                            <span v-if="!selectedFile">Choose file</span>
                                            <span v-else class="selected-file">{{ selectedFile.name }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Optional warehouse selection -->
                            <div class="col-12" v-if="showOptions">
                                <div class="form-group mb-20">
                                    <label class="d-block fs-14 fw-semibold text-title mb-8">Warehouse</label>
                                    <select v-model="options.warehouseId" class="modern-select"
                                        v-if="warehouses && warehouses.length">
                                        <option :value="null">-- Select Warehouse --</option>
                                        <option v-for="w in warehouses" :key="w.id" :value="w.id">
                                            {{ w.name }}
                                        </option>
                                    </select>
                                </div>
                                <div class="form-check mb-20">
                                    <input type="checkbox" class="form-check-input modern-checkbox"
                                        id="overwriteExisting" v-model="options.overwriteExisting" />
                                    <label class="form-check-label" for="overwriteExisting">
                                        Overwrite existing products
                                    </label>
                                </div>
                            </div>

                            <div class="col-lg-6 mb-md-15">
                                <button type="button" class="btn modern-btn-outline w-100" data-bs-dismiss="modal">
                                    Cancel
                                </button>
                            </div>
                            <div class="col-lg-6">
                                <button type="submit" class="btn modern-btn-primary w-100"
                                    :disabled="!selectedFile || submitting">
                                    <span v-if="submitting" class="spinner-border spinner-border-sm me-2"></span>
                                    Import
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
import { defineComponent, ref, watch, PropType, onMounted, onUnmounted } from 'vue';
import { Modal } from 'bootstrap';
import { useImportExportStore } from '@/stores/importExportStore';
import ImportProgress from './ImportProgress.vue';
import type { WarehouseListItem } from '@/types/Warehouse';

export default defineComponent({
    name: 'ImportDialog',
    components: { ImportProgress },
    props: {
        module: { type: String, required: true },
        moduleName: { type: String, required: true },
        showOptions: { type: Boolean, default: false },
        warehouses: { type: Array as PropType<WarehouseListItem[]>, default: () => [] },
    },
    emits: ['imported', 'refresh'],
    setup(props, { emit }) {
        const store = useImportExportStore();
        const selectedFile = ref<File | null>(null);
        const options = ref<any>({ warehouseId: null, overwriteExisting: false });
        const modalRef = ref<HTMLElement | null>(null);
        const fileInputRef = ref<HTMLInputElement | null>(null);
        const submitting = ref(false);
        const currentJobType = ref<string | null>(null);
        let modalInstance: Modal | null = null;

        const resetFileInput = () => {
            selectedFile.value = null;
            if (fileInputRef.value) fileInputRef.value.value = '';
        };

        const forceCloseModal = () => {
            if (modalInstance) {
                modalInstance.hide();
            } else {
                const modalElement = document.getElementById('importModal');
                if (modalElement) {
                    const instance = (Modal as any).getInstance(modalElement);
                    if (instance) instance.hide();
                    else {
                        modalElement.classList.remove('show');
                        document.body.classList.remove('modal-open');
                        document.querySelector('.modal-backdrop')?.remove();
                    }
                }
            }
            resetFileInput();
        };

        const onModalHidden = () => resetFileInput();

        onMounted(() => {
            if (modalRef.value) {
                modalInstance = new Modal(modalRef.value);
                modalRef.value.addEventListener('hidden.bs.modal', onModalHidden);
            }
        });

        onUnmounted(() => {
            if (modalRef.value) {
                modalRef.value.removeEventListener('hidden.bs.modal', onModalHidden);
            }
            if (modalInstance) {
                modalInstance.hide();
                modalInstance = null;
            }
        });

        const onFileSelected = (event: Event) => {
            const target = event.target as HTMLInputElement;
            if (target.files?.length) selectedFile.value = target.files[0];
        };

        const submitImport = async () => {
            if (!selectedFile.value) return;
            submitting.value = true;
            try {
                const jobId = await store.startImport({
                    module: props.module,
                    file: selectedFile.value,
                    options: options.value,
                });
                currentJobType.value = 'IMPORT';
                emit('imported', jobId);
            } catch (error) {
                console.error('Import start failed:', error);
                alert('Failed to start import. Please try again.');
                forceCloseModal();
            } finally {
                submitting.value = false;
            }
        };

        watch(
            () => store.currentJob,
            (newJob) => {
                if (
                    newJob &&
                    currentJobType.value === 'IMPORT' &&
                    (newJob.status === 'COMPLETED' || newJob.status === 'FAILED')
                ) {
                    forceCloseModal();

                    let message = '';
                    if (newJob.status === 'COMPLETED') {
                        message = `Import completed.\n✅ Added: ${newJob.successRecords || 0}\n⚠️ Skipped: ${newJob.errorRecords || 0}`;
                    } else {
                        message = `Import failed.\n❌ Error: ${newJob.errorSummary || 'Unknown error'}`;
                    }
                    alert(message);

                    emit('refresh');
                    store.clearCurrentJob();
                    currentJobType.value = null;
                }
            }
        );

        return {
            store,
            selectedFile,
            options,
            submitting,
            fileInputRef,
            onFileSelected,
            submitImport,
        };
    },
});
</script>

<style scoped>
/* ===== MODAL OVERRIDES ===== */
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

/* Custom file input */
.file-upload-wrapper {
    position: relative;
    display: flex;
    align-items: center;
    width: 100%;
}

.file-input {
    position: absolute;
    opacity: 0;
    width: 100%;
    height: 100%;
    cursor: pointer;
    z-index: 2;
}

.file-upload-label {
    width: 100%;
    padding: 12px 18px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 16px;
    font-size: 14px;
    color: #64748b;
    transition: all 0.2s;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.file-upload-label::after {
    content: "📁";
    font-size: 18px;
    opacity: 0.7;
}

.file-upload-label:hover {
    border-color: #6366f1;
    background: #f1f5f9;
}

.selected-file {
    color: #0f172a;
    font-weight: 500;
    max-width: 80%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

/* Modern select */
.modern-select {
    width: 100%;
    padding: 12px 18px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 16px;
    font-size: 14px;
    color: #0f172a;
    transition: all 0.2s;
    appearance: none;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='%236366f1' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'%3E%3C/polyline%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-position: right 18px center;
    cursor: pointer;
}

.modern-select:focus {
    outline: none;
    border-color: #6366f1;
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
    background-color: white;
}

/* Checkbox */
.modern-checkbox {
    width: 18px;
    height: 18px;
    border-radius: 5px;
    border: 2px solid #cbd5e1;
    transition: all 0.2s;
    cursor: pointer;
    margin-top: 0;
    margin-right: 8px;
}

.modern-checkbox:checked {
    background-color: #4f46e5;
    border-color: #4f46e5;
}

.form-check-label {
    color: #334155;
    font-weight: 500;
    cursor: pointer;
}

/* ===== BUTTONS ===== */
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