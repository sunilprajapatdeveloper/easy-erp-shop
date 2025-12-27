<template>
  <header
    id="header"
    :class="['header-area style-one bg-white w-100', { sticky: isSticky }]"
  >
    <div class="container-fluid">
      <div class="row align-items-center">
        <div class="col-xl-6 col-lg-5 col-md-8 col-3">
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
            <form
              action="#"
              class="search-area position-relative w-100 md-none"
            >
              <input
                type="search"
                placeholder="Search Your Product"
                class="w-100 bg_ash border-0 rounded-1 fs-14 text-black"
              />
              <button
                type="submit"
                class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-4"
              >
                <img src="../../../assets/img/icons/search.svg" alt="Image" />
              </button>
            </form>
          </div>
        </div>
        <div class="col-xl-6 col-lg-7 col-md-4 col-9">
          <div
            class="header-right d-flex align-items-center justify-content-end"
          >
            <ul class="navbar-nav d-flex flex-row md-none">
              <li class="nav-item">
                <a href="javascript:void(0)" class="dropdown-toggle nav-link">
                  Shop
                </a>
                <ul
                  class="dropdown-menu position-absolute bg-white start-0 rounded-1"
                >
                  <li class="nav-item fs-14">
                    <router-link to="/shop-right-sidebar" class="nav-link">
                      Shop Right Sideabr
                    </router-link>
                  </li>
                  <li class="nav-item fs-14">
                    <router-link to="/shop-left-sidebar" class="nav-link">
                      Shop left Sideabr
                    </router-link>
                  </li>
                  <li class="nav-item fs-14">
                    <router-link to="/shop-grid" class="nav-link">
                      Shop Grid
                    </router-link>
                  </li>
                  <li class="nav-item fs-14">
                    <router-link to="/shop-details" class="nav-link">
                      Shop Details
                    </router-link>
                  </li>
                  <li class="nav-item fs-14">
                    <router-link to="/cart" class="nav-link">
                      Cart
                    </router-link>
                  </li>
                  <li class="nav-item fs-14">
                    <router-link to="/wishlist" class="nav-link">
                      Wishlist
                    </router-link>
                  </li>
                  <li class="nav-item fs-14">
                    <router-link to="/checkout" class="nav-link">
                      Checkout
                    </router-link>
                  </li>
                  <li class="nav-item fs-14">
                    <router-link to="/login" class="nav-link">
                      Login
                    </router-link>
                  </li>
                  <li class="nav-item fs-14">
                    <router-link to="/terms-conditions" class="nav-link">
                      Terms & Conditions
                    </router-link>
                  </li>
                </ul>
              </li>
              <li class="nav-item">
                <router-link to="/offers" class="nav-link"> Offer </router-link>
              </li>
              <li class="nav-item">
                <router-link to="/faq" class="nav-link">FAQ</router-link>
              </li>
              <li class="nav-item">
                <router-link to="/contact" class="nav-link">
                  Contact
                </router-link>
              </li>
            </ul>
            <div class="option-item d-md-none">
              <button
                type="button"
                class="search-btn bg-transparent border-0 p-0 position-relative"
                data-bs-toggle="modal"
                data-bs-target="#searchModal"
              >
                <img
                  src="../../../assets/img/icons/search.svg"
                  alt="Image"
                  class="position-relative top-1"
                />
              </button>
            </div>
            <div class="option-item">
              <router-link
                to="/cart"
                class="shopcart position-relative d-inline-block"
              >
                <img src="../../../assets/img/icons/cart.svg" alt="Image" />
                <span
                  class="position-absolute rounded-circle text-center top-0 end-0 text-white fw-medium"
                >
                  2
                </span>
              </router-link>
            </div>
            <div class="option-item">
              <div class="profile-dropdown">
                <button
                  class="dropdown-toggle text-start text-black-emphasis d-flex align-items-center p-0 position-relative bg-transparent border-0 transition lh-1"
                  type="button"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  <img src="../../../assets/img/admin.webp" alt="admin" />
                  <span class="md-none">
                    <span class="d-block fw-bold text-title">Andrew Smith</span>
                    <span class="text-paragraph fs-14">User</span>
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
                      <router-link to="/logout" class="d-block">
                        Logout
                      </router-link>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="option-item me-lg-0">
              <a
                class="settings-toggler d-flex flex-column justify-content-center align-items-center"
                data-bs-toggle="offcanvas"
                href="#settingsBar"
                role="button"
                aria-controls="settingsBar"
              >
                <img src="../../../assets/img/icons/setting.svg" alt="Image" />
              </a>
            </div>
            <div class="option-item d-lg-none">
              <a
                class="navbar-toggler d-flex flex-column justify-content-center align-items-center rounded-1"
                data-bs-toggle="offcanvas"
                href="#navbarOffcanvas"
                role="button"
                aria-controls="navbarOffcanvas"
              >
                <img
                  src="../../../assets/img/icons/menu-bar.svg"
                  alt="Menu Bar"
                />
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>

  <SearchModal />
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from "vue";
import SearchModal from "./SearchModal.vue";

export default defineComponent({
  name: "MainHeader",
  components: {
    SearchModal,
  },
  setup() {
    const isSticky = ref(false);

    onMounted(() => {
      window.addEventListener("scroll", () => {
        let scrollPos = window.scrollY;
        isSticky.value = scrollPos >= 100;
      });
    });

    return {
      isSticky,
    };
  },
});
</script>

<style lang="scss" scoped>
.header-area {
  box-shadow: 0px 4px 30px 0px rgba(0, 0, 0, 0.03);
  &.style-one {
    position: relative;
    padding: {
      top: 22.5px;
      bottom: 22.5px;
    }
    &.sticky {
      position: sticky;
      top: 0;
      left: 0;
      width: 100%;
      z-index: 99;
      -webkit-animation: 500ms ease-in-out 0s normal none 1 running fadeInDown;
      animation: 500ms ease-in-out 0s normal none 1 running fadeInDown;
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
      & > .nav-item {
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
              background-image: url(../../../assets/img/icons/down-arrow-black.svg);
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
                background-image: url(../../../assets/img/icons/down-arrow-2.svg);
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
    background: var(
      --Purple-Gradient,
      linear-gradient(132deg, #4f46e5 4.27%, #6366f1 100%)
    );
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
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.03);
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
        & > .nav-item {
          margin-right: 43px;
        }
      }
    }
    &.style-one {
      .header-right {
        .option-item {
          margin-right: 30px;
          &:last-child {
            margin-right: 0;
          }
        }
      }
    }
  }
}
</style>