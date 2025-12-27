export enum RoundingMode {
  HALF_UP = "HALF_UP",
  HALF_DOWN = "HALF_DOWN",
  HALF_EVEN = "HALF_EVEN",
  UP = "UP",
  DOWN = "DOWN",
  CEILING = "CEILING",
  FLOOR = "FLOOR",
}

export const RoundingModeLabels: Record<RoundingMode, string> = {
  [RoundingMode.HALF_UP]: "Half Up",
  [RoundingMode.HALF_DOWN]: "Half Down",
  [RoundingMode.HALF_EVEN]: "Half Even",
  [RoundingMode.UP]: "Up",
  [RoundingMode.DOWN]: "Down",
  [RoundingMode.CEILING]: "Ceiling",
  [RoundingMode.FLOOR]: "Floor",
};
