<template>
  <div>
    <input
      @click="toggleDarkMode"
      type="checkbox"
      id="theme_switch"
      class="theme-switch"
    />
    <label for="theme_switch" class="checkbox-label">
      <span class="round"></span>
    </label>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "LightDarkSwtichBtn",

  data() {
    return {
      isDarkMode: false,
    };
  },
  methods: {
    toggleDarkMode() {
      this.isDarkMode = !this.isDarkMode;
      document.body.classList.toggle("theme-dark", this.isDarkMode);
      localStorage.setItem("theme-dark", this.isDarkMode.toString());
    },
  },
  mounted() {
    const storedDarkMode = localStorage.getItem("theme-dark");
    if (storedDarkMode) {
      this.isDarkMode = storedDarkMode === "true";
      document.body.classList.toggle("theme-dark", this.isDarkMode);
    }
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
  .theme-switch {
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
  .theme-switch:checked + .checkbox-label .round {
    transform: translateX(26px);
    box-shadow: none;
    background-color: #4f46e5;
    transition: var(--transition);
  }
}
</style>