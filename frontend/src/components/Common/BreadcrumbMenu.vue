<template>
  <div class="row mb-25 pb-18 align-items-end br-1 gx-0">
    <div class="col-md-6">
      <h4 class="mb-0">{{ pageTitle }}</h4>
    </div>
    <div class="col-md-6">
      <ul class="breadcrumb-menu list-style lh-base text-md-end">
        <li>
          <router-link to="/">Dashboard</router-link>
        </li>
        <li class="active">{{ pageTitle }}</li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "BreadcrumbMenu",
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