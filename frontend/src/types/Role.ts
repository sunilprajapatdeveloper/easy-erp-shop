export interface Role {
  id: number;
  name: string;
  description?: string;
  permissionIds: number[];
}
