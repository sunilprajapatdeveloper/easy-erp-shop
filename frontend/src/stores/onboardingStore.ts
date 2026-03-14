import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type {
  UserData,
  CompanyData,
  PlanData,
  WarehouseData,
  ConfigData,
  PaymentInfo,
} from "@/types/onboarding";

interface EmailVerificationData {
  email: string;
  verified: boolean;
  verificationCode?: string;
}

export const useOnboardingStore = defineStore("onboarding", () => {
  // State
  const currentStep = ref(1);
  const userId = ref<number | null>(null);
  const companyId = ref<number | null>(null);
  const userData = ref<UserData | null>(null);
  const companyData = ref<CompanyData | null>(null);
  const planData = ref<PlanData | null>(null);
  const paymentInfo = ref<PaymentInfo | null>(null);
  const warehouseData = ref<WarehouseData | null>(null);
  const configData = ref<ConfigData | null>(null);
  const completedSteps = ref<number[]>([]);
  const emailData = ref<EmailVerificationData>({
    email: "",
    verified: false,
  });

  // Getters
  const getEmail = computed(() => emailData.value.email);
  const isEmailVerified = computed(() => emailData.value.verified);
  const isPaymentRequired = computed(() => {
    return planData.value ? !planData.value.isTrial : false;
  });

  // Actions
  const setUserId = (id: number) => (userId.value = id);
  const setCompanyId = (id: number) => (companyId.value = id);

  const setUserData = (data: UserData) => {
    userData.value = data;
    markStepComplete(3);
  };

  const setCompanyData = (data: CompanyData) => {
    companyData.value = data;
    markStepComplete(2);
  };

  const setPlanData = (data: PlanData) => {
    planData.value = data;
    if (data.isTrial) paymentInfo.value = null;
    markStepComplete(4);
  };

  const setPaymentInfo = (data: PaymentInfo) => {
    paymentInfo.value = data;
    if (isPaymentRequired.value) markStepComplete(5);
  };

  const setWarehouseData = (data: WarehouseData) => {
    warehouseData.value = data;
    const step = isPaymentRequired.value ? 6 : 5;
    markStepComplete(step);
  };

  const setConfigData = (data: ConfigData) => {
    configData.value = data;
    // Not used in new flow; keep but don't mark step
  };

  const setCurrentStep = (step: number) => {
    currentStep.value = step;
  };

  const markStepComplete = (step: number) => {
    if (!completedSteps.value.includes(step)) {
      completedSteps.value.push(step);
    }
  };

  const setDefaultWarehouse = () => {
    warehouseData.value = {
      name: "Main Warehouse",
      code: "WH-001",
      type: "retail",
      address: companyData.value?.address || {
        street: "",
        city: "",
        state: "",
        postalCode: "",
        country: "",
      },
      managerName: userData.value?.name || "",
      managerEmail: userData.value?.email || "",
      phone: companyData.value?.phone || "",
      currency: companyData.value?.primaryCurrency || "USD",
      isDefault: true,
      settings: {
        enableInventoryTracking: true,
        enableBarcode: true,
        lowStockAlert: 10,
        requireApproval: false,
        enablePos: true,
        enableReceiving: true,
      },
    };
    const step = isPaymentRequired.value ? 6 : 5;
    markStepComplete(step);
  };

  const setDefaultPlan = () => {
    planData.value = {
      subscriptionPlanId: 1,
      billingCycle: "monthly",
      price: 0,
      isTrial: true,
      trialEndDate: new Date(
        Date.now() + 14 * 24 * 60 * 60 * 1000,
      ).toISOString(),
    };
    paymentInfo.value = null;
    markStepComplete(4);
  };

  const setDefaultConfiguration = () => {
    configData.value = {
      posSettings: {
        receiptHeader: "",
        receiptFooter: "Thank you for your business!",
        defaultTaxRate: 0,
        roundOffAmount: false,
        enableCustomerDisplay: true,
        enableCashDrawer: true,
        printReceipt: true,
        emailReceipt: false,
      },
      inventorySettings: {
        enableBatchTracking: false,
        enableExpiryTracking: false,
        defaultReorderPoint: 10,
        autoUpdateStock: true,
      },
      salesSettings: {
        defaultPaymentMethod: "cash",
        defaultCustomer: "walkin",
        enableDiscount: true,
        enableServiceCharges: false,
      },
      purchaseSettings: {
        defaultSupplier: "primary",
        enablePurchaseOrder: true,
        autoCreateGRN: false,
      },
      notificationSettings: {
        emailNotifications: true,
        pushNotifications: true,
        lowStockAlerts: true,
        dailyReports: true,
        weeklyReports: false,
      },
    };
    // Not marking step because configuration step is removed
  };

  const saveProgress = async () => {
    const data = {
      currentStep: currentStep.value,
      userId: userId.value,
      companyId: companyId.value,
      userData: userData.value,
      companyData: companyData.value,
      planData: planData.value,
      paymentInfo: paymentInfo.value,
      warehouseData: warehouseData.value,
      configData: configData.value,
      completedSteps: completedSteps.value,
      emailData: emailData.value,
    };
    localStorage.setItem("onboarding", JSON.stringify(data));
  };

  const loadProgress = () => {
    const saved = localStorage.getItem("onboarding");
    if (saved) {
      const data = JSON.parse(saved);
      currentStep.value = data.currentStep || 1;
      userId.value = data.userId || null;
      companyId.value = data.companyId || null;
      userData.value = data.userData || null;
      companyData.value = data.companyData || null;
      planData.value = data.planData || null;
      paymentInfo.value = data.paymentInfo || null;
      warehouseData.value = data.warehouseData || null;
      configData.value = data.configData || null;
      completedSteps.value = data.completedSteps || [];
      emailData.value = data.emailData || { email: "", verified: false };
    }
  };

  const clearProgress = () => {
    localStorage.removeItem("onboarding");
    // reset all refs
    currentStep.value = 1;
    userId.value = null;
    companyId.value = null;
    userData.value = null;
    companyData.value = null;
    planData.value = null;
    paymentInfo.value = null;
    warehouseData.value = null;
    configData.value = null;
    completedSteps.value = [];
    emailData.value = { email: "", verified: false };
  };

  const completeOnboarding = async () => {
    clearProgress();
  };

  // Email methods
  const setEmail = (email: string) => {
    emailData.value.email = email;
    emailData.value.verified = false;
  };

  const markEmailVerified = () => {
    emailData.value.verified = true;
    markStepComplete(1);
  };

  const setVerificationCode = (code: string) => {
    emailData.value.verificationCode = code;
  };

  return {
    // State
    currentStep,
    userId,
    companyId,
    userData,
    companyData,
    planData,
    paymentInfo,
    warehouseData,
    configData,
    completedSteps,
    emailData,

    // Getters
    getEmail,
    isEmailVerified,
    isPaymentRequired,

    // Actions
    setUserId,
    setCompanyId,
    setUserData,
    setCompanyData,
    setPlanData,
    setPaymentInfo,
    setWarehouseData,
    setConfigData,
    setCurrentStep,
    markStepComplete,
    setDefaultWarehouse,
    setDefaultPlan,
    setDefaultConfiguration,
    saveProgress,
    loadProgress,
    clearProgress,
    completeOnboarding,
    setEmail,
    markEmailVerified,
    setVerificationCode,
  };
});
