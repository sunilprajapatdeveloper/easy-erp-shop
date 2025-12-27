import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  SocialMediaSettings,
  CreateSocialMediaSettingsRequest,
  UpdateSocialMediaSettingsRequest,
} from "@/types/SocialMediaSettings";

const getHeaders = () => {
  const store = useUserStore();
  const headers: Record<string, string> = {};
  if (store.currentUser?.id)
    headers["X-User-Id"] = String(store.currentUser.id);
  if (store.currentUser?.companyId)
    headers["X-Company-Id"] = String(store.currentUser.companyId);
  if (store.token) headers["Authorization"] = `Bearer ${store.token}`;
  return { headers };
};

export const socialMediaSettingsService = {
  create: async (
    payload: CreateSocialMediaSettingsRequest,
    createdBy: number
  ): Promise<SocialMediaSettings> => {
    const res = await api.post<SocialMediaSettings>(
      "/v1/social-media-settings",
      payload,
      { ...getHeaders(), params: { createdBy } }
    );
    return res.data;
  },

  update: async (
    id: number,
    companyId: number,
    payload: UpdateSocialMediaSettingsRequest,
    updatedBy: number
  ): Promise<SocialMediaSettings> => {
    const res = await api.put<SocialMediaSettings>(
      `/v1/social-media-settings/${id}`,
      payload,
      { ...getHeaders(), params: { companyId, updatedBy } }
    );
    return res.data;
  },

  getById: async (
    id: number,
    companyId: number
  ): Promise<SocialMediaSettings> => {
    const res = await api.get<SocialMediaSettings>(
      `/v1/social-media-settings/${id}`,
      { ...getHeaders(), params: { companyId } }
    );
    return res.data;
  },

  listByCompany: async (companyId: number): Promise<SocialMediaSettings[]> => {
    const res = await api.get<SocialMediaSettings[]>(
      "/v1/social-media-settings",
      { ...getHeaders(), params: { companyId } }
    );
    return res.data;
  },
};
