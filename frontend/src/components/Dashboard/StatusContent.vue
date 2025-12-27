<template>
  <div class="row justify-content-center">
    <div
      class="col-xxl-3 col-xl-6 col-md-6 col-sm-6"
      v-for="state in status"
      :key="state.id"
    >
      <div class="card border-0 rounded-1 mb-20">
        <div
          class="stat-card d-flex flex-wrap align-items-center justify-content-between"
        >
          <div class="stat-icon d-flex flex-coulmn justify-content-center">
            <img :src="state.imgBlurIcon" alt="Image" class="blur-icon" />
            <img :src="state.imgIcon" alt="Image" />
          </div>
          <div class="stat-info text-end">
            <span class="d-block fs-14 fw-medium">
              {{ state.title }}
            </span>
            <h4 class="fw-black mb-0 lh-1">${{ state.value }}</h4>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "StatusContent",
  data() {
    return {
      status: [
        {
          id: 1,
          imgBlurIcon: require("../../assets/img/icons/wallet-money-2.svg"),
          imgIcon: require("../../assets/img/icons/wallet-money.svg"),
          title: "SALES",
          value: "5312.00",
        },
        {
          id: 2,
          imgBlurIcon: require("../../assets/img/icons/wallet-add-2.svg"),
          imgIcon: require("../../assets/img/icons/wallet-add.svg"),
          title: "PURCHASES",
          value: "1304.00",
        },
        {
          id: 3,
          imgBlurIcon: require("../../assets/img/icons/money-send-2.svg"),
          imgIcon: require("../../assets/img/icons/money-send.svg"),
          title: "SALES RETURN",
          value: "314.00",
        },
        {
          id: 4,
          imgBlurIcon: require("../../assets/img/icons/money-change-2.svg"),
          imgIcon: require("../../assets/img/icons/money-change.svg"),
          title: "PURCHASES RETURN",
          value: "50.00",
        },
      ],
    };
  },
});
</script>

<style lang="scss" scoped>
.stat-card {
  padding: 8px 20px;
  .stat-icon {
    position: relative;
    width: 105px;
    min-height: 104px;
    .blur-icon {
      position: absolute;
      top: 0;
      left: 0;
    }
    img {
      margin-right: auto;
    }
  }
  .stat-info {
    width: calc(100% - 110px);
    span {
      color: #a8acb2;
      letter-spacing: 0.7px;
      display: block;
      margin-bottom: 1px;
    }
    h4 {
      color: var(--titleColor);
      font-size: 22px;
      font-weight: 800;
    }
  }
}

@media only screen and (min-width: 1600px) {
  .stat-card {
    padding: 8px 30px;
    .stat-info {
      position: relative;
      top: -3px;
      h4 {
        font-size: 25px;
      }
    }
  }
}
</style>