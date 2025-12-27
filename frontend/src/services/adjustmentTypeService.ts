import api from "./api";
import type { CreateAdjustmentRequest } from "@/types/Adjustment";

export const createAdjustment = (data: CreateAdjustmentRequest) =>
  api.post("/adjustments", data);
