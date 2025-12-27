import { Offcanvas } from "bootstrap";

type PopupType = "success" | "error";

export function showPopup(
  type: PopupType,
  message: string,
  messageRef: { value: string },
  duration = 3000
) {
  const popupId = type === "success" ? "successPopup" : "errorPopup";
  const triggerId =
    type === "success" ? "triggerSuccessPopup" : "triggerErrorPopup";

  messageRef.value = message;

  const trigger = document.getElementById(triggerId) as HTMLAnchorElement;
  const popup = document.getElementById(popupId);

  if (!trigger || !popup) {
    console.warn(`${type} popup trigger or element not found.`);
    return;
  }

  trigger.click();
  const instance = Offcanvas.getOrCreateInstance(popup);

  // === Full Cleanup Function ===
  const cleanupAndHide = () => {
    instance?.hide();

    // Remove Bootstrap offcanvas side effects
    document.body.classList.remove("offcanvas-backdrop");
    document.body.style.overflow = "";
    document.body.style.paddingRight = "";

    // Remove all event listeners
    document.removeEventListener("click", cleanupAndHide);
    document.removeEventListener("keydown", cleanupAndHide);
    document.removeEventListener("wheel", cleanupAndHide);
    document.removeEventListener("touchmove", cleanupAndHide);
  };

  // Add close event listeners
  setTimeout(() => {
    document.addEventListener("click", cleanupAndHide);
    document.addEventListener("keydown", cleanupAndHide);
    document.addEventListener("wheel", cleanupAndHide);
    document.addEventListener("touchmove", cleanupAndHide);
  }, 100);

  // Auto-close
  setTimeout(cleanupAndHide, duration);
}
