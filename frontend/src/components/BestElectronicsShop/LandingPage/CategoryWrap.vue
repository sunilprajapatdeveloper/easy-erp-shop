<template>
  <div class="category-wrap mt-30">
    <div class="category-slider">
      <swiper
        :spaceBetween="15"
        :slidesPerView="1"
        :grabCursor="true"
        :preventClicks="true"
        :loop="true"
        :autoplay="{
          delay: 4000,
          disableOnInteraction: false,
          pauseOnMouseEnter: true,
        }"
        :navigation="{
          nextEl: '.category-prev',
          prevEl: '.category-next',
        }"
        :breakpoints="{
          0: {
            slidesPerView: 1,
          },
          768: {
            slidesPerView: 2,
          },
          992: {
            slidesPerView: 4,
          },
          1200: {
            slidesPerView: 5,
          },
          1400: {
            slidesPerView: 6,
          },
          1600: {
            slidesPerView: 7,
          },
        }"
        :modules="modules"
        class="category-slider"
      >
        <swiper-slide v-for="category in categorys" :key="category.id">
          <div class="ctegory-card">
            <div class="cat-img">
              <img :src="category.image" alt="Image" />
            </div>
            <div
              class="cat-info d-flex justify-content-between align-items-center"
            >
              <h6 class="fs-18">
                <router-link to="/shop-details">
                  {{ category.title }}
                </router-link>
              </h6>
              <span>({{ category.items }} Items)</span>
            </div>
          </div>
        </swiper-slide>
      </swiper>
    </div>
    <div class="slider-btn">
      <div class="category-prev">
        <img src="../../../assets/img/icons/left-arrow.svg" alt="Image" />
      </div>
      <div class="category-next">
        <img src="../../../assets/img/icons/right-arrow.svg" alt="Image" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { Autoplay, Navigation } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/vue";

export default defineComponent({
  name: "CategoryWrap",
  components: {
    Swiper,
    SwiperSlide,
  },
  data() {
    return {
      categorys: [
        {
          id: 1,
          image: require("../../../assets/img/category/category-1.webp"),
          title: "Tablet",
          items: "36",
        },
        {
          id: 2,
          image: require("../../../assets/img/category/category-2.webp"),
          title: "Laptop",
          items: "21",
        },
        {
          id: 3,
          image: require("../../../assets/img/category/category-3.webp"),
          title: "iMac",
          items: "54",
        },
        {
          id: 4,
          image: require("../../../assets/img/category/category-4.webp"),
          title: "Drone",
          items: "46",
        },
        {
          id: 5,
          image: require("../../../assets/img/category/category-5.webp"),
          title: "Earbuds",
          items: "114",
        },
        {
          id: 6,
          image: require("../../../assets/img/category/category-6.webp"),
          title: "Camera",
          items: "126",
        },
        {
          id: 7,
          image: require("../../../assets/img/category/category-7.webp"),
          title: "Accessories",
          items: "43",
        },
        {
          id: 8,
          image: require("../../../assets/img/category/category-1.webp"),
          title: "Drone",
          items: "46",
        },
        {
          id: 9,
          image: require("../../../assets/img/category/category-5.webp"),
          title: "Earbuds",
          items: "114",
        },
      ],
    };
  },
  setup() {
    return {
      Autoplay,
      modules: [Navigation],
    };
  },
});
</script>

<style lang="scss" scoped>
.ctegory-card {
  img {
    border-radius: 5px;
    margin-bottom: 18px;
  }
  h6 {
    margin: 0;
    a {
      color: var(--titleColor);
      &:hover {
        color: var(--secondaryColor);
      }
    }
  }
}
.category-wrap {
  position: relative;
  .slider-btn {
    .category-prev,
    .category-next {
      position: absolute;
      top: 42%;
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
    .category-prev {
      left: 0;
    }
    .category-next {
      right: 0;
    }
  }
}

@media only screen and (min-width: 1400px) {
  .category-wrap {
    padding: 0 50px;
    .slider-btn {
      .category-prev,
      .category-next {
        top: 50%;
      }
      .category-prev {
        left: 20px;
      }
      .category-next {
        right: 20px;
      }
    }
  }
}
</style>