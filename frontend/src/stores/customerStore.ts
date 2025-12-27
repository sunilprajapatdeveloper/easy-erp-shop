import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type { Customer, CreateCustomerRequest } from "@/types/Customer";
import * as customerService from "@/services/customerService";

export const useCustomerStore = defineStore("customer", () => {
  const customers = ref<Customer[]>([]);
  const loading = ref(false);

  // For dropdowns or quick lookup
  const customerMap = computed<Record<number, string>>(() => {
    return customers.value.reduce((map, customer) => {
      map[customer.id] = customer.name;
      return map;
    }, {} as Record<number, string>);
  });

  const fetchCustomers = async (): Promise<Customer[]> => {
    loading.value = true;
    try {
      const res = await customerService.getCustomers();
      customers.value = res.data;
      return res.data;
    } finally {
      loading.value = false;
    }
  };

  const addCustomer = async (data: CreateCustomerRequest) => {
    const res = await customerService.createCustomer(data);
    customers.value.push(res.data);
  };

  const updateCustomer = async (id: number, data: CreateCustomerRequest) => {
    const res = await customerService.updateCustomer(id, data);
    const index = customers.value.findIndex((c) => c.id === id);
    if (index !== -1) {
      customers.value[index] = res.data;
    }
  };

  const removeCustomer = async (id: number) => {
    await customerService.deleteCustomer(id);
    customers.value = customers.value.filter((c) => c.id !== id);
  };

  return {
    customers,
    customerMap,
    loading,
    fetchCustomers,
    addCustomer,
    updateCustomer,
    removeCustomer,
  };
});
