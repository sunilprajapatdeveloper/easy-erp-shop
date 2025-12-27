export enum PlanStatus {
  ACTIVE = "ACTIVE",
  INACTIVE = "INACTIVE",
  DEPRECATED = "DEPRECATED",
}

export const PlanStatusLabels: Record<PlanStatus, string> = {
  [PlanStatus.ACTIVE]: "Active",
  [PlanStatus.INACTIVE]: "Inactive",
  [PlanStatus.DEPRECATED]: "Deprecated",
};
