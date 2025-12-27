<template>
  <div class="card-body">
    <h4 class="fs-20 fw-semibold p-30">Your Order</h4>
    <div class="table-responsive">
      <table class="table text-nowrap align-middle mb-0">
        <tbody>
          <tr>
            <td class="shadow-none fw-medium text-black title">
              <div class="d-flex align-items-center cart-item">
                <img
                  src="../../../assets/img/products/cart-item-1.webp"
                  alt="Image"
                />
                <span>
                  Laptop
                  <span class="qty">X1</span>
                </span>
              </div>
            </td>
            <td class="shadow-none lh-1 fw-semibold text-title text-end">
              $51.50
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-medium text-black title">
              <div class="d-flex align-items-center cart-item">
                <img
                  src="../../../assets/img/products/cart-item-2.webp"
                  alt="Image"
                />
                <span>
                  Camera
                  <span class="qty">X1</span>
                </span>
              </div>
            </td>
            <td class="shadow-none lh-1 fw-semibold text-title text-end">
              $51.50
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-medium text-black title">
              <div class="d-flex align-items-center cart-item">
                <img
                  src="../../../assets/img/products/cart-item-3.webp"
                  alt="Image"
                />
                <span>
                  Tablet
                  <span class="qty">X1</span>
                </span>
              </div>
            </td>
            <td class="shadow-none lh-1 fw-semibold text-title text-end">
              $51.50
            </td>
          </tr>
          <tr class="br-0">
            <td class="shadow-none fw-medium text-black title border-0 pb-0">
              <span class="fw-semibold text-optional d-block fs-14 ls-7">
                SUBTOTAL
              </span>
            </td>
            <td class="shadow-none lh-1 fw-semibold text-title border-0 pb-0">
              <span class="fw-semibold text-title d-block fs-14 ls-7 text-end">
                $51.00 USD
              </span>
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-medium text-black title pt-2">
              <span class="fw-semibold text-optional d-block fs-14 ls-7">
                SHIPPING
              </span>
            </td>
            <td class="shadow-none lh-1 fw-semibold text-title pt-2">
              <span class="fw-semibold text-title d-block fs-14 ls-7 text-end">
                $51.00 USD
              </span>
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-medium text-black title pt-2">
              <span class="fw-semibold text-optional d-block fs-14 ls-7">
                TOTAL
              </span>
            </td>
            <td class="shadow-none lh-1 fw-semibold text-title pt-2">
              <span class="fw-semibold text-title d-block fs-14 ls-7 text-end">
                $51.00 USD
              </span>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="px-4 mx-1">
        <button class="btn style-one w-100 d-block">
          Place Order Now
          <img
            src="../../../assets/img/icons/right-long-arrow.svg"
            alt="Image"
          />
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "YourOrder",
};
</script>

<style lang="scss" scoped>
.table-responsive {
  margin: 0 -30px;
  table {
    tr {
      td {
        padding: 20px 30px;
      }
    }
  }
}
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
</style>