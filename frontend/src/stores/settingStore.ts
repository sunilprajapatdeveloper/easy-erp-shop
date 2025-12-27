import { defineStore } from "pinia";
import { ref } from "vue";

export const useSettingStore = defineStore("settingStore", () => {
  // Currency
  const currencySymbol = ref("$");
  const currencyCode = ref("USD");
  const currencyName = ref("US Dollar");

  // Date and time formatting
  const dateFormat = ref("en-US"); // Used in toLocaleDateString()
  const timeFormatOptions: Intl.DateTimeFormatOptions = {
    hour: "2-digit",
    minute: "2-digit",
  };

  // Update methods
  const setCurrency = (symbol: string, code: string, name: string) => {
    currencySymbol.value = symbol;
    currencyCode.value = code;
    currencyName.value = name;
  };

  const setDateFormat = (format: string) => {
    dateFormat.value = format;
  };

  return {
    currencySymbol,
    currencyCode,
    currencyName,
    dateFormat,
    timeFormatOptions,
    setCurrency,
    setDateFormat,
  };
});
