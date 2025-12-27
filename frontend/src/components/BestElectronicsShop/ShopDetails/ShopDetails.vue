<template>
  <div class="row ptb-100 align-items-center">
    <div class="col-lg-6">
      <div class="single-product-item">
        <img
          src="../../../assets/img/products/single-product-2.webp"
          alt="Image"
        />
        <span>New</span>
      </div>
    </div>
    <div class="col-lg-6">
      <div class="single-product-details">
        <div class="d-flex align-items-center justify-content-between">
          <span class="fs-14 fw-semibold text-bg">LAPTOP</span>
          <div class="ratings d-flex align-items-center">
            <ul class="list-style d-flex lh-1">
              <li>
                <img src="../../../assets/img/icons/star.svg" alt="Image" />
              </li>
              <li>
                <img src="../../../assets/img/icons/star.svg" alt="Image" />
              </li>
              <li>
                <img src="../../../assets/img/icons/star.svg" alt="Image" />
              </li>
              <li>
                <img src="../../../assets/img/icons/star.svg" alt="Image" />
              </li>
              <li>
                <img src="../../../assets/img/icons/star.svg" alt="Image" />
              </li>
            </ul>
            <span class="fs-14 text-bg lh-1 ms-2 mt-1">4.9</span>
          </div>
        </div>
        <h1 class="text-title fw-bold">Sanung Laptop 14”, 8gb ram, 1tb ssd</h1>
        <h3 class="product-price fw-bold text-green">$3210.00</h3>
        <p>
          Your willingness to feel better will be your guide out of despair and
          joy. Feeling your feelings is essential to moving up the scale.
        </p>
        <div class="add-to-cart d-flex flex-wrap align-items-center">
          <div class="v-counter style-three bg_ash">
            <button class="plusBtn" @click="increment"></button>
            <input
              type="text"
              size="25"
              :value="quantity"
              class="count bg_ash"
            />
            <button class="minusBtn" @click="decrement"></button>
          </div>
          <button class="btn style-eleven">Add To Cart</button>
          <button class="btn style-tweleve">
            Add To Wishlist
            <img src="../../../assets/img/icons/heart.svg" alt="Image" />
          </button>
        </div>
        <ul class="product-features list-style">
          <li class="text-bg fw-semibold fs-14">
            SKU : <span class="fw-medium">125J31</span>
          </li>
          <li class="text-bg fw-semibold fs-14">
            CATEGORY : <span class="fw-medium">Books, Happiness</span>
          </li>
          <li class="text-bg fw-semibold fs-14">
            AVAILABILITY : <span class="fw-medium text-green">instock</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
  <ul class="nav nav-tabs table-tablist mb-40" role="tablist">
    <li class="nav-item">
      <button
        class="nav-link active"
        data-bs-toggle="tab"
        data-bs-target="#tab_1"
        type="button"
        role="tab"
      >
        Description
      </button>
    </li>
    <li class="nav-item">
      <button
        class="nav-link"
        data-bs-toggle="tab"
        data-bs-target="#tab_2"
        type="button"
        role="tab"
      >
        Reviews
      </button>
    </li>
  </ul>
  <div class="tab-content product-tab-content">
    <div class="tab-pane fade show active" id="tab_1" role="tabpanel">
      <div class="shop-desc row">
        <div class="col-lg-7">
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus in
            magna ac tellus fringilla eleifend. Mauris et velit risus. Phasellus
            nisi ipsum, fermentum eget consequat non, molestie at augue. Proin
            rutrum sem a rutrum ultricies.
          </p>
          <p>
            Nunc felis neque, dictum ut porta a, ullamcorper vel ante. Quisque
            non consequat ligula. Pellentesque porttitor eu nisi eu bibendum. In
            non faucibus justo, et viverra lectus. Fusce sodales mauris et velit
            accumsan vulputate. Lorem ipsum dolor sit ame.
          </p>
        </div>
        <div class="col-lg-5">
          <div class="offer-card style-two">
            <div class="offer-img">
              <img
                src="../../../assets/img/products/offer-2.webp"
                alt="Image"
              />
            </div>
            <div class="offer-content">
              <h6>
                <router-link to="/shop-right-sidebar">
                  Get <span>15%</span>
                  Cashback On Brand New Laptop
                </router-link>
              </h6>
              <router-link to="/shop-left-sidebar" class="btn style-two">
                Available Offer
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="tab-pane fade" id="tab_2" role="tabpanel">
      <div class="row">
        <div class="col-lg-7">
          <div class="client-reviews">
            <h6 class="fs-24 text-title mb-30">01 Reviews</h6>
            <div class="review-item d-flex">
              <div class="client-img">
                <img src="../../../assets/img/users/client.webp" alt="Image" />
              </div>
              <div class="review-info">
                <div
                  class="d-flex flex-wrap justify-content-between align-items-center"
                >
                  <h6>Jack Smith <span>Sep 09, 2023 at 7:41 pm</span></h6>
                  <div class="ratings d-flex align-items-center">
                    <ul class="list-style d-flex lh-1">
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                    </ul>
                    <span class="fs-14 text-bg lh-1 ms-2 mt-1">4.9</span>
                  </div>
                </div>
                <p class="mb-0">
                  Your willingness to feel better will be your guide out of
                  despair and into joy. Feeling your feelings is essential to
                  moving up the scale. Often when we’re stuck in an emotion, the
                  reason we can’t get out of it is that we’re unwilling to
                  acknowledge
                </p>
              </div>
            </div>
            <div class="review-item d-flex">
              <div class="client-img">
                <img src="../../../assets/img/users/client.webp" alt="Image" />
              </div>
              <div class="review-info">
                <div
                  class="d-flex flex-wrap justify-content-between align-items-center"
                >
                  <h6>Alex Denver <span>Sep 12, 2023 at 10:41 am</span></h6>
                  <div class="ratings d-flex align-items-center">
                    <ul class="list-style d-flex lh-1">
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                      <li>
                        <img
                          src="../../../assets/img/icons/star.svg"
                          alt="Image"
                        />
                      </li>
                    </ul>
                    <span class="fs-14 text-bg lh-1 ms-2 mt-1">4.9</span>
                  </div>
                </div>
                <p class="mb-0">
                  Your willingness to feel better will be your guide out of
                  despair and into joy. Feeling your feelings is essential to
                  moving up the scale. Often when we’re stuck in an emotion, the
                  reason we can’t get out of it is that we’re unwilling to
                  acknowledge
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "ShopDetails",
  setup() {
    const quantity = ref(1);

    const increment = () => {
      quantity.value++;
    };

    const decrement = () => {
      if (quantity.value > 1) {
        quantity.value--;
      }
    };

    return {
      quantity,
      increment,
      decrement,
    };
  },
});
</script>

<style lang="scss" scoped>
.single-product-details {
  .ratings {
    li {
      margin-right: 3px;
    }
  }
  h1 {
    color: var(--titleColor);
    font-size: 46px;
    font-weight: 700;
    line-height: 60px;
    margin-bottom: 18px;
    letter-spacing: -2.3px;
  }
  .product-price {
    font-size: 24px;
    margin-bottom: 22px;
  }
  p {
    margin-bottom: 25px;
  }
  .add-to-cart {
    .btn {
      padding: 12.5px 22px;
      &.style-eleven {
        background: #6366f1;
        margin: 0 15px;
      }
      &.style-tweleve {
        padding: 12.5px 5px;
      }
    }
    .v-counter {
      background-color: var(--ashColor);
      &.style-three {
        input {
          background-color: #cdcedf;
        }
        .plusBtn,
        .minusBtn {
          &:before {
            top: 14px;
          }
        }
      }
    }
  }
  .product-features {
    margin-top: 30px;
    li {
      letter-spacing: 1.4px;
      margin-bottom: 6px;
      span {
        letter-spacing: 0;
      }
    }
  }
}
.single-product-item {
  position: relative;
  span {
    position: absolute;
    top: 20px;
    left: 20px;
    background-color: #635bff;
    font-size: 13px;
    font-weight: 700;
    border-radius: 4px;
    line-height: 1;
    padding: 7px 10px;
    color: var(--whiteColor);
  }
  img {
    border-radius: 20px;
  }
}
.client-reviews {
  .review-item {
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    margin-bottom: 20px;
    .client-img {
      width: 70px;
      border-radius: 5px;
      img {
        border-radius: 5px;
      }
    }
    .review-info {
      width: calc(100% - 90px);
      margin-left: 20px;
      h6 {
        color: var(--titleColor);
        font-size: 16px;
        font-weight: 600;
        line-height: 24px;
        letter-spacing: -0.32px;
        margin: 0;
        span {
          color: #646464;
          font-size: 14px;
          font-weight: 400;
          line-height: 14px;
          margin-left: 10px;
        }
      }
      p {
        margin: 15px 0 0;
      }
    }
  }
}
.offer-card {
  border-radius: 10px;
  padding: 30px 20px;
  position: relative;
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;
  justify-content: center;
  min-height: 260px;
  &.style-two {
    background: linear-gradient(
      119deg,
      #d9d8e6 22.05%,
      #d0d2e1 50.57%,
      #c2c5d8 98.4%
    );
  }
  .offer-content {
    padding-right: 80px;
    position: relative;
    z-index: 1;
    h6 {
      font-size: 20px;
      font-weight: 600;
      line-height: 28px;
      margin-bottom: 21px;
      span {
        font-weight: 900;
      }
      a {
        color: var(--titleColor);
      }
    }
    .btn {
      font-size: 16px;
      padding: 11px 20px;
      &.style-two {
        border: none;
        background-color: var(--whiteColor);
        color: var(--secondaryColor);
        border-radius: 3px;
      }
      &.style-three {
        border: none;
        background-color: var(--titleColor);
        color: var(--whiteColor);
        border-radius: 3px;
        &:hover {
          background-color: var(--secondaryColor);
        }
      }
      &:hover {
        color: var(--whiteColor);
      }
    }
  }
  .offer-img {
    position: absolute;
    top: 50%;
    right: 5px;
    transform: translateY(-50%);
  }
}

@media only screen and (max-width: 767px) {
  .single-product-details {
    h1 {
      font-size: 32px;
      line-height: 42px;
      letter-spacing: -1.3px;
    }
    .product-price {
      font-size: 20px;
      margin-bottom: 15px;
    }
    p {
      margin-bottom: 20px;
    }
    .add-to-cart {
      .btn {
        padding: 12.5px 22px;
        &.style-eleven {
          background: #6366f1;
          margin: 0 15px 10px 0;
        }
        &.style-tweleve {
          padding: 12.5px 5px;
          margin: 0 0px 10px 0;
        }
      }
      .v-counter {
        margin: 0 15px 10px 0;
      }
    }
    .product-features {
      margin-top: 20px;
    }
  }
  .offer-card {
    .offer-img {
      position: relative;
      top: auto;
      transform: translateY(0);
      margin: 0 auto;
    }
    .offer-content {
      text-align: center;
      padding-right: 0;
      margin-top: 20px;
    }
  }
}

@media only screen and (max-width: 991px) {
  .single-product-item {
    margin-bottom: 30px;
  }
  .client-reviews {
    .review-item {
      margin-bottom: 20px;
      .review-info {
        width: 100%;
        margin: 20px 0 0;
        h6 {
          width: 100%;
          span {
            color: #646464;
            font-size: 14px;
            font-weight: 400;
            line-height: 14px;
            margin-left: 10px;
          }
        }
        .ratings {
          width: 100%;
          margin: 10px 0 0;
        }
        p {
          margin: 15px 0 0;
        }
      }
    }
  }
}

@media only screen and (min-width: 992px) and (max-width: 1199px) {
  .offer-card {
    .offer-img {
      max-width: 130px;
    }
  }
}

@media only screen and (min-width: 1200px) and (max-width: 1399px) {
  .offer-card {
    .offer-img {
      max-width: 130px;
    }
  }
}

@media only screen and (min-width: 1400px) {
  .single-product-details {
    .add-to-cart {
      .v-counter {
        background-color: var(--ashColor);
      }
      .btn {
        &.style-eleven {
          padding: 12.5px 42px;
        }
      }
    }
  }
  .offer-card {
    padding: 30px 40px;
    &.style-one {
      .offer-content {
        padding-right: 155px;
      }
    }
    &.style-two {
      .offer-content {
        padding-right: 155px;
      }
    }
    &.style-three {
      .offer-content {
        padding-right: 155px;
      }
    }
  }
}
</style>