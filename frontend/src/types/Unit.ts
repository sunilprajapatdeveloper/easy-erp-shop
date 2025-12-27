export interface Unit {
  id: number;
  name: string;
  shortName: string;
  baseUnit: string;
  operator: string;
  operatorValue: number;
}

export type CreateUnitRequest = Omit<Unit, 'id'>;