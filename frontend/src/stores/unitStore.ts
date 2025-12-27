import { defineStore } from "pinia";
import { ref } from "vue";
import type { Unit, CreateUnitRequest } from "@/types/Unit";
import * as unitService from "@/services/unitService";

export const useUnitStore = defineStore("unitStore", () => {
  const units = ref<Unit[]>([]);

  const fetchUnits = async () => {
    const res = await unitService.getUnits();
    units.value = res.data;
  };

  const addUnit = async (data: CreateUnitRequest) => {
    const res = await unitService.createUnit(data);
    units.value.push(res.data);
  };

  const updateUnit = async (id: number, data: CreateUnitRequest) => {
    const res = await unitService.updateUnit(id, data);
    const index = units.value.findIndex((u) => u.id === id);
    if (index !== -1) units.value[index] = res.data;
  };

  const removeUnit = async (id: number) => {
    await unitService.deleteUnit(id);
    units.value = units.value.filter((u) => u.id !== id);
  };

  return { units, fetchUnits, addUnit, updateUnit, removeUnit };
});
