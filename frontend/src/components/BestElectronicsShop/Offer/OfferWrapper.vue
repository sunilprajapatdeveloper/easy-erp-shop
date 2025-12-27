<template>
  <div class="container pt-60 pb-100">
    <div class="offer-wrapper">
      <swiper
        :spaceBetween="24"
        :grabCursor="true"
        :loop="false"
        :navigation="{
          nextEl: '.offer-next',
          prevEl: '.offer-prev',
        }"
        :breakpoints="{
          0: {
            slidesPerView: 1,
          },
          992: {
            slidesPerView: 2,
          },
          1200: {
            slidesPerView: 3,
          },
          1400: {
            slidesPerView: 3,
          },
        }"
        :modules="modules"
        class="offer-slider"
      >
        <swiper-slide v-for="offer in offers" :key="offer.id">
          <div :class="['offer-card', offer.class]">
            <div class="offer-img">
              <img :src="offer.image" alt="Image" />
            </div>
            <div class="offer-content">
              <h6>
                <router-link to="/shop-right-sidebar">
                  Get
                  <span class="fw-bold">{{ offer.percent }}%</span>
                  {{ offer.desc }}
                </router-link>
              </h6>
              <router-link to="/shop-left-sidebar" class="btn style-two">
                Available Offer
              </router-link>
            </div>
          </div>
        </swiper-slide>
      </swiper>
      <div class="slider-btn">
        <div class="offer-prev">
          <img src="../../../assets/img/icons/left-arrow.svg" alt="Image" />
        </div>
        <div class="offer-next">
          <img src="../../../assets/img/icons/right-arrow.svg" alt="Image" />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { Navigation } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/vue";

export default defineComponent({
  name: "OfferWrapper",
  components: {
    Swiper,
    SwiperSlide,
  },
  data() {
    return {
      offers: [
        {
          id: 1,
          class: "style-one",
          image: require("../../../assets/img/products/offer-1.webp"),
          percent: "20",
          desc: "Discounts On The Gaming Products",
        },
        {
          id: 2,
          class: "style-two",
          image: require("../../../assets/img/products/offer-2.webp"),
          percent: "15",
          desc: "Cashback On Brand New Laptop",
        },
        {
          id: 3,
          class: "style-three",
          image: require("../../../assets/img/products/offer-3.webp"),
          percent: "30",
          desc: "Off on Apple Combo Products",
        },
        {
          id: 4,
          class: "style-one",
          image: require("../../../assets/img/products/offer-1.webp"),
          percent: "20",
          desc: "Discounts On The Gaming Products",
        },
      ],
    };
  },
  setup() {
    return {
      modules: [Navigation],
    };
  },
});
</script>

<style lang="scss" scoped>
.offer-card {
  border-radius: 10px;
  padding: 30px 20px;
  position: relative;
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;
  justify-content: center;
  min-height: 260px;
  &.style-one {
    background: linear-gradient(180deg, #8fdeff 0%, #91deff 50.52%);
  }
  &.style-two {
    background: linear-gradient(
      119deg,
      #d9d8e6 22.05%,
      #d0d2e1 50.57%,
      #c2c5d8 98.4%
    );
  }
  &.style-three {
    background: linear-gradient(
      181deg,
      #e5d8cf 1.71%,
      #cbb9af 37.01%,
      #bdaa9e 54.79%,
      #ae9b8c 73.24%,
      #cbbab0 96.23%
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
.offer-wrapper {
  position: relative;
  .slider-btn {
    .offer-prev,
    .offer-next {
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      display: flex;
      flex-wrap: wrap;
      flex-direction: column;
      justify-content: center;
      width: 60px;
      height: 60px;
      border-radius: 50%;
      background-color: var(--whiteColor);
      box-shadow: 0px 2px 20px rgba(0, 0, 0, 0.06);
      z-index: 2;
      transition: var(--transition);
      cursor: pointer;
      img {
        display: block;
        margin: 0 auto;
        transition: var(--transition);
      }
      &:hover {
        background-color: var(--secondaryColor);
        img {
          filter: brightness(0) invert(1);
        }
      }
    }
    .offer-prev {
      left: 0;
    }
    .offer-next {
      right: 0;
    }
  }
}

@media only screen and (max-width: 767px) {
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
  .offer-wrapper {
    .slider-btn {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      .offer-prev,
      .offer-next {
        position: relative;
        top: auto;
        justify-content: center;
        margin-top: 30px;
        width: 49px;
        height: 49px;
        transform: translateY(0);
      }
      .offer-prev {
        left: auto;
      }
      .offer-next {
        right: -10px;
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
  .offer-wrapper {
    .slider-btn {
      .offer-prev {
        left: -20px;
      }
      .offer-next {
        right: -20px;
      }
    }
  }
  .offer-card {
    .offer-img {
      max-width: 130px;
    }
  }
}

@media only screen and (min-width: 1300px) and (max-width: 1399px) {
  .offer-wrapper {
    .slider-btn {
      .offer-prev {
        left: -50px;
      }
      .offer-next {
        right: -50px;
      }
    }
  }
}

@media only screen and (min-width: 1400px) {
  .offer-wrapper {
    .slider-btn {
      .offer-prev,
      .offer-next {
        top: 50%;
      }
      .offer-prev {
        left: -35px;
      }
      .offer-next {
        right: -35px;
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