import { defineStore } from "pinia";
import { importExportService } from "@/services/importExportService";
import type {
  JobStatusResponse,
  ImportError,
  StartImportRequest,
  StartExportRequest,
} from "@/types/importExport";

export const useImportExportStore = defineStore("importExport", {
  state: () => ({
    currentJob: null as JobStatusResponse | null,
    jobErrors: [] as ImportError[],
    errorTotal: 0,
    loading: false,
    pollingInterval: null as number | null,
  }),
  actions: {
    async startImport(request: StartImportRequest): Promise<number> {
      this.loading = true;
      try {
        const job = await importExportService.startImport(request);
        this.startPolling(job.id);
        return job.id;
      } finally {
        this.loading = false;
      }
    },

    async startExport(request: StartExportRequest): Promise<number> {
      this.loading = true;
      try {
        const job = await importExportService.startExport(request);
        this.startPolling(job.id);
        return job.id;
      } finally {
        this.loading = false;
      }
    },

    async fetchJobStatus(jobId: number): Promise<JobStatusResponse> {
      const status = await importExportService.getJobStatus(jobId);
      this.currentJob = status;
      return status;
    },

    async fetchJobErrors(jobId: number, page = 0, size = 20): Promise<void> {
      const result = await importExportService.getJobErrors(jobId, page, size);
      this.jobErrors = result.content;
      this.errorTotal = result.totalElements;
    },

    startPolling(jobId: number) {
      this.stopPolling();
      this.pollingInterval = window.setInterval(async () => {
        try {
          const status = await this.fetchJobStatus(jobId);
          if (
            status.status === "COMPLETED" ||
            status.status === "FAILED" ||
            status.status === "CANCELLED"
          ) {
            this.stopPolling();
          }
        } catch (error) {
          console.error("Polling error", error);
          this.stopPolling();
        }
      }, 2000);
    },

    stopPolling() {
      if (this.pollingInterval) {
        clearInterval(this.pollingInterval);
        this.pollingInterval = null;
      }
    },

    async cancelJob(jobId: number) {
      await importExportService.cancelJob(jobId);
      if (this.currentJob?.id === jobId) {
        this.currentJob = null;
        this.stopPolling();
      }
    },

    clearCurrentJob() {
      this.currentJob = null;
      this.jobErrors = [];
      this.errorTotal = 0;
      this.stopPolling();
    },
  },
});
