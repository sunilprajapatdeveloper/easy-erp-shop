import { defineStore } from "pinia";
import type {
  SubscriptionPlan,
  CreateSubscriptionPlanRequest,
  UpdateSubscriptionPlanRequest,
} from "@/types/SubscriptionPlan";
import { subscriptionPlanService } from "@/services/subscriptionPlanService";

interface SubscriptionPlanState {
  plans: SubscriptionPlan[];
  loading: boolean;
}

export const useSubscriptionPlanStore = defineStore("subscriptionPlan", {
  state: (): SubscriptionPlanState => ({
    plans: [],
    loading: false,
  }),

  actions: {
    async fetchPlans() {
      this.loading = true;
      try {
        this.plans = await subscriptionPlanService.list();
      } finally {
        this.loading = false;
      }
    },

    async getPlan(id: number): Promise<SubscriptionPlan | undefined> {
      this.loading = true;
      try {
        const plan = await subscriptionPlanService.get(id);
        return plan;
      } finally {
        this.loading = false;
      }
    },

    async createPlan(data: CreateSubscriptionPlanRequest, userId: number) {
      this.loading = true;
      try {
        const newPlan = await subscriptionPlanService.create(data, userId);
        this.plans.push(newPlan);
      } finally {
        this.loading = false;
      }
    },

    async updatePlan(
      id: number,
      data: UpdateSubscriptionPlanRequest,
      userId: number
    ) {
      this.loading = true;
      try {
        const updatedPlan = await subscriptionPlanService.update(
          id,
          data,
          userId
        );
        const index = this.plans.findIndex((p) => p.id === id);
        if (index !== -1) this.plans[index] = updatedPlan;
      } finally {
        this.loading = false;
      }
    },

    async deletePlan(id: number, userId: number) {
      this.loading = true;
      try {
        await subscriptionPlanService.delete(id, userId);
        this.plans = this.plans.filter((p) => p.id !== id);
      } finally {
        this.loading = false;
      }
    },
  },
});
