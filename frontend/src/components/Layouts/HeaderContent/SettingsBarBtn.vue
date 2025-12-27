<template>
  <a
    class="settings-toggler d-flex flex-column justify-content-center align-items-center"
    data-bs-toggle="offcanvas"
    href="#settingsBar"
    role="button"
    aria-controls="settingsBar"
  >
    <img src="../../../assets/img/icons/setting.svg" alt="Image" />
  </a>
</template>

<script>
export default {
  name: "SettingsBarBtn",
};
</script>

<style lang="scss" scoped>
.settings-toggler {
  width: 40px;
  height: 40px;
  border-radius: 7px;
  background: var(
    --Purple-Gradient,
    linear-gradient(132deg, #4f46e5 4.27%, #6366f1 100%)
  );
  box-shadow: 0px 4px 20px 0px rgba(91, 89, 236, 0.2);
  img {
    display: block;
    margin: 0 auto;
  }
}
</style>