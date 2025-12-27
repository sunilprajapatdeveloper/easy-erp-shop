<template>
  <div class="setting-sidebar offcanvas offcanvas-end border-0 bg-white" tabindex="-1" id="settingsBar">
    <button type="button" class="close-btn bg-transparent position-absolute lh-1 p-0 border-0"
      data-bs-dismiss="offcanvas" aria-label="Close">
      <img src="../../assets/img/icons/close-circle.svg" alt="Image" />
    </button>
    <div class="offcanvas-body p-0">
      <router-link to="/" class="logo">
        EasyERP
        Shop
        <!-- <img
                class="logo-light"
                src="../../../assets"
                alt="Image"
              />
              <img
                class="logo-dark"
                src="../../../assets"
                alt="Image"
              /> -->
      </router-link>
      <!-- <div class="settings-item">
        <div class="d-flex justify-content-between align-items-center">
          <span class="fs-14 fw-medium text-title mb-0">Enable RTL</span>
          <div>
            <input type="checkbox" class="rtl_switch" id="rtl_switch" />
            <label for="rtl_switch" class="checkbox-label">
              <span class="round"></span>
            </label>
          </div>
        </div>
      </div> -->
      <div class="settings-item">
        <div class="d-flex justify-content-between align-items-center">
          <span class="fs-14 fw-medium text-title mb-0">Enable Dark Mode</span>
          <LightDarkSwtichBtn />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import LightDarkSwtichBtn from "./LightDarkSwtich.vue";

export default defineComponent({
  name: "SettingSidebar",
  components: {
    LightDarkSwtichBtn,
  },
});
</script>

<style lang="scss" scoped>
.setting-sidebar {
  box-shadow: 0px 4px 34px 0px rgba(99, 102, 241, 0.11);
  padding: 25px 0 50px;

  .close-btn {
    top: 32px;
    right: 25px;
  }

  .logo {
    display: block;
    padding: 0 30px 30px 30px;
    border-bottom: 1px solid #eef3fa;
    margin: 0 0 30px;
  }

  .theme-switch,
  .rtl_switch {
    opacity: 0;
    position: absolute;
  }

  .checkbox-label {
    background-color: #eef2f9;
    width: 58px;
    height: 32px;
    border-radius: 50px;
    position: relative;
    padding: 5px;
    cursor: pointer;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border: 1px solid #6366f1;
  }

  .checkbox-label .round {
    background-color: var(--whiteColor);
    width: 24px;
    height: 24px;
    position: absolute;
    border-radius: 50px;
    box-shadow: 0px 2px 4px 0px rgba(79, 70, 229, 0.25);
    left: 4px;
    top: 3px;
    border-radius: 50%;
    transition: transform 0.2s linear;
  }

  .theme-switch:checked+.checkbox-label .round,
  .rtl_switch:checked+.checkbox-label .round {
    transform: translateX(26px);
    box-shadow: none;
    background-color: #4f46e5;
    transition: var(--transition);
  }

  .offcanvas-body {
    .settings-item {
      border-bottom: 1px solid #eef3fa;
      padding: 0 30px 30px 30px;
      margin-bottom: 25px;

      h6 {
        border-bottom: 1px solid #eef3fa;
        margin: 0 -30px 25px -30px;
        padding: 0 30px 30px;
      }

      &:last-child {
        border-bottom: none;
        margin-bottom: 0;
        padding-bottom: 0;
      }
    }
  }
}
</style>