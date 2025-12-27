<template>
  <div id="header" class="header-area style-two bg-white w-100">
    <div class="container-fluid">
      <div class="row align-items-center">
        <div class="col-xl-6 col-lg-5 col-md-5 col-sm-4">
          <div class="header-left d-flex align-items-center">
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
            <button class="header-burger-menu transition position-relative lh-1 bg-transparent p-0 border-0"
              id="header-burger-menu" title="Hide/Show" @click="toggleBurgerMenu">
              <i class="menu ri-menu-line" @click="toggleButtonVisibility('openBtn')" v-show="showOpenBtn"></i>
              <i class="close ri-close-line" @click="toggleButtonVisibility('closeBtn')" v-show="showCloseBtn"></i>
              <!-- <img src="../../assets/img/icons/sidebar-menu.svg" alt="Image" /> -->
            </button>
          </div>
        </div>
        <div class="col-xl-6 col-lg-7 col-md-7 col-sm-8">
          <div class="header-right d-flex align-items-center justify-content-sm-end justify-content-between">
            <div class="option-item" v-if="hasPermission('SALE_POS')">
              <router-link to="/pos" class="pos-link rounded-1 d-inline-block lh-1 fw-semibold transition">
                POS
              </router-link>
            </div>
            <div class="option-item">
              <ToggleFullscreenBtn />
            </div>
            <!-- <div class="option-item">
              <LanguageDropdown />
            </div> -->
            <div class="option-item">
              <NotificationDropdown />
            </div>
            <div class="option-item">
              <ProfileDropdown />
            </div>
            <div class="option-item">
              <SettingsBarBtn />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useUserStore } from "@/stores/userStore";

import ToggleFullscreenBtn from "./HeaderContent/ToggleFullscreenBtn.vue";
import LanguageDropdown from "./HeaderContent/LanguageDropdown.vue";
import NotificationDropdown from "./HeaderContent/NotificationDropdown.vue";
import ProfileDropdown from "./HeaderContent/ProfileDropdown.vue";
import SettingsBarBtn from "./HeaderContent/SettingsBarBtn.vue";

const userStore = useUserStore();

const hasPermission = (permission: string) =>
  userStore.userPermissions.includes(permission);

// Burger menu state
const isBurgerMenu = ref(false);
const showOpenBtn = ref(true);
const showCloseBtn = ref(false);

// Methods
function toggleBurgerMenu() {
  isBurgerMenu.value = !isBurgerMenu.value;
  document.body.classList.toggle("minimized", isBurgerMenu.value);
  localStorage.setItem("minimized", isBurgerMenu.value.toString());
}

function toggleButtonVisibility(buttonId: string) {
  if (buttonId === "openBtn") {
    showOpenBtn.value = false;
    showCloseBtn.value = true;
  } else if (buttonId === "closeBtn") {
    showOpenBtn.value = true;
    showCloseBtn.value = false;
  }
}
</script>

<style lang="scss">
.header-area {
  box-shadow: 0px 4px 30px 0px rgba(0, 0, 0, 0.03);

  &.style-two {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 99;

    padding: {
      top: 28.5px;
      bottom: 28.5px;
    }

    .header-left {
      .header-burger-menu {
        margin-left: 40px;

        i {
          font-size: 25px;

          // &.close {
          //   display: none;
          // }
        }
      }
    }
  }

  .header-left {
    .logo {
      min-width: 110px;
    }

    .search-area {
      margin-left: 30px;

      input {
        height: 55px;
        padding: 13px 35px 13px 23px;

        &::placeholder {
          opacity: 1;
          color: #8d929a;
        }
      }
    }
  }

  .header-right {
    .navbar-nav {
      margin-right: 25px;

      &>.nav-item {
        display: inline-block;
        margin-right: 20px;
        position: relative;

        &:last-child {
          margin-right: 0;
        }

        .nav-link {
          color: #0e1622;
          font-weight: 500;
          position: relative;

          &.dropdown-toggle {
            padding-right: 12px;
          }

          &.active,
          &:hover {
            color: var(--secondaryColor);
          }

          &.dropdown-toggle {
            &:before {
              position: absolute;
              content: "";
              right: -1px;
              top: 20px;
              background-image: url(../../assets/img/icons/down-arrow-black.svg);
              background-repeat: no-repeat;
              background-size: 10px;
              width: 10px;
              height: 10px;
            }

            &:after {
              display: none;
            }

            &.active,
            &:hover {
              &:before {
                background-image: url(../../assets/img/icons/down-arrow-2.svg);
              }
            }
          }
        }

        .dropdown-menu {
          top: 65px;
          opacity: 0;
          z-index: 99;
          width: 260px;
          display: block;
          padding: 12px 25px;
          visibility: hidden;
          transition: all 0.2s ease-in-out;
          border: none;
          transform: translate3d(0, 18px, 0);
          box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;

          .nav-item {
            .nav-link {
              transition: var(--transition);

              &:hover {
                padding-left: 8px;
              }
            }
          }
        }

        &:hover {
          .dropdown-menu {
            top: 50px;
            opacity: 1;
            visibility: visible;
            transform: translate3d(0, 0, 0);
          }
        }
      }
    }

    .option-item {
      margin-right: 20px;

      &:last-child {
        margin-right: 0;
      }

      .pos-link {
        color: #4f46e5;
        border: 2px solid var(--Purple-Gradient, #4f46e5);
        box-shadow: 0px 5px 10px 0px rgba(99, 102, 241, 0.2);
        padding: 11.5px 22.5px;

        &:hover {
          border-color: transparent;
          background-color: #4f46e5;
          color: var(--whiteColor);
        }
      }

      .notification-dropdown,
      .language-dropdown {
        .dropdown-toggle {
          &:after {
            display: none;
          }
        }
      }

      .language-dropdown {
        .dropdown-toggle {
          img {
            position: relative;
            top: 1px;
          }
        }
      }
    }
  }
}

.shopcart {
  padding: 3px 7px 0 0;

  span {
    width: 17px;
    height: 17px;
    font-size: 11px;
    background: var(--Purple-Gradient,
        linear-gradient(132deg, #4f46e5 4.27%, #6366f1 100%));
    line-height: 17px;
  }
}

.searchModal {
  overflow-x: hidden;

  .modal-dialog {
    margin: 0;
    width: 100%;
    max-width: 100%;
    padding: 30px 15px;
    background-color: var(--whiteColor);
    background-position: center center;
    background-repeat: no-repeat;
    background-size: cover;

    .modal-content {
      border: 0;
      padding: 0;
      display: flex;
      flex-wrap: wrap;
      border-radius: 0;
      max-width: 720px;
      background-color: transparent;
      margin-left: auto;
      margin-right: auto;

      form {
        width: calc(100% - 25px);
        position: relative;

        .form-control {
          height: 55px;
          border-radius: 0;
          box-shadow: unset;
          border-radius: 5px;
          color: var(--blackColor);
          font-size: 14px;
          border: 1px solid #eeeeee;
          background-color: var(--whiteColor);
          padding-left: 18px;
          padding-right: 18px;

          &::placeholder {
            transition: var(--transition);
            color: var(--paraColor);
          }

          &:focus {
            &::-moz-placeholder {
              color: transparent;
            }

            &:-ms-input-placeholder {
              color: transparent;
            }

            &::placeholder {
              color: transparent;
            }
          }

          &:-moz-placeholder {
            -moz-transition: var(--transition);
            transition: var(--transition);
            color: var(--paragraphColor);
          }

          &:-ms-input-placeholder {
            -ms-transition: var(--transition);
            transition: var(--transition);
            color: var(--paragraphColor);
          }
        }

        button {
          top: 5px;
          border: 0;
          padding: 0;
          right: 5px;
          width: 45px;
          height: 45px;
          font-size: 17px;
          border-radius: 5px;
          position: absolute;
          color: var(--whiteColor);
          transition: var(--transition);
          background-color: var(--secondaryColor);

          &:hover {
            background-color: var(--titleColor);
            color: var(--whiteColor);
          }

          img {
            position: relative;
            top: -1px;
            filter: brightness(0) invert(1);
          }
        }
      }

      .btn-close {
        top: 13px;
        padding: 0;
        z-index: 2;
        opacity: 1;
        right: 0px;
        text-align: right;
        cursor: pointer;
        box-shadow: unset;
        position: absolute;
        background-color: transparent;
        background-image: none;

        img {
          min-width: 22px;
        }
      }
    }
  }
}

@media only screen and (max-width: 575px) {
  .header-area {
    .header-left {
      .logo {
        max-width: 82px;
        min-width: 82px;
      }
    }

    .header-right {
      .option-item {
        margin-right: 10px;

        &:last-child {
          margin-right: 0;
        }

        .settings-toggler {
          img {
            max-width: 20px;
            position: relative;
            left: 0.5px;
          }
        }
      }
    }

    &.style-two {
      .header-left {
        margin-bottom: 15px;
        justify-content: space-between;
      }

      .header-right {
        justify-content: space-between;

        .option-item {
          margin-right: 10px;

          &:last-child {
            margin-right: 0;
          }

          .pos-link {
            font-size: 14px;
            padding: 8px 15px;
          }
        }
      }
    }
  }
}

@media only screen and (max-width: 767px) {
  .header-area {
    &.style-two {
      padding: 18px 0;

      .header-left {
        .header-burger-menu {
          margin-left: 20px;
        }
      }

      .header-right {
        .option-item {
          margin-right: 15px;

          &:last-child {
            margin-right: 0;
          }
        }
      }
    }
  }
}

@media only screen and (max-width: 991px) {
  .header-area {
    .header-left {
      .logo {
        max-width: 85px;
        min-width: 85px;
      }
    }

    .header-right {
      .option-item {
        .settings-toggler {
          width: 35px;
          height: 35px;
        }

        .profile-dropdown {
          .dropdown-toggle {
            img {
              width: 35px;
              height: 35px;
            }
          }
        }
      }
    }
  }

  .profile-dropdown img {
    margin-right: 0;
  }

  .navbar-toggler {
    width: 35px;
    height: 35px;
    border: 1px solid rgba(0, 0, 0, 0.1);
  }
}

@media only screen and (min-width: 992px) and (max-width: 1199px) {
  .header-area {
    .header-right {
      .navbar-nav {
        .nav-item {
          font-size: 14px;
        }
      }
    }
  }
}

@media only screen and (min-width: 1400px) {
  .header-area {
    .container-fluid {
      padding: {
        right: 50px;
        left: 50px;
      }
    }

    .header-left {
      .search-area {
        margin-left: 80px;
      }
    }

    .header-right {
      .navbar-nav {
        margin-right: 38px;

        &>.nav-item {
          margin-right: 43px;
        }
      }
    }

    &.style-two {
      .header-left {
        .header-burger-menu {
          margin-left: 78px;
        }
      }

      .header-right {
        .option-item {
          margin-right: 27px;

          &:last-child {
            margin-right: 0;
          }
        }
      }
    }
  }
}
</style>