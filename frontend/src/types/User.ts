import { MediaResponse } from "./Media";

export interface User {
  id: number;
  email: string;
  phone: string;
  firstname: string | null;
  lastname: string | null;
  status: boolean;
  profileId: string | null;
  profile: string | null;
  roleId: number | null;
  roleName: string | null;
  rolePermissions: string[];
  createdBy: number | null;
  createdAt: string;
  updatedBy: number | null;
  updatedAt: string;
  companyId: number;
  warehouseIds: number[];
  defaultWarehouseId: number | null;
}

export interface UserListItem {
  id: number;
  email: string;
  firstname: string | null;
  lastname: string | null;
  phone: string;
  roleName: string | null;
  status: boolean;
}

export interface CreateUserRequest {
  firstname: string;
  lastname: string;
  email: string;
  phone: string;
  mfaEnabled?: boolean;
  profileImageUrl?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  country?: string;
  postalCode?: string;
  timezone?: string;
  language?: string;
  gender?: string;
  roleId: number;
  department?: string;
  positionTitle?: string;
  warehouseIds?: number[];
  defaultWarehouseId?: number;
}

export interface UpdateUserRequest {
  firstname?: string;
  lastname?: string;
  email?: string;
  phone?: string;
  mfaEnabled?: boolean;
  profileImageUrl?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  country?: string;
  postalCode?: string;
  timezone?: string;
  language?: string;
  gender?: string;
  roleId?: number;
  department?: string;
  positionTitle?: string;
  warehouseIds?: number[];
  defaultWarehouseId?: number;
}

export interface UserRegisterRequest {
  firstname: string;
  lastname: string;
  email: string;
  phone: string;
}

export interface LoginRequest {
  identifier: string;
  password: string;
}

export interface UpdatePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

export interface UserResponse extends User {}

export interface JwtResponse {
  token: string;
  expiresIn: number;
  user: UserResponse;
}

export interface UserProfileWithMedia extends User {
  profileMedia?: MediaResponse;
}
