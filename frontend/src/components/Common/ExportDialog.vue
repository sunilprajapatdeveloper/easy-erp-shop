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
                        <img src="../../assets/img/icons/close-circle-2.svg" alt="Image" />
                    </button>
                </div>
                <div class="modal-body">
                    <form @submit.prevent="submitExport">
                        <div class="row">
                            <div class="col-12">
                                <div class="form-group mb-15">
                                    <label class="d-block fs-14 text-black mb-10">Format</label>
                                    <select v-model="format" class="bg_ash border-0 rounded-1 fs-14 text-optional">
                                        <option value="EXCEL">Excel (XLSX)</option>
                                        <option value="CSV">CSV</option>
                                        <option value="PDF">PDF</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="form-group mb-15">
                                    <label class="d-block fs-14 text-black mb-10">
                                        Filters (optional)
                                    </label>
                                    <input type="text"
                                        class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                                        placeholder="e.g., status=ACTIVE" v-model="filters.status" />
                                </div>
                            </div>

                            <div class="col-lg-6 mb-md-15">
                                <button type="button" class="btn style-four w-100 d-block" data-bs-dismiss="modal">
                                    Cancel
                                </button>
                            </div>
                            <div class="col-lg-6">
                                <button type="submit" class="btn style-five w-100 d-block" :disabled="submitting">
                                    <span v-if="submitting" class="spinner-border spinner-border-sm me-1"></span>
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