<template>
  <div class="hero-section">
    <div class="row align-items-center">
      <div class="col-lg-6">
        <div class="hero-content">
          <h1>The Best Electronics Shop For Online Purchase</h1>
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et mi
            viverra gravida rutrum lorem. Amet sed morbi dignissim tortor nisl.
            Faucibus donec et, est cras facilisis. Iaculis tristique.
          </p>
          <router-link
            to="/shop-right-sidebar"
            class="btn style-eleven px-xl-5"
          >
            Shop Now
          </router-link>
        </div>
      </div>
      <div class="col-lg-6">
        <div class="hero-img">
          <img src="../../../assets/img/hero-img.webp" alt="Image" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MainBanner",
};
</script>

<style lang="scss" scoped>
.hero-section {
  background-color: #8897e8;
  margin-top: 30px;
  border-radius: 10px;
  padding: 35px 50px;
  .hero-content {
    h1 {
      color: var(--whiteColor);
      font-size: 38px;
      font-weight: 800;
      line-height: 52px;
      margin-bottom: 10px;
    }
    p {
      color: #e3e8ff;
      line-height: 28px;
      margin-bottom: 20px;
    }
  }
  .hero-img {
    img {
      display: block;
      margin: 0 auto;
    }
  }
}

@media only screen and (max-width: 767px) {
  .hero-section {
    padding: 48px 20px;
    .hero-content {
      h1 {
        font-size: 28px;
        font-weight: 700;
        line-height: 36px;
      }
      p {
        padding-right: 0;
      }
    }
    .hero-img {
      margin-top: 35px;
      padding: 0;
    }
  }
}

@media only screen and (min-width: 1400px) {
  .hero-section {
    margin: 30px 50px 0;
    padding: 48px 80px;
    .hero-content {
      h1 {
        font-size: 42px;
        line-height: 60px;
      }
      p {
        padding-right: 20px;
      }
    }
    .hero-img {
      padding-left: 135px;
    }
  }
}

@media only screen and (min-width: 1600px) {
  .hero-section {
    .hero-content {
      h1 {
        font-size: 60px;
        line-height: 80px;
      }
    }
  }
}
</style>