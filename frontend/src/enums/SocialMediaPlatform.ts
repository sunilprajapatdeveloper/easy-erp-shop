export enum SocialMediaPlatform {
  FACEBOOK = "FACEBOOK",
  INSTAGRAM = "INSTAGRAM",
  TWITTER = "TWITTER",
  LINKEDIN = "LINKEDIN",
  TIKTOK = "TIKTOK",
  WHATSAPP = "WHATSAPP",
  YOUTUBE = "YOUTUBE",
  TELEGRAM = "TELEGRAM",
  CUSTOM = "CUSTOM",
}

export const SocialMediaPlatformLabels: Record<SocialMediaPlatform, string> = {
  [SocialMediaPlatform.FACEBOOK]: "Facebook",
  [SocialMediaPlatform.INSTAGRAM]: "Instagram",
  [SocialMediaPlatform.TWITTER]: "Twitter",
  [SocialMediaPlatform.LINKEDIN]: "LinkedIn",
  [SocialMediaPlatform.TIKTOK]: "TikTok",
  [SocialMediaPlatform.WHATSAPP]: "WhatsApp",
  [SocialMediaPlatform.YOUTUBE]: "YouTube",
  [SocialMediaPlatform.TELEGRAM]: "Telegram",
  [SocialMediaPlatform.CUSTOM]: "Custom",
};
