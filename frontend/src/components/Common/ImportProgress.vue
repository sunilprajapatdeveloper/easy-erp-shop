<template>
    <div v-if="job" class="import-progress mt-3">
        <div class="progress mb-2" style="height: 8px">
            <div class="progress-bar" role="progressbar" :style="{ width: progressPercent + '%' }" :class="{
                'bg-success': job.status === 'COMPLETED',
                'bg-danger': job.status === 'FAILED',
            }" :aria-valuenow="progressPercent" aria-valuemin="0" aria-valuemax="100"></div>
        </div>
        <div class="d-flex justify-content-between small text-muted">
            <span>Status: {{ job.status }}</span>
            <span v-if="job.processedRecords !== null">
                {{ job.processedRecords }} / {{ job.totalRecords || '?' }}
            </span>
            <span>Success: {{ job.successRecords || 0 }}</span>
            <span>Errors: {{ job.errorRecords || 0 }}</span>
        </div>
        <div v-if="job.status === 'COMPLETED' && job.resultUrl" class="mt-2">
            <a :href="job.resultUrl" class="btn btn-sm btn-success" target="_blank">
                Download Result
            </a>
        </div>
        <div v-if="job.status === 'FAILED' && job.errorUrl" class="mt-2">
            <a :href="job.errorUrl" class="btn btn-sm btn-danger" target="_blank">
                Download Error Report
            </a>
        </div>
        <div v-if="job.status === 'COMPLETED' && job.errorRecords && job.errorRecords > 0" class="mt-2">
            <button class="btn btn-sm btn-outline-secondary" @click="showErrors = !showErrors">
                {{ showErrors ? 'Hide' : 'Show' }} Errors
            </button>
            <div v-if="showErrors" class="mt-2">
                <div v-for="err in errors" :key="err.id" class="alert alert-warning alert-sm py-1 mb-1">
                    <strong>Row {{ err.rowNumber }}:</strong> {{ err.errorMessage }}
                    <span v-if="err.rawData" class="text-muted d-block small">{{ err.rawData }}</span>
                </div>
                <div v-if="errorTotal > errors.length" class="text-center mt-2">
                    <button class="btn btn-sm btn-link" @click="loadMoreErrors">Load more</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch, computed } from 'vue';
import { useImportExportStore } from '@/stores/importExportStore';
import { importExportService } from '@/services/importExportService';
import type { JobStatusResponse } from '@/types/importExport';

export default defineComponent({
    name: 'ImportProgress',
    props: {
        job: {
            type: Object as () => JobStatusResponse,
            required: true,
        },
    },
    setup(props) {
        const store = useImportExportStore();
        const showErrors = ref(false);
        const errors = ref<any[]>([]);
        const errorPage = ref(0);

        const progressPercent = computed(() => {
            if (!props.job.totalRecords) return 0;
            return (props.job.processedRecords! / props.job.totalRecords) * 100;
        });

        const loadErrors = async () => {
            const result = await importExportService.getJobErrors(props.job.id, errorPage.value);
            errors.value.push(...result.content);
            errorPage.value++;
        };

        const loadMoreErrors = () => loadErrors();

        watch(
            () => props.job,
            async (newJob) => {
                if (newJob && newJob.status === 'COMPLETED' && newJob.errorRecords && newJob.errorRecords > 0) {
                    await loadErrors();
                }
            },
            { immediate: true }
        );

        return {
            showErrors,
            errors,
            errorTotal: computed(() => store.errorTotal),
            progressPercent,
            loadMoreErrors,
        };
    },
});
</script>