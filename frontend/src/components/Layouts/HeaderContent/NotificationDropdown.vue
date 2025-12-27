<template>
  <div class="notification-dropdown">
    <button
      class="dropdown-toggle text-start text-black-emphasis d-flex align-items-center position-relative bg-transparent border-0 transition lh-1"
      type="button"
      data-bs-toggle="dropdown"
      aria-expanded="false"
    >
      <img src="../../../assets/img/icons/notification.svg" alt="admin" />
      <span
        class="position-absolute rounded-circle text-center end-0 text-white fw-medium"
      >
        3
      </span>
    </button>
    <div
      class="dropdown-menu rounded-1 bg-white border-0 start-auto end-0 p-0"
      data-popper-placement="bottom-end"
    >
      <ul class="list-style dropdown-body">
        <li class="text-muted position-relative">
          <span
            class="unread position-absolute top-50 start-0 rounded-circle d-inline-block"
          ></span>
          <router-link
            to="/login"
            class="d-block text-decoration-none text-title"
          >
            You have requested a
            <strong class="fw-semibold">withdrawal</strong>
            <span class="d-block text-paragraph"> 2 hrs ago </span>
          </router-link>
        </li>
        <li class="text-muted position-relative">
          <span
            class="unread position-absolute top-50 start-0 rounded-circle d-inline-block"
          ></span>
          <span class="d-block text-black-emphasis"></span>
          <router-link
            to="/login"
            class="d-block text-decoration-none text-title"
          >
            2 product quantity
            <strong class="fw-semibold">alerts</strong>
            <span class="d-block text-paragraph"> 5 hrs ago </span>
          </router-link>
        </li>
        <li class="text-muted position-relative">
          <span
            class="read position-absolute top-50 start-0 rounded-circle d-inline-block"
          ></span>
          <router-link
            to="/login"
            class="d-block text-decoration-none text-title"
          >
            4 product <strong class="fw-semibold">added</strong>
            <span class="d-block text-paragraph"> 6 hrs ago </span>
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "NotificationDropdown",
};
</script>

<style lang="scss" scoped>
.notification-dropdown {
  .dropdown-toggle {
    padding: 2px 7px 0 0;
    span {
      width: 20px;
      height: 20px;
      top: -8px;
      font-size: 12px;
      line-height: 18px;
      background: var(
        --Purple-Gradient,
        linear-gradient(132deg, #4f46e5 4.27%, #6366f1 100%)
      );
      line-height: 20px;
      background: #0eb77a;
    }
  }
  .dropdown-menu {
    width: 315px;
    font-size: 14px;
    box-shadow: 0px 4px 34px rgba(101, 96, 240, 0.1);
    margin-top: 19px !important;
    margin-right: -10px !important;
    .dropdown-body {
      padding: 15px 28px;
      li {
        position: relative;
        padding: 15px 0 15px 30px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
        .unread,
        .read {
          width: 12px;
          height: 12px;
          transform: translateY(-50%);
          i,
          img {
            opacity: 0.7;
          }
        }
        .unread {
          background-color: #0eb77a;
        }
        .read {
          background-color: #4f46e5;
        }
        &:last-child {
          border-bottom: none;
        }
        a {
          line-height: 22px;
        }
      }
    }
  }
}
</style>