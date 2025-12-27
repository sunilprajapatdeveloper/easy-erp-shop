<template>
  <div class="pt-100">
    <div class="table-responsive wishlist-table">
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
              ADD TO CART
            </th>
            <th
              scope="col"
              class="text-title fw-semibold fs-14 pt-0 bg-transparent text-end"
            >
              DELETE
            </th>
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
            <td class="bg-transparent shadow-none lh-1 fw-semibold text-purple">
              $11.50
            </td>
            <td class="bg-transparent shadow-none lh-1 fw-semibold text">
              <button class="btn style-eleven">Add To cart</button>
            </td>
            <td
              class="bg-transparent shadow-none lh-1 fs-14 fw-semibold text-paragraph text-end"
            >
              <button type="button" class="bg-transparent border-0 p-0">
                <img src="../../../assets/img/icons/close-2.svg" alt="Image" />
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
            <td class="bg-transparent shadow-none lh-1 fw-semibold text-purple">
              $11.50
            </td>
            <td class="bg-transparent shadow-none lh-1 fw-semibold text">
              <button class="btn style-eleven">Add To cart</button>
            </td>
            <td
              class="bg-transparent shadow-none lh-1 fs-14 fw-semibold text-paragraph text-end"
            >
              <button type="button" class="bg-transparent border-0 p-0">
                <img src="../../../assets/img/icons/close-2.svg" alt="Image" />
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
              <button class="btn style-eleven">Add To cart</button>
            </td>
            <td
              class="bg-transparent shadow-none lh-1 fs-14 fw-semibold text-paragraph text-end"
            >
              <button type="button" class="bg-transparent border-0 p-0">
                <img src="../../../assets/img/icons/close-2.svg" alt="Image" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: "WishlistContent",
};
</script>

<style lang="scss" scoped>
.wishlist-table {
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

@media only screen and (max-width: 767px) {
  .wishlist-table {
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