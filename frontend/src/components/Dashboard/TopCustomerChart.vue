<template>
  <div class="card-body p-xl-40">
    <h6 class="card-title fw-semiBold fs-18 mb-30">
      Top 05 Customer (January)
    </h6>
    <div class="chart-body">
      <apexchart
        type="donut"
        width="90%"
        :options="topCustomerChart"
        :series="customer"
      ></apexchart>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "TopCustomerChart",
  data: function () {
    return {
      customer: [44, 55, 41, 17, 15],
      topCustomerChart: {
        chart: {
          type: "donut",
          width: "90%",
        },
        toolbar: {
          show: false,
        },
        labels: [
          "Toggy Business",
          "Solit IT",
          "Walk-In Customer",
          "Medizo Enterprize",
          "Coze IT",
        ],
        colors: ["#7F26C6", "#BBBCF1", "#4F46E5", "#FAB134", "#FB5264"],
        legend: {
          show: true,
          position: "bottom",
          horizontalAlign: "center",
          floating: false,
          fontSize: "14px",
          fontWeight: 500,
          inverseOrder: false,
          offsetX: 0,
          offsetY: 0,
          labels: {
            colors: "#838A95",
          },
          markers: {
            width: 16,
            height: 16,
            radius: 2,
            offsetX: 0,
            offsetY: 0,
          },
        },
        stroke: {
          width: 0,
          colors: undefined,
        },
        responsive: [
          {
            breakpoint: 480,
            options: {
              chart: {
                width: 300,
              },
              legend: {
                position: "bottom",
                fontSize: "10px",
              },
            },
          },
        ],
      },
    };
  },
});
</script>

<style lang="scss" scoped>
@media only screen and (max-width: 380px) {
}
</style>