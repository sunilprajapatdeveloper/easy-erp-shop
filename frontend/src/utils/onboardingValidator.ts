export const validateEmail = (email: string): boolean => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
};

export const validatePassword = (
  password: string
): { isValid: boolean; errors: string[] } => {
  const errors: string[] = [];

  if (password.length < 8) {
    errors.push("Password must be at least 8 characters long");
  }

  if (!/[A-Z]/.test(password)) {
    errors.push("Password must contain at least one uppercase letter");
  }

  if (!/[0-9]/.test(password)) {
    errors.push("Password must contain at least one number");
  }

  if (!/[^A-Za-z0-9]/.test(password)) {
    errors.push("Password must contain at least one special character");
  }

  return {
    isValid: errors.length === 0,
    errors,
  };
};

export const validatePhone = (phone: string): boolean => {
  // Basic phone validation - can be enhanced based on country
  const re = /^[\+]?[1-9][\d]{0,15}$/;
  return re.test(phone.replace(/[\s\-\(\)]/g, ""));
};

export const validateSubdomain = (
  subdomain: string
): { isValid: boolean; error?: string } => {
  if (!subdomain) {
    return { isValid: false, error: "Subdomain is required" };
  }

  if (subdomain.length < 3 || subdomain.length > 63) {
    return {
      isValid: false,
      error: "Subdomain must be between 3 and 63 characters",
    };
  }

  const re = /^[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?$/;
  if (!re.test(subdomain)) {
    return {
      isValid: false,
      error:
        "Subdomain can only contain lowercase letters, numbers, and hyphens. Cannot start or end with a hyphen.",
    };
  }

  // Check for reserved words
  const reserved = [
    "admin",
    "api",
    "app",
    "dashboard",
    "support",
    "blog",
    "www",
  ];
  if (reserved.includes(subdomain)) {
    return { isValid: false, error: "This subdomain is reserved" };
  }

  return { isValid: true };
};

export const validateCompanyData = (
  data: any
): { isValid: boolean; errors: Record<string, string> } => {
  const errors: Record<string, string> = {};

  if (!data.legalName?.trim()) {
    errors.legalName = "Legal business name is required";
  }

  if (!data.industry) {
    errors.industry = "Industry is required";
  }

  if (!data.email || !validateEmail(data.email)) {
    errors.email = "Valid business email is required";
  }

  if (!data.primaryCurrency) {
    errors.primaryCurrency = "Primary currency is required";
  }

  return {
    isValid: Object.keys(errors).length === 0,
    errors,
  };
};
