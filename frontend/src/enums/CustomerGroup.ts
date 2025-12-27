export enum CustomerGroup {
  RETAIL = "RETAIL",
  WHOLESALE = "WHOLESALE",
  VIP = "VIP",
  EMPLOYEE = "EMPLOYEE",
  DISTRIBUTOR = "DISTRIBUTOR",
  GOVERNMENT = "GOVERNMENT",
  NON_PROFIT = "NON_PROFIT",
}

export const CustomerGroupLabels: Record<CustomerGroup, string> = {
  [CustomerGroup.RETAIL]: "Retail",
  [CustomerGroup.WHOLESALE]: "Wholesale",
  [CustomerGroup.VIP]: "VIP",
  [CustomerGroup.EMPLOYEE]: "Employee",
  [CustomerGroup.DISTRIBUTOR]: "Distributor",
  [CustomerGroup.GOVERNMENT]: "Government",
  [CustomerGroup.NON_PROFIT]: "Non-Profit",
};
