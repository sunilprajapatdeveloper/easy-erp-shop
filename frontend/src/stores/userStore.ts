import { defineStore } from "pinia";
import { userService } from "@/services/userService";
import type {
  User,
  UserListItem,
  CreateUserRequest,
  UpdateUserRequest,
  UserRegisterRequest,
  LoginRequest,
  JwtResponse,
  UpdatePasswordRequest,
  UserProfileWithMedia,
} from "@/types/User";
import { useMediaStore } from "./mediaStore";

export const useUserStore = defineStore("user", {
  state: () => ({
    users: [] as UserListItem[],
    currentUser: null as User | null,
    token: localStorage.getItem("authToken") || "",
    loading: false,
    error: null as string | null,
  }),

  persist: true,

  getters: {
    userPermissions: (state): string[] =>
      state.currentUser?.rolePermissions || [],

    defaultWarehouseId: (state): number | null =>
      state.currentUser?.defaultWarehouseId ?? null,
  },

  actions: {
    async fetchUsers() {
      this.loading = true;
      this.error = null;
      try {
        this.users = await userService.getAllUsers();
      } catch (err: any) {
        this.error = err?.message || "Failed to fetch users";
      } finally {
        this.loading = false;
      }
    },

    async fetchUserById(id: number): Promise<User | null> {
      this.loading = true;
      this.error = null;
      try {
        return await userService.getUserById(id);
      } catch (err: any) {
        this.error = err?.message || "Failed to fetch user";
        return null;
      } finally {
        this.loading = false;
      }
    },

    async addUser(payload: CreateUserRequest) {
      this.loading = true;
      this.error = null;
      try {
        const created = await userService.createUser(payload);
        this.users.push({
          id: created.id,
          email: created.email,
          firstname: created.firstname,
          lastname: created.lastname,
          phone: created.phone,
          roleName: created.roleName,
          status: created.status,
        });
        return created;
      } catch (err: any) {
        this.error = err?.message || "Failed to create user";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updateUser(id: number, payload: UpdateUserRequest) {
      this.loading = true;
      this.error = null;
      try {
        const updated = await userService.updateUser(id, payload);
        const idx = this.users.findIndex((u) => u.id === id);
        if (idx !== -1) this.users[idx] = updated;
        if (this.currentUser?.id === id) this.currentUser = updated;
        return updated;
      } catch (err: any) {
        this.error = err?.message || "Failed to update user";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async removeUser(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await userService.deleteUser(id);
        this.users = this.users.filter((u) => u.id !== id);
        if (this.currentUser?.id === id) this.currentUser = null;
      } catch (err: any) {
        this.error = err?.message || "Failed to delete user";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async register(payload: UserRegisterRequest, companyId: number) {
      this.loading = true;
      this.error = null;
      try {
        const jwt: JwtResponse = await userService.register(payload, companyId);
        this.token = jwt.token;
        this.currentUser = jwt.user;
        const expiry = Date.now() + jwt.expiresIn * 1000;
        localStorage.setItem("authToken", jwt.token);
        localStorage.setItem("authTokenExpiry", expiry.toString());
        return jwt.user;
      } catch (err: any) {
        this.error = err?.message || "Registration failed";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async login(payload: LoginRequest) {
      this.loading = true;
      this.error = null;
      try {
        const jwt: JwtResponse = await userService.login(payload);
        this.token = jwt.token;
        this.currentUser = jwt.user;
        const expiry = Date.now() + jwt.expiresIn * 1000;
        localStorage.setItem("authToken", jwt.token);
        localStorage.setItem("authTokenExpiry", expiry.toString());
        return jwt;
      } catch (err: any) {
        this.error = err?.message || "Login failed";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updatePassword(payload: UpdatePasswordRequest) {
      this.loading = true;
      this.error = null;
      try {
        await userService.updatePassword(payload);
      } catch (err: any) {
        this.error = err?.message || "Password update failed";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updateProfileWithImage(
      userId: number,
      userData: UpdateUserRequest,
      imageFile?: File,
    ): Promise<User> {
      try {
        let profileImageUrl = userData.profileImageUrl;

        // If there's a new image file, upload it
        if (imageFile) {
          const mediaStore = useMediaStore();

          const mediaResponse = await mediaStore.uploadSingleFile(imageFile, {
            entityType: "USER",
            entityId: userId,
            isPrimary: true,
          });

          profileImageUrl = mediaResponse.url;
        }

        // Update user with new image info
        const updateData: UpdateUserRequest = {
          ...userData,
          profileImageUrl,
        };

        const user = await this.updateUser(userId, updateData);
        return user;
      } catch (error) {
        console.error("Failed to update profile with image:", error);
        throw error;
      }
    },

    async fetchUserWithMedia(
      userId: number,
    ): Promise<UserProfileWithMedia | null> {
      try {
        const user = await this.fetchUserById(userId);
        if (!user) return null;

        // Fetch media if profileId exists
        if (user.profileId) {
          const mediaStore = useMediaStore();
          const media = await mediaStore.fetchMediaById(user.profileId);

          return {
            ...user,
            profileMedia: media ?? undefined,
          };
        }

        return user;
      } catch (error) {
        console.error("Failed to fetch user with media:", error);
        return null;
      }
    },

    logout() {
      this.token = "";
      this.currentUser = null;
      localStorage.removeItem("authToken");
      localStorage.removeItem("authTokenExpiry");
    },
  },
});
