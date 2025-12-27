<template>
  <footer class="footer-wrap style-two">
    <div class="row align-items-center">
      <div class="col-md-6">
        <div class="copyright-text">
          <span class="text-white text-bold">&copy; EasyERPShop</span> All rights reserved.
        </div>
      </div>
      <div class="col-md-6">
        <p class="text-offwhite mb-0 text-md-end">
          <span class="text-white text-bold">EasyERPShop</span> - Built for modern businesses
        </p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: "MainFooter",
};
</script>

<style lang="scss" scoped>
.footer-wrap {
  background-color: #161e2c;
  padding: {
    top: 26px;
    bottom: 26px;
  }
  .copyright-text {
    color: var(--offwhiteColor);
    a {
      &:hover {
        color: var(--secondaryColor) !important;
      }
    }
  }
  &.style-two {
    margin: 0 -12px;
    padding-left: 12px;
    padding-right: 12px;
  }
}
@media only screen and (max-width: 767px) {
  .footer-wrap {
    padding: {
      top: 18px;
      bottom: 18px;
    }
    .copyright-text {
      margin-bottom: 8px;
    }
    .copyright-text,
    p {
      text-align: center;
      font-size: 14px;
    }
  }
}

@media only screen and (min-width: 1400px) {
  .footer-wrap {
    &.style-two {
      margin: 0 -40px;
      padding-left: 40px;
      padding-right: 40px;
    }
  }
}
</style>