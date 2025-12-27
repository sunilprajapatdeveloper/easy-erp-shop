export function getAuthToken(): string | null {
  const token = localStorage.getItem("authToken");
  const expiry = localStorage.getItem("authTokenExpiry");

  if (!token || !expiry) return null;

  const now = Date.now();
  if (now > parseInt(expiry)) {
    // Clean up expired token
    localStorage.removeItem("authToken");
    localStorage.removeItem("authTokenExpiry");
    return null;
  }

  return token;
}
