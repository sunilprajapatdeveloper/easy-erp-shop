import { TaxApplicationOrder } from "@/enums/TaxApplicationOrder";
import { TaxCategory} from "@/enums/TaxCategory";
import { TaxInclusionType } from "@/enums/TaxInclusionType";

export interface TaxSetting {
  id?: number;
  taxCategory: TaxCategory;
  name: string;
  rate: number;
  applicationOrder: TaxApplicationOrder;
  inclusionType: TaxInclusionType;
  active: boolean;
  regionCode?: string;
  description?: string;
  warehouseId?: number;
}

export interface CreateTaxSettingRequest {
  taxCategory: TaxCategory;
  name: string;
  rate: number;
  applicationOrder: TaxApplicationOrder;
  inclusionType: TaxInclusionType;
  active?: boolean;
  regionCode?: string;
  description?: string;
  warehouseId?: number;
}

export interface UpdateTaxSettingRequest {
  taxCategory?: TaxCategory;
  name?: string;
  rate?: number;
  applicationOrder?: TaxApplicationOrder;
  inclusionType?: TaxInclusionType;
  active?: boolean;
  regionCode?: string;
  description?: string;
  warehouseId?: number;
}
