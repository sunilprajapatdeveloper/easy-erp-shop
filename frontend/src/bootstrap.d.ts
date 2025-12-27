declare module "bootstrap" {
  export class Offcanvas {
    constructor(element: Element | string, options?: any);
    static getOrCreateInstance(element: Element | string): Offcanvas;
    show(): void;
    hide(): void;
  }

  export class Modal {
    constructor(element: Element | string, options?: any);
    static getOrCreateInstance(element: Element | string): Modal;
    show(): void;
    hide(): void;
  }
}
