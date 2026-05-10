import { PromotionType } from "@/enums/promotionType";
import { DiscountType } from "@/enums/discountType";
import { PromotionStackingStrategy } from "@/enums/promotionStackingStrategy";
import { CustomerGroup } from "@/enums/CustomerGroup";

export interface PromotionResponse {
  id: number;
  name: string;
  code?: string;
  description?: string;
  type: PromotionType;
  discountType: DiscountType;
  discountValue?: number;
  maxDiscountAmount?: number;
  minOrderAmount?: number;
  maxOrderAmount?: number;
  usageLimit?: number;
  usageLimitPerCustomer?: number;
  startDate: string;
  endDate?: string;
  isActive: boolean;
  stackingPriority?: number;
  stackingStrategy: PromotionStackingStrategy;
  buyQuantity?: number;
  getQuantity?: number;
  getDiscountPercent?: number;
  buyProductId?: number;
  getProductId?: number;
  warehouseId?: number;
  companyId: number;
  createdBy: number;
  createdAt: string;
  updatedBy?: number;
  updatedAt?: string;
  productIds: number[];
  categoryIds: number[];
  customerGroups: CustomerGroup[];
}

export interface CreatePromotionRequest {
  name: string;
  code?: string;
  description?: string;
  type: PromotionType;
  discountType: DiscountType;
  discountValue?: number;
  maxDiscountAmount?: number;
  minOrderAmount?: number;
  maxOrderAmount?: number;
  usageLimit?: number;
  usageLimitPerCustomer?: number;
  startDate: string;
  endDate?: string;
  stackingPriority?: number;
  stackingStrategy: PromotionStackingStrategy;
  buyQuantity?: number;
  getQuantity?: number;
  getDiscountPercent?: number;
  buyProductId?: number;
  getProductId?: number;
  warehouseId?: number;
  productIds: number[];
  categoryIds: number[];
  customerGroups: CustomerGroup[];
}

export type UpdatePromotionRequest = Partial<CreatePromotionRequest>;

export interface CartItemDto {
  productId: number;
  quantity: number;
  unitPrice: number;
}

export interface CouponValidationRequest {
  couponCode: string;
  customerId?: number;
  warehouseId: number;
  companyId: number;
  currencyCode: string;
  items: CartItemDto[];
  shippingCost: number;
}

export interface AppliedProductDiscount {
  productId: number;
  discountAmount: number;
  description: string;
}

export interface CouponValidationResponse {
  valid: boolean;
  message: string;
  discountAmount: number;
  discountType: DiscountType;
  appliedDiscountValue: number;
  appliedPromotionId: number;
  appliedPromotionName: string;
  productDiscounts: AppliedProductDiscount[];
  freeShipping: boolean;
}
