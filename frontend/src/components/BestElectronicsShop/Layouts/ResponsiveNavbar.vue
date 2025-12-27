<template>
  <div class="responsive-navbar offcanvas offcanvas-end border-0" tabindex="-1" id="navbarOffcanvas">
    <div class="offcanvas-header">
      <a href="/landing-page" class="logo d-inline-block">
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
      </a>
      <button type="button" class="close-btn bg-transparent position-relative lh-1 p-0 border-0"
        data-bs-dismiss="offcanvas" aria-label="Close">
        <img src="../../../assets/img/icons/close-circle.svg" alt="Image" />
      </button>
    </div>
    <div class="offcanvas-body">
      <ul class="responsive-menu list-style">
        <li :class="['responsive-menu-list', { active: isActive }]" @click="toggleActive">
          <a href="javascript:void(0)" class="dropdown-toggle nav-link">
            Shop
          </a>
          <ul class="responsive-menu-items">
            <li class="nav-item fs-14">
              <a href="/shop-right-sidebar" class="nav-link">
                Shop Right Sideabr
              </a>
            </li>
            <li class="nav-item fs-14">
              <a href="/shop-left-sidebar" class="nav-link">
                Shop left Sideabr
              </a>
            </li>
            <li class="nav-item fs-14">
              <a href="/shop-grid" class="nav-link">Shop Grid</a>
            </li>
            <li class="nav-item fs-14">
              <a href="/shop-details" class="nav-link">Shop Details</a>
            </li>
            <li class="nav-item fs-14">
              <a href="/cart" class="nav-link">Cart</a>
            </li>
            <li class="nav-item fs-14">
              <a href="/wishlist" class="nav-link">Wishlist</a>
            </li>
            <li class="nav-item fs-14">
              <a href="/checkout" class="nav-link">Checkout</a>
            </li>
            <li class="nav-item fs-14">
              <a href="/login" class="nav-link">Login</a>
            </li>
            <li class="nav-item fs-14">
              <a href="terms-conditions" class="nav-link">Checkout</a>
            </li>
          </ul>
        </li>
        <li class="responsive-menu-list">
          <a href="/offers" class="nav-link">Offer</a>
        </li>
        <li class="responsive-menu-list">
          <a href="/faq" class="nav-link">FAQ</a>
        </li>
        <li class="responsive-menu-list">
          <a href="/contact" class="nav-link">Contact</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "ResponsiveNavbar",
  data() {
    return {
      isActive: ref(false),
    };
  },
  methods: {
    toggleActive() {
      this.isActive = !this.isActive;
    },
  },
});
</script>

<style lang="scss" scoped>
@media only screen and (max-width: 991px) {
  .responsive-navbar {
    background-color: var(--ashColor);
    box-shadow: 0px 2px 20px 0px rgba(0, 0, 0, 0.05);

    .offcanvas-header {
      padding: 20px 25px 10px;

      .close-btn {
        transition: var(--transition);
        color: var(--titleColor);
        font-size: 21px;
        cursor: pointer;
        top: 2px;
      }
    }

    .offcanvas-body {
      padding: 20px 25px;

      ul {
        list-style-type: none;
        margin-bottom: 0;

        &.responsive-menu {
          padding-left: 0;
          overflow: hidden;

          .responsive-menu-list {
            border-bottom: 1px solid rgba(0, 0, 0, 0.1);
            position: relative;
            line-height: 55px;
            height: 55px;
            clear: both;
            width: 100%;

            a {
              position: relative;
              height: 100%;
              display: block;
              padding-right: 40px;
              text-decoration: none;

              font: {
                size: 14px;
                weight: 500;
              }

              &.dropdown-toggle {
                &:before {
                  top: 50%;
                  right: 0;
                  content: "";
                  background-image: url(../../../assets/img/icons/down-arrow-black.svg);
                  background-repeat: no-repeat;
                  background-size: 10px;
                  width: 10px;
                  height: 10px;
                  position: absolute;
                  transform: translateY(-50%);
                  transition: var(--transition);
                  cursor: pointer;
                }

                &:after {
                  display: none;
                }
              }

              &:hover,
              &.active {
                color: var(--secondaryColor);
              }
            }

            .responsive-menu-items {
              height: 0;
              overflow: hidden;

              a {
                font-size: 15px;
                border-bottom: 1px solid rgba(0, 0, 0, 0.1);

                &:hover,
                &.active {
                  color: var(--secondaryColor);
                }
              }
            }
          }

          .active {
            &:after {
              transform: translateY(-50%) rotate(180deg);
            }

            >.responsive-menu-items {
              transition: all 200ms;
              display: block;
              height: auto;
              clear: both;
              float: left;
              width: 100%;
              padding: 0 0 0 20px;

              li {
                border-bottom: 1px solid rgba(255, 255, 255, 0.1);
              }
            }

            >a {
              color: var(--titleColor);
            }
          }
        }
      }
    }
  }
}
</style>