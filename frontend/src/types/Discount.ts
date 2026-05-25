import { DiscountScope } from "@/enums/DiscountScope";
import { DiscountSource } from "@/enums/DiscountSource";
import { DiscountType } from "@/enums/discountType";

export interface DiscountResponse {
  id: number;
  name: string;
  code?: string;
  description?: string;
  discountType: DiscountType;
  scope: DiscountScope;
  source: DiscountSource;
  discountValue: number;
  maxDiscountAmount?: number;
  minOrderAmount?: number;
  maxOrderAmount?: number;
  stackable: boolean;
  autoApply: boolean;
  requiresManagerApproval: boolean;
  approvalRequiredAbove?: number;
  priority?: number;
  usageLimit?: number;
  usageLimitPerCustomer?: number;
  isActive: boolean;
  startDate?: string; // LocalDateTime → ISO string
  endDate?: string;
  warehouseId?: number;
  companyId: number;
  createdBy: number;
  createdAt: string;
  updatedBy?: number;
  updatedAt?: string;
  productIds: number[];
  categoryIds: number[];
}

export interface CreateDiscountRequest {
  name: string;
  code?: string;
  description?: string;
  discountType: DiscountType;
  scope: DiscountScope;
  source: DiscountSource;
  discountValue: number;
  maxDiscountAmount?: number;
  minOrderAmount?: number;
  maxOrderAmount?: number;
  stackable?: boolean;
  autoApply?: boolean;
  requiresManagerApproval?: boolean;
  approvalRequiredAbove?: number;
  priority?: number;
  usageLimit?: number;
  usageLimitPerCustomer?: number;
  isActive?: boolean;
  startDate?: string;
  endDate?: string;
  warehouseId?: number;
  productIds?: number[];
  categoryIds?: number[];
}

export type UpdateDiscountRequest = Partial<CreateDiscountRequest>;

/** Simplified model for dropdowns / sale form */
export interface DiscountItem {
  id: number;
  name: string;
  code: string;
  discountType: DiscountType;
  discountValue: number;
}
