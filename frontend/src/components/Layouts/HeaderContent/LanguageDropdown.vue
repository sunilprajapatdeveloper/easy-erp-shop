<template>
  <div class="language-dropdown">
    <button
      class="dropdown-toggle text-start text-black-emphasis d-flex align-items-center p-0 position-relative bg-transparent border-0 transition lh-1"
      type="button"
      data-bs-toggle="dropdown"
      aria-expanded="false"
    >
      <img src="../../../assets/img/icons/globe.svg" alt="globe" />
    </button>
    <div
      class="dropdown-menu rounded-1 bg-white border-0 start-auto end-0"
      data-popper-placement="bottom-start"
    >
      <ul class="list-style dropdown-body">
        <li>
          <button
            class="flag-item border-0 bg-transparent text-start d-flex align-items-center p-0"
          >
            <img src="../../../assets/img/flags/uk.webp" alt="Image" />
            <span class="text-title fs-14 d-block ms-3"> English </span>
          </button>
        </li>
        <li class="position-relative">
          <button
            class="flag-item border-0 bg-transparent text-start d-flex align-items-center p-0"
          >
            <img src="../../../assets/img/flags/france.webp" alt="Image" />
            <span class="text-title fs-14 d-block ms-3"> French </span>
          </button>
        </li>
        <li class="position-relative">
          <button
            class="flag-item border-0 bg-transparent text-start d-flex align-items-center p-0"
          >
            <img src="../../../assets/img/flags/japan.webp" alt="Image" />
            <span class="text-title fs-14 d-block ms-3"> Japanese </span>
          </button>
        </li>
        <li class="position-relative">
          <button
            class="flag-item border-0 bg-transparent text-start d-flex align-items-center p-0"
          >
            <img src="../../../assets/img/flags/turkey.webp" alt="Image" />
            <span class="text-title fs-14 d-block ms-3"> Turkish </span>
          </button>
        </li>
        <li class="position-relative">
          <button
            class="flag-item border-0 bg-transparent text-start d-flex align-items-center p-0"
          >
            <img src="../../../assets/img/flags/china.webp" alt="Image" />
            <span class="text-title fs-14 d-block ms-3"> Chinese </span>
          </button>
        </li>
        <li class="position-relative">
          <button
            class="flag-item border-0 bg-transparent text-start d-flex align-items-center p-0"
          >
            <img src="../../../assets/img/flags/german.webp" alt="Image" />
            <span class="text-title fs-14 d-block ms-3"> German </span>
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "LanguageDropdown",
};
</script>

<style lang="scss" scoped>
.language-dropdown {
  .dropdown-menu {
    width: 225px;
    font-size: 14px;
    box-shadow: 0px 4px 34px rgba(101, 96, 240, 0.1);
    margin-top: 19px !important;
    margin-right: -10px !important;
    .dropdown-body {
      padding: 15px 30px;
      li {
        position: relative;
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
        margin-bottom: 15px;
        padding-bottom: 15px;
        &:last-child {
          padding-bottom: 0;
          margin-bottom: 0;
          border-bottom: none;
        }
        button {
          img {
            max-width: 40px;
          }
        }
      }
    }
  }
}
</style>