import { App, ref } from "vue";
import { Offcanvas } from "bootstrap";

type PopupType = "success" | "error";

export interface PopupState {
  type: PopupType;
  message: string;
  isVisible: boolean;
}

export function showPopup(type: PopupType, message: string, duration = 3000) {
  const popupId = type === "success" ? "successPopup" : "errorPopup";
  const triggerId =
    type === "success" ? "triggerSuccessPopup" : "triggerErrorPopup";

  const trigger = document.getElementById(triggerId) as HTMLAnchorElement;
  const popup = document.getElementById(popupId);

  if (!trigger || !popup) {
    console.warn(`${type} popup trigger or element not found.`);
    return;
  }

  trigger.click();
  const instance = Offcanvas.getOrCreateInstance(popup);

  const cleanupAndHide = () => {
    instance?.hide();
    document.body.classList.remove("offcanvas-backdrop");
    document.body.style.overflow = "";
    document.body.style.paddingRight = "";
  };

  setTimeout(cleanupAndHide, duration);
}

export default {
  install(app: App) {
    // Provide globally as $popup
    app.config.globalProperties.$popup = showPopup;
    app.provide("popup", showPopup);
  },
};
