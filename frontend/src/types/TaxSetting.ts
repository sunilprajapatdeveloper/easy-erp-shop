import { TaxType } from "@/enums/TaxType";
import { TaxCalculationType } from "@/enums/TaxCalculationType";
import { TaxInclusiveType } from "@/enums/TaxInclusiveType";

export interface TaxSetting {
  id?: number;
  taxType: TaxType;
  name: string;
  rate: number;
  calculationType: TaxCalculationType;
  inclusiveType: TaxInclusiveType;
  active: boolean;
  regionCode?: string;
  description?: string;
  warehouseId?: number;
}

export interface CreateTaxSettingRequest {
  taxType: TaxType;
  name: string;
  rate: number;
  calculationType: TaxCalculationType;
  inclusiveType: TaxInclusiveType;
  active?: boolean;
  regionCode?: string;
  description?: string;
  warehouseId?: number;
}

export interface UpdateTaxSettingRequest {
  taxType?: TaxType;
  name?: string;
  rate?: number;
  calculationType?: TaxCalculationType;
  inclusiveType?: TaxInclusiveType;
  active?: boolean;
  regionCode?: string;
  description?: string;
  warehouseId?: number;
}
