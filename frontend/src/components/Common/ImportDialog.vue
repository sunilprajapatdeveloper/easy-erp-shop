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
                        <img src="../../assets/img/icons/close-circle-2.svg" alt="Image" />
                    </button>
                </div>
                <div class="modal-body">
                    <form @submit.prevent="submitImport">
                        <div class="row">
                            <div class="col-12">
                                <div class="form-group mb-15">
                                    <label class="d-block fs-14 text-black mb-10">
                                        File (Excel or CSV)
                                    </label>
                                    <input type="file"
                                        class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                                        accept=".xlsx,.xls,.csv" @change="onFileSelected" required ref="fileInputRef" />
                                </div>
                            </div>

                            <!-- Optional warehouse selection -->
                            <div class="col-12" v-if="showOptions">
                                <div class="form-group mb-15">
                                    <label class="d-block fs-14 text-black mb-10">Warehouse</label>
                                    <select v-model="options.warehouseId"
                                        class="bg_ash border-0 rounded-1 fs-14 text-optional"
                                        v-if="warehouses && warehouses.length">
                                        <option :value="null">-- Select Warehouse --</option>
                                        <option v-for="w in warehouses" :key="w.id" :value="w.id">
                                            {{ w.name }}
                                        </option>
                                    </select>
                                </div>
                                <div class="form-check mb-15">
                                    <input type="checkbox" class="form-check-input" id="overwriteExisting"
                                        v-model="options.overwriteExisting" />
                                    <label class="form-check-label" for="overwriteExisting">
                                        Overwrite existing
                                    </label>
                                </div>
                            </div>

                            <div class="col-lg-6 mb-md-15">
                                <button type="button" class="btn style-four w-100 d-block" data-bs-dismiss="modal">
                                    Cancel
                                </button>
                            </div>
                            <div class="col-lg-6">
                                <button type="submit" class="btn style-five w-100 d-block"
                                    :disabled="!selectedFile || submitting">
                                    <span v-if="submitting" class="spinner-border spinner-border-sm me-1"></span>
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