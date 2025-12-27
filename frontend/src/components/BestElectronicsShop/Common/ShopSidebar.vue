<template>
  <div class="sidebar">
    <div class="sidebar-widget">
      <h3 class="sidebar-widget-title">Search Product</h3>
      <form action="#" class="search-area position-relative w-sm-100">
        <input
          type="search"
          placeholder="Search Product"
          class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black"
        />
        <button
          type="submit"
          class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-3"
        >
          <img src="../../../assets/img/icons/search.svg" alt="Image" />
        </button>
      </form>
    </div>
    <div class="sidebar-widget">
      <h3 class="sidebar-widget-title">Categories</h3>
      <ul class="category-list list-style">
        <li>
          <router-link to="/shop-left-sidebar">
            Electronics <span>(12)</span>
          </router-link>
        </li>
        <li>
          <router-link to="/shop-left-sidebar">
            Mockup<span>(12)</span>
          </router-link>
        </li>
        <li>
          <router-link to="/shop-left-sidebar">
            Laptop<span>(12)</span>
          </router-link>
        </li>
        <li>
          <router-link to="/shop-left-sidebar">
            Camera<span>(12)</span>
          </router-link>
        </li>
        <li>
          <router-link to="/shop-left-sidebar">
            Computer<span>(12)</span>
          </router-link>
        </li>
        <li>
          <router-link to="/shop-left-sidebar">
            Smart Phone<span>(12)</span>
          </router-link>
        </li>
        <li>
          <router-link to="/shop-left-sidebar">
            Design<span>(12)</span>
          </router-link>
        </li>
      </ul>
    </div>
    <div class="sidebar-widget">
      <h3 class="sidebar-widget-title">Filter By Price</h3>
      <div class="center">
        <div class="multi-range">
          <input
            id="min"
            type="range"
            :min="minRange"
            :max="maxRange"
            v-model="minValue"
          />
          <input
            id="max"
            type="range"
            :min="minRange"
            :max="maxRange"
            v-model="maxValue"
          />
        </div>
        <div class="price-title d-flex flex-wrap justify-content-between">
          <span id="from" class="fs-14 text-optional"> ${{ minValue }} </span>
          <span id="to" class="fs-14 text-optional"> ${{ maxValue }} </span>
        </div>
      </div>
    </div>
    <div class="sidebar-widget">
      <h3 class="sidebar-widget-title">Tags</h3>
      <ul class="tag-list list-style">
        <li><router-link to="/shop-left-sidebar">Electronics</router-link></li>
        <li><router-link to="/shop-left-sidebar">Sale</router-link></li>
        <li><router-link to="/shop-left-sidebar">New</router-link></li>
        <li><router-link to="/shop-left-sidebar">Popular</router-link></li>
        <li><router-link to="/shop-left-sidebar">Earphone</router-link></li>
        <li><router-link to="/shop-left-sidebar">Keyboard</router-link></li>
        <li><router-link to="/shop-left-sidebar">Laptop</router-link></li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "ShopSidebar",
  data() {
    return {
      minRange: 0,
      maxRange: 100,
      minValue: 0,
      maxValue: 100,
    };
  },
});
</script>

<style lang="scss" scoped>
.sidebar {
  .sidebar-widget {
    background-color: var(--whiteColor);
    padding: 25px;
    border-radius: 10px;
    margin: 0 0 30px;
    position: relative;
    z-index: 1;
    .sidebar-widget-title {
      color: var(--titleColor);
      font-size: 20px;
      font-weight: 600;
      margin-bottom: 20px;
    }
    &:last-child {
      margin: 0;
    }
  }
}
.multi-range {
  position: relative;
  height: 30px;
  margin: 0 0 20px;
}
.category-list {
  li {
    position: relative;
    margin-bottom: 8px;
    &:last-child {
      margin-bottom: 0;
    }
    a {
      color: var(--optionalColor);
      display: block;
      font-size: 14px;
      transition: var(transition);
      border-radius: 5px;
      span {
        margin-left: 5px;
      }
      &:hover {
        color: var(--secondaryColor);
      }
    }
  }
}
.price-title {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  p {
    margin: 0 5px 0 0;
    color: #555;
    font-size: 18px;
    font-weight: 400;
    line-height: 30px;
  }
  .price {
    span {
      margin: 0 5px 0 0;
      color: #555;
      font-size: 18px;
      font-weight: 400;
      line-height: 30px;
    }
  }
}
.multi-range input[type="range"]:nth-child(1)::-webkit-slider-thumb::before {
  background-color: #808080;
}
.multi-range input[type="range"]:nth-child(2) {
  background: none;
}
.multi-range input[type="range"]:nth-child(2)::-webkit-slider-thumb::before {
  background-color: #808080;
}
.multi-range input[type="range"]::-moz-range-track {
  background: none;
}
input[type="range"] {
  position: absolute;
  width: 100%;
  padding: 0;
  margin: 0;
  border: 0;
  outline: none;
  background: linear-gradient(#6366f1, #6366f1) no-repeat center;
  background-size: 100% 2px;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  pointer-events: none;
}
input[type="range"]:active,
input[type="range"]:focus,
input[type="range"]::-moz-focus-outer {
  border: none;
  outline: none;
}
input[type="range"]::-moz-range-thumb {
  position: relative;
  height: 20px;
  width: 20px;
  margin: 5px 0;
  border-radius: 50%;
  background-color: var(--whiteColor);
  box-shadow: 0px 2px 4px 0px rgba(79, 70, 229, 0.25);
  border: none;
  -moz-appearance: none;
  appearance: none;
  pointer-events: all;
  cursor: pointer;
}
input[type="range"]::-webkit-slider-thumb {
  position: relative;
  height: 28px;
  width: 28px;
  margin: 5px 0;
  border-radius: 50%;
  background-color: var(--whiteColor);
  box-shadow: 0px 2px 4px 0px rgba(79, 70, 229, 0.25);
  -webkit-appearance: none;
  appearance: none;
  pointer-events: all;
}

.tag-list {
  margin-top: -10px;
  li {
    display: inline-block;
    margin: 10px 6px 0px 0px;
    a {
      display: inline-block;
      font-size: 13px;
      line-height: 14px;
      padding: 11px 15px;
      border-radius: 5px;
      color: #4f46e5;
      background-color: #f1f5f9;
      transition: var(transition);
      &:hover {
        background-color: #4f46e5;
        color: var(--whiteColor);
        border-color: transparent;
      }
    }
  }
}

@media only screen and (min-width: 1400px) {
  .sidebar {
    .sidebar-widget {
      padding: 38px 40px 40px;
    }
  }
}
</style>