<template>
  <div class="container ptb-100">
    <h2 class="section-title mb-40 text-center text-dark">
      Browse All Products
    </h2>
    <div class="product-card-wrap d-flex flex-wrap">
      <div
        class="product-card"
        v-for="product in productsLoaded"
        :key="product.id"
      >
        <router-link to="/shop-details">
          <img :src="product.image" alt="Image" />
        </router-link>
        <div class="d-flex justify-content-between align-items-center">
          <div class="product-info">
            <h3 class="fs-18 lh-1">
              <router-link to="/shop-details">
                {{ product.title }}
              </router-link>
            </h3>
            <span>${{ product.price }}</span>
          </div>
          <router-link
            to="/cart"
            class="add-to-cart d-flex flex-column justify-content-center align-items-center transition bg-white"
          >
            <img
              src="../../../assets/img/icons/shopping-cart.svg"
              alt="Image"
            />
          </router-link>
        </div>
      </div>
    </div>
    <div class="text-center mt-2">
      <button @click="loadMore" class="btn style-one px-xxl-5">
        Load More Products
      </button>
      <div v-if="showAlert" class="alert alert-success mt-3">
        {{ alertMessage }}
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

interface ProductsContent {
  products: Array<{ id: number; image: string; title: string; price: string }>;
  length: number;
  showAlert: boolean;
  alertMessage: string;
}

export default defineComponent({
  name: "BrowseAllProducts",
  data() {
    return {
      products: [
        {
          id: 1,
          image: require("../../../assets/img/products/product-2.webp"),
          title: "Gaming Laptop",
          price: "540",
        },
        {
          id: 2,
          image: require("../../../assets/img/products/product-1.webp"),
          title: "VR Box",
          price: "234",
        },
        {
          id: 3,
          image: require("../../../assets/img/products/product-3.webp"),
          title: "Gaming VR",
          price: "123",
        },
        {
          id: 4,
          image: require("../../../assets/img/products/product-4.webp"),
          title: "Gaming Headphone",
          price: "240",
        },
        {
          id: 5,
          image: require("../../../assets/img/products/product-5.webp"),
          title: "Joy Stick",
          price: "125",
        },
        {
          id: 6,
          image: require("../../../assets/img/products/product-6.webp"),
          title: "Smart Phone",
          price: "120",
        },
        {
          id: 7,
          image: require("../../../assets/img/products/product-7.webp"),
          title: "Gaming Joy Stick",
          price: "40",
        },
        {
          id: 8,
          image: require("../../../assets/img/products/product-8.webp"),
          title: "Gaming VR",
          price: "550",
        },
        {
          id: 9,
          image: require("../../../assets/img/products/product-9.webp"),
          title: "Smart Watch",
          price: "520",
        },
        {
          id: 10,
          image: require("../../../assets/img/products/product-10.webp"),
          title: "Drone",
          price: "230",
        },
        {
          id: 11,
          image: require("../../../assets/img/products/product-11.webp"),
          title: "Smart Phone",
          price: "540",
        },
        {
          id: 12,
          image: require("../../../assets/img/products/product-12.webp"),
          title: "Tablet PC",
          price: "177",
        },
        {
          id: 13,
          image: require("../../../assets/img/products/product-13.webp"),
          title: "Gaming Joy Stick",
          price: "133",
        },
        {
          id: 14,
          image: require("../../../assets/img/products/product-14.webp"),
          title: "Playstation",
          price: "312",
        },
        {
          id: 15,
          image: require("../../../assets/img/products/product-15.webp"),
          title: "Camera",
          price: "300",
        },
        {
          id: 16,
          image: require("../../../assets/img/products/product-16.webp"),
          title: "Pendrive",
          price: "540",
        },
        {
          id: 17,
          image: require("../../../assets/img/products/product-17.webp"),
          title: "Gaming Laptop",
          price: "250",
        },
        {
          id: 18,
          image: require("../../../assets/img/products/product-18.webp"),
          title: "Smart Watch",
          price: "270",
        },
        {
          id: 19,
          image: require("../../../assets/img/products/product-19.webp"),
          title: "Macbook",
          price: "170",
        },
        {
          id: 20,
          image: require("../../../assets/img/products/product-20.webp"),
          title: "Smart Watch",
          price: "225",
        },
        {
          id: 21,
          image: require("../../../assets/img/products/product-21.webp"),
          title: "Laptop",
          price: "340",
        },
        {
          id: 22,
          image: require("../../../assets/img/products/product-22.webp"),
          title: "TV",
          price: "55",
        },
        {
          id: 23,
          image: require("../../../assets/img/products/product-23.webp"),
          title: "VR Box",
          price: "135",
        },
        {
          id: 24,
          image: require("../../../assets/img/products/product-24.webp"),
          title: "Magic Pen",
          price: "230",
        },
        {
          id: 25,
          image: require("../../../assets/img/products/product-25.webp"),
          title: "Video Camera",
          price: "324",
        },
      ],
      length: 20,
      showAlert: false,
      alertMessage: "All Products Loaded",
    };
  },
  methods: {
    loadMore() {
      if (this.length >= this.products.length) {
        this.showAlert = true;
        this.alertMessage = "All products have been loaded!";
        return;
      }

      this.length = this.length + 5;
    },
  },
  computed: {
    productsLoaded(this: ProductsContent): Array<{
      id: number;
      image: string;
      title: string;
      price: string;
    }> {
      return this.products.slice(0, this.length);
    },
  },
});
</script>

<style lang="scss" scoped>
.product-card-wrap {
  margin: 0 -7.5px;
  .product-card {
    margin: 0 7.5px 25px;
  }
}
.product-card {
  margin: 0 7.5px 30px;
  & > a {
    img {
      border-radius: 5px;
      margin: 0 0 24px;
    }
  }
  .add-to-cart {
    width: 46px;
    height: 46px;
    border-radius: 50%;
    background-color: var(--whiteColor);
    transition: var(--transition);
    img {
      display: block;
      margin: 0 auto;
      transition: var(--transition);
    }
    &:hover {
      background-color: var(--secondaryColor) !important;
      img {
        filter: brightness(0) invert(1);
      }
    }
  }
  .product-info {
    h3 {
      font-size: 18px;
      line-height: 26px;
      font-weight: 800;
      margin-bottom: 13px;
      a {
        color: var(--primaryColor);
        &:hover {
          color: var(--secondaryColor);
        }
      }
    }
    span {
      color: #5855eb;
      font-weight: 500;
    }
  }
}

@media only screen and (max-width: 767px) {
  .product-card-wrap {
    margin: 0;
    .product-card {
      width: 100%;
      margin: 0 0 25px;
    }
  }
}

@media only screen and (min-width: 768px) and (max-width: 991px) {
  .product-card-wrap {
    .product-card {
      width: calc(50% - 15px);
    }
  }
}

@media only screen and (min-width: 992px) and (max-width: 1199px) {
  .product-card-wrap {
    .product-card {
      width: calc(33.33% - 15px);
    }
  }
}

@media only screen and (min-width: 1200px) {
  .product-card-wrap {
    .product-card {
      width: calc(25% - 15px);
    }
  }
}

@media only screen and (min-width: 1400px) {
  .product-card-wrap {
    .product-card {
      width: calc(20% - 15px);
      margin: 0 7.5px 40px;
    }
  }
}
</style>