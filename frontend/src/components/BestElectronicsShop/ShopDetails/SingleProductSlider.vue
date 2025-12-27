<template>
  <div class="single-product-slider-wrap">
    <swiper
      :slidesPerView="1"
      :loop="true"
      :spaceBetween="10"
      :thumbs="{ swiper: thumbsSwiper }"
      :modules="[Thumbs]"
      class="single-product-slider"
    >
      <swiper-slide
        class="single-product-item"
        v-for="product in products"
        :key="product.id"
      >
        <img :src="product.image" alt="Image" />
        <span>{{ product.tag }}</span>
      </swiper-slide>
    </swiper>
    <swiper
      :loop="true"
      :spaceBetween="10"
      :slidesPerView="4"
      :freeMode="true"
      :watchSlidesProgress="true"
      :breakpoints="{
        0: {
          slidesPerView: 3,
          direction: 'horizontal',
        },
        768: {
          slidesPerView: 4,
          direction: 'vertical',
        },
        992: {
          slidesPerView: 4,
          direction: 'vertical',
        },
        1200: {
          slidesPerView: 4,
          direction: 'vertical',
        },
      }"
      :modules="[Thumbs]"
      @swiper="setThumbsSwiper"
      class="single-product-thumb"
    >
      <swiper-slide
        class="single-product-item"
        v-for="product in products"
        :key="product.id"
      >
        <img :src="product.image" alt="Image" />
      </swiper-slide>
    </swiper>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { Swiper, SwiperSlide } from "swiper/vue";
import { Thumbs } from "swiper/modules";

import "swiper/css/free-mode";
import "swiper/css/thumbs";

export default defineComponent({
  name: "SingleProductSlider",
  components: {
    Swiper,
    SwiperSlide,
  },
  data() {
    return {
      products: [
        {
          id: 1,
          image: require("../../../assets/img/products/single-product-2.webp"),
          tag: "New",
        },
        {
          id: 2,
          image: require("../../../assets/img/products/single-product-3.webp"),
          tag: "Hot",
        },
        {
          id: 3,
          image: require("../../../assets/img/products/single-product-4.webp"),
          tag: "New",
        },
        {
          id: 4,
          image: require("../../../assets/img/products/single-product-5.webp"),
          tag: "New",
        },
      ],
    };
  },
  setup() {
    const thumbsSwiper = ref(null);
    const setThumbsSwiper = (swiper: null) => {
      thumbsSwiper.value = swiper;
    };
    return {
      Thumbs,
      thumbsSwiper,
      setThumbsSwiper,
    };
  },
});
</script>

<style lang="scss" scoped>
.single-product-slider-wrap {
  display: flex;
  flex-wrap: wrap;
  .single-product-thumb {
    width: 110px;
    .swiper-wrapper {
      height: 505px;
    }
    .single-product-item {
      display: flex;
      flex-wrap: wrap;
      flex-direction: column;
      justify-content: center;
      border-radius: 20px;
      cursor: pointer;
      img {
        display: block;
        border-radius: 20px;
        margin: 0 auto;
      }
    }
  }
  .single-product-slider {
    width: calc(100% - 134px);
    margin-right: 24px;
  }
}
.single-product-slider {
  position: relative;
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;
  justify-content: center;
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
  }
  .single-product-prev {
    left: 10px;
  }
  .single-product-next {
    right: 10px;
  }
  .single-product-next,
  .single-product-prev {
    position: absolute;
    top: 50%;
    z-index: 1;
    cursor: pointer;
    transform: translateY(-50%);
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background-color: rgba(255, 255, 255, 1);
    transition: var(--transition);
    img {
      max-width: 20px;
      display: block;
      margin: 0 auto;
      transition: var(--transition);
    }
    &:hover {
      background-color: var(--optionalColor);
      i {
        color: var(--whiteColor);
      }
      img {
        filter: brightness(0) invert(1);
      }
    }
  }
  .single-product-item {
    border-radius: 20px;
    img {
      border-radius: 20px;
      display: block;
      margin: 0 auto;
    }
  }
}

@media only screen and (max-width: 767px) {
  .single-product-slider-wrap {
    display: block;
    .single-product-thumb {
      width: 100%;
      margin-top: 20px;
      .swiper-wrapper {
        height: auto;
      }
    }
    .single-product-slider {
      width: 100%;
      margin-right: 0;
    }
  }
}
@media only screen and (max-width: 991px) {
  .single-product-slider-wrap {
    margin-bottom: 30px;
  }
}
@media only screen and (min-width: 1400px) {
  .single-product-slider-wrap {
    width: calc(100% - 5px);
  }
}
</style>