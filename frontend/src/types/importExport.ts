export type JobType = "IMPORT" | "EXPORT";
export type JobStatus =
  | "PENDING"
  | "PROCESSING"
  | "COMPLETED"
  | "FAILED"
  | "CANCELLED";
export type ExportFormat = "EXCEL" | "CSV" | "PDF";

export interface ImportExportJob {
  id: number;
  jobNumber: number;
  module: string; // 'Product', 'Sale', etc.
  type: JobType;
  status: JobStatus;
  sourceMediaId?: string;
  resultMediaId?: string;
  errorMediaId?: string;
  totalRecords: number | null;
  processedRecords: number | null;
  successRecords: number | null;
  errorRecords: number | null;
  startedAt: string | null;
  completedAt: string | null;
  errorSummary: string | null;
  optionsJson: string | null;
  createdAt: string;
}

export interface JobStatusResponse {
  id: number;
  status: JobStatus;
  totalRecords: number | null;
  processedRecords: number | null;
  successRecords: number | null;
  errorRecords: number | null;
  errorSummary: string | null;
  startedAt: string | null;
  completedAt: string | null;
  resultUrl?: string;
  errorUrl?: string;
}

export interface ImportError {
  id: number;
  rowNumber: number;
  columnName?: string;
  errorMessage: string;
  rawData?: string;
}

export interface StartImportRequest {
  module: string;
  file: File;
  options?: Record<string, any>;
}

export interface StartExportRequest {
  module: string;
  format: ExportFormat;
  filters?: Record<string, any>;
}
