import { defineStore } from "pinia";
import type { Role } from "@/types/Role";
import {
  getRoles,
  getRoleById,
  createRole,
  updateRole,
  deleteRole,
} from "@/services/roleService";

export const useRoleStore = defineStore("role", {
  state: () => ({
    roles: [] as Role[],
    loading: false,
  }),

  actions: {
    async fetchRoles() {
      this.loading = true;
      try {
        const res = await getRoles();
        this.roles = res.data;
      } catch (err) {
        console.error("Failed to fetch roles:", err);
      } finally {
        this.loading = false;
      }
    },

    async getRoleById(id: number): Promise<Role | null> {
      try {
        const res = await getRoleById(id);
        return res.data;
      } catch (err) {
        console.error(`Failed to fetch role with ID ${id}:`, err);
        return null;
      }
    },

    async addRole(role: Omit<Role, "id">) {
      try {
        const res = await createRole(role);
        this.roles.push(res.data);
      } catch (err) {
        console.error("Failed to add role:", err);
      }
    },

    async updateRole(id: number, role: Omit<Role, "id">) {
      try {
        const res = await updateRole(id, role);
        const index = this.roles.findIndex((r) => r.id === id);
        if (index !== -1) {
          this.roles[index] = res.data;
        }
      } catch (err) {
        console.error("Failed to update role:", err);
      }
    },

    async removeRole(id: number) {
      try {
        await deleteRole(id);
        this.roles = this.roles.filter((r) => r.id !== id);
      } catch (err) {
        console.error("Failed to delete role:", err);
      }
    },
  },
});
