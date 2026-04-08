import api from "./api";
import type {
  ImportExportJob,
  JobStatusResponse,
  ImportError,
  StartImportRequest,
  StartExportRequest,
  JobType,
} from "@/types/importExport";

export const importExportService = {
  // Start an import job
  async startImport(data: StartImportRequest): Promise<ImportExportJob> {
    const formData = new FormData();
    formData.append("module", data.module);
    formData.append("file", data.file);
    if (data.options) {
      formData.append("options", JSON.stringify(data.options));
    }
    const response = await api.post<ImportExportJob>(
      "/import-export/import",
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      },
    );
    return response.data;
  },

  // Start an export job
  async startExport(data: StartExportRequest): Promise<ImportExportJob> {
    const response = await api.post<ImportExportJob>(
      "/import-export/export",
      data,
    );
    return response.data;
  },

  // Get job status
  async getJobStatus(jobId: number): Promise<JobStatusResponse> {
    const response = await api.get<JobStatusResponse>(
      `/import-export/jobs/${jobId}`,
    );
    return response.data;
  },

  // Get errors for a job (paginated)
  async getJobErrors(
    jobId: number,
    page = 0,
    size = 20,
  ): Promise<{ content: ImportError[]; totalElements: number }> {
    const response = await api.get(`/import-export/jobs/${jobId}/errors`, {
      params: { page, size },
    });
    return response.data;
  },

  // Get job history (optional)
  async getJobHistory(
    module?: string,
    type?: JobType,
    page = 0,
    size = 20,
  ): Promise<{ content: ImportExportJob[]; totalElements: number }> {
    const params: any = { page, size };
    if (module) params.module = module;
    if (type) params.type = type;
    const response = await api.get("/import-export/history", { params });
    return response.data;
  },

  // Cancel a job
  async cancelJob(jobId: number): Promise<void> {
    await api.delete(`/import-export/jobs/${jobId}`);
  },
};
