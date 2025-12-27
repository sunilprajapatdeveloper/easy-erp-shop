<template>
  <div
    class="br-wrapper d-flex flex-wrap align-items-center justify-content-between pb-60 br-2"
  >
    <h4 class="text-title fs-30 mb-0 fw-black">{{ pageTitle }}</h4>
    <ul class="breadcrumb-menu list-style">
      <li class="fs-14">
        <router-link to="/landing-page"> Home </router-link>
      </li>
      <li class="fs-14 active">{{ pageTitle }}</li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "BreadCrumb",
  props: ["pageTitle"],
});
</script>

<style lang="scss" scoped>
.breadcrumb-menu {
  li {
    display: inline-block;
    position: relative;
    padding-right: 20px;
    font-size: 14px;
    font-weight: 500;
    &:after {
      position: absolute;
      top: -1px;
      right: 6px;
      content: "/";
      font-size: 14px;
      font-weight: 400;
    }
    &:last-child {
      padding-right: 0;
      &:after {
        display: none;
      }
    }
    a {
      color: var(--secondaryColor);
    }
  }
}

@media only screen and (max-width: 767px) {
  .breadcrumb-menu {
    margin-top: 10px;
  }
}
</style>