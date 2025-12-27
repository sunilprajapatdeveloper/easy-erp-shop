<template>
  <div class="row pt-100">
    <div class="col-xxl-9 col-xl-8 pe-xxl-5">
      <div class="table-responsive cart-table">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr class="b">
              <th
                scope="col"
                class="text-title fw-semibold fs-14 pt-0 ps-0 bg-transparent"
              >
                PRODUCT
              </th>
              <th
                scope="col"
                class="text-title fw-semibold fs-14 pt-0 bg-transparent"
              >
                PRICE
              </th>
              <th
                scope="col"
                class="text-title fw-semibold fs-14 pt-0 bg-transparent"
              >
                QUANTITY
              </th>
              <th
                scope="col"
                class="text-title fw-semibold fs-14 pt-0 bg-transparent"
              >
                TOTAL
              </th>
              <th
                scope="col"
                class="text-title fw-semibold fs-14 pt-0 bg-transparent"
              ></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td
                class="bg-transparent shadow-none fw-medium text-black title ps-0"
              >
                <div class="d-flex align-items-center shop-item">
                  <img
                    src="../../../assets/img/products/cart-item-1.webp"
                    alt="Image"
                  />
                  <router-link to="/shop-details">Laptop</router-link>
                </div>
              </td>
              <td
                class="bg-transparent shadow-none lh-1 fw-semibold text-purple"
              >
                $11.50
              </td>
              <td class="bg-transparent shadow-none lh-1 fw-semibold text">
                <QuantityCounter />
              </td>
              <td
                class="bg-transparent shadow-none lh-1 fw-semibold text-purple"
              >
                $11.50
              </td>
              <td
                class="bg-transparent shadow-none lh-1 fs-14 fw-semibold text-paragraph"
              >
                <button type="button" class="bg-transparent border-0 p-0">
                  <img
                    src="../../../assets/img/icons/close-2.svg"
                    alt="Image"
                  />
                </button>
              </td>
            </tr>
            <tr>
              <td
                class="bg-transparent shadow-none fw-medium text-black title ps-0"
              >
                <div class="d-flex align-items-center shop-item">
                  <img
                    src="../../../assets/img/products/cart-item-2.webp"
                    alt="Image"
                  />
                  <router-link to="/shop-details">Camera</router-link>
                </div>
              </td>
              <td
                class="bg-transparent shadow-none lh-1 fw-semibold text-purple"
              >
                $11.50
              </td>
              <td class="bg-transparent shadow-none lh-1 fw-semibold text">
                <QuantityCounter />
              </td>
              <td class="bg-transparent shadow-none lh-1 fw-medium text-purple">
                $11.50
              </td>
              <td
                class="bg-transparent shadow-none lh-1 fs-14 fw-semibold text-paragraph"
              >
                <button type="button" class="bg-transparent border-0 p-0">
                  <img
                    src="../../../assets/img/icons/close-2.svg"
                    alt="Image"
                  />
                </button>
              </td>
            </tr>
            <tr>
              <td
                class="bg-transparent shadow-none fw-medium text-black title ps-0"
              >
                <div class="d-flex align-items-center shop-item">
                  <img
                    src="../../../assets/img/products/cart-item-3.webp"
                    alt="Image"
                  />
                  <router-link to="/shop-details">Tablet</router-link>
                </div>
              </td>
              <td class="bg-transparent shadow-none lh-1 fw-medium text-purple">
                $11.50
              </td>
              <td class="bg-transparent shadow-none lh-1 fw-semibold text">
                <QuantityCounter />
              </td>
              <td class="bg-transparent shadow-none lh-1 fw-medium text-purple">
                $11.50
              </td>
              <td
                class="bg-transparent shadow-none lh-1 fs-14 fw-semibold text-paragraph"
              >
                <button type="button" class="bg-transparent border-0 p-0">
                  <img
                    src="../../../assets/img/icons/close-2.svg"
                    alt="Image"
                  />
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="col-xxl-3 col-xl-4 px-xxl-0">
      <div class="sidebar">
        <div class="card border-0 shadow-none">
          <div class="card-body">
            <h4 class="fs-20 fw-semibold p-30 mb-20">Cart Total</h4>
            <form action="#" class="coupon-form position-relative mb-10">
              <input
                type="text"
                placeholder="COUPON CODE"
                class="w-100 bg_ash h-55 fs-12 text-title border-0 rounded-1"
              />
              <button
                type="submit"
                class="position-absolute top-0 end-0 text-white border-0 rounded-1 fw-semibold"
              >
                Apply
              </button>
            </form>
            <div class="table-responsive">
              <table class="table text-nowrap align-middle mb-0">
                <tbody>
                  <tr class="br-0">
                    <td
                      class="shadow-none fw-medium text-black title border-0 pb-0 px-0"
                    >
                      <span
                        class="fw-semibold text-optional d-block fs-14 ls-7"
                      >
                        SUBTOTAL
                      </span>
                    </td>
                    <td
                      class="shadow-none lh-1 fw-semibold text-title border-0 pb-0 px-0"
                    >
                      <span
                        class="fw-semibold text-title d-block fs-14 ls-7 text-end"
                      >
                        $51.00 USD
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <td
                      class="shadow-none fw-medium text-black title pt-2 px-0"
                    >
                      <span
                        class="fw-semibold text-optional d-block fs-14 ls-7"
                      >
                        TOTAL
                      </span>
                    </td>
                    <td
                      class="shadow-none lh-1 fw-semibold text-title pt-2 px-0"
                    >
                      <span
                        class="fw-semibold text-title d-block fs-14 ls-7 text-end"
                      >
                        $51.00 USD
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
              <div class="mt-4 px-xxl-1">
                <button class="btn style-eleven w-100 d-blockfs-14">
                  Proceed To Checkout<img
                    src="../../../assets/img/icons/right-long-arrow.svg"
                    alt="Image"
                  />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import QuantityCounter from "./QuantityCounter.vue";

export default defineComponent({
  name: "CartContent",
  components: {
    QuantityCounter,
  },
});
</script>

<style lang="scss" scoped>
.cart-item {
  img {
    border-radius: 10px;
    margin-right: 15px;
  }
  span {
    &.qty {
      display: block;
    }
  }
}
.cart-table {
  thead {
    tr {
      th {
        font-size: 14px;
        color: #646464;
        font-weight: 600;
        border-bottom: 1px solid #e0e8ee;
      }
    }
  }
  tbody {
    tr {
      &:last-child {
        td {
          border-bottom: 1px solid #e0e8ee;
          padding-bottom: 20px;
        }
      }
      td {
        border-bottom: 1px solid #e0e8ee;
        .shop-item {
          display: flex;
          flex-wrap: wrap;
          align-items: center;
          img {
            border-radius: 10px;
          }
          a {
            color: var(--titleColor);
            font-size: 18px;
            font-weight: 600;
            line-height: 26px;
            margin-left: 33px;
          }
        }
      }
    }
  }
}
.coupon-form {
  position: relative;
  button {
    background-color: #03b288;
    height: 100%;
    padding: 10px 20px;
  }
}

@media only screen and (max-width: 767px) {
  .cart-table {
    .table {
      tbody {
        tr {
          td {
            padding-right: 85px;
            .shop-item {
              flex-wrap: nowrap;
            }
          }
        }
      }
    }
  }
}
</style>