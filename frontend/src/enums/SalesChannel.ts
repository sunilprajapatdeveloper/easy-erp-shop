export enum SalesChannel {
  POS = "POS",
  ONLINE = "ONLINE",
  MOBILE = "MOBILE",
  MARKETPLACE = "MARKETPLACE",
  CALL_CENTER = "CALL_CENTER",
  SOCIAL = "SOCIAL",
  B2B = "B2B",
}

export const SalesChannelLabels: Record<SalesChannel, string> = {
  [SalesChannel.POS]: "POS (In-Store)",
  [SalesChannel.ONLINE]: "Online",
  [SalesChannel.MOBILE]: "Mobile App",
  [SalesChannel.MARKETPLACE]: "Marketplace",
  [SalesChannel.CALL_CENTER]: "Call Center",
  [SalesChannel.SOCIAL]: "Social Commerce",
  [SalesChannel.B2B]: "B2B Portal",
};
