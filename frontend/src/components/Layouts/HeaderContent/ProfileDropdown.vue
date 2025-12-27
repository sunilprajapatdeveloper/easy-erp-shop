<template>
  <div class="profile-dropdown">
    <button
      class="dropdown-toggle text-start text-black-emphasis d-flex align-items-center p-0 position-relative bg-transparent border-0 transition lh-1"
      type="button" data-bs-toggle="dropdown" aria-expanded="false">
      <img :src="profileImage" alt="profile" class="rounded-circle border border-white shadow-sm object-fit-cover"
        style="width: 42px; height: 42px;" />
      <span class="md-none">
        <span class="d-block fw-bold text-title">
          {{ fullName }}
        </span>
        <span class="text-paragraph fs-14">Admin</span>
      </span>
    </button>
    <div class="dropdown-menu top-1 shadow-none border-0">
      <ul class="list-style">
        <li class="fs-14 d-block transition">
          <router-link to="/my-profile" class="d-block">
            My Profile
          </router-link>
        </li>
        <li class="fs-14 d-block transition">
          <router-link to="/system-settings" class="d-block">
            Settings
          </router-link>
        </li>
        <li class="fs-14 d-block transition">
          <router-link to="/logout" class="d-block"> Logout </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useUserStore } from "@/stores/userStore";

const userStore = useUserStore();

const fullName = computed(() => {
  if (userStore.currentUser) {
    const { firstname, lastname, username } = userStore.currentUser;
    return firstname || lastname
      ? `${firstname ?? ""} ${lastname ?? ""}`.trim()
      : username;
  }
  return "Guest";
});

const profileImage = computed(() => {
  const user = userStore.currentUser;

  if (!user) {
    return "/src/assets/img/admin.webp";
  }

  // Check if user has profileUrl field directly
  if (user.profile) {
    return user.profile;
  }

  // Try to construct from stored filename
  if (user.profile && user.companyId) {
    // Check what format profile is in
    if (user.profile.startsWith('http')) {
      return user.profile; // Already a URL
    } else if (user.profile.includes('/')) {
      // Might be a path
      return `http://localhost:9090${user.profile.startsWith('/') ? '' : '/'}${user.profile}`;
    } else {
      // Assume it's a filename
      return `http://localhost:9090/api/v1/media/local/${user.companyId}/${user.profile}`;
    }
  }

  return "/src/assets/img/admin.webp";
});
</script>

<style lang="scss" scoped>
.profile-dropdown {
  img {
    border-radius: 5px;
    margin-right: 10px;
  }

  .dropdown-toggle {
    &:after {
      display: none;
    }
  }

  span {
    &:nth-child(1) {
      margin-bottom: 5px;
    }

    &:nth-child(2) {
      color: #8a939b;
    }
  }

  .dropdown-menu {
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.15) !important;
    padding: 14px 20px;
    top: 20px !important;

    ul {
      li {
        margin-bottom: 7px;
        padding-bottom: 7px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.03);

        &:last-child {
          margin-bottom: 0;
          padding-bottom: 0;
          border-bottom: none;
        }

        a {
          &:hover {
            color: var(--secondaryColor);
            padding-left: 5px;
          }
        }
      }
    }
  }
}
</style>