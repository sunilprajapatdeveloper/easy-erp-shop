<template>
  <div class="card-body p-xl-40">
    <h6 class="card-title fw-semiBold fs-18 mb-30">
      Payment Sent & Received(Last 5 Days)
    </h6>
    <apexchart
      type="line"
      height="350"
      :options="paymentSentReceivedChart"
      :series="payment"
    ></apexchart>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "PaymentSentReceivedChart",
  data: function () {
    return {
      payment: [
        {
          name: "Payment Sent",
          data: [55, 62, 38, 24, 43, 26, 31, 20, 6, 26, 15, 10],
        },
        {
          name: "Payment Sent",
          data: [35, 41, 62, 42, 13, 18, 29, 37, 36, 51, 32, 35],
        },
      ],
      paymentSentReceivedChart: {
        chart: {
          height: 350,
          toolbar: {
            show: false,
          },
          type: "line",
          zoom: {
            enabled: false,
          },
        },
        toolbar: {
          show: false,
        },
        dataLabels: {
          enabled: false,
        },
        stroke: {
          width: [2, 2],
          curve: "smooth",
          dashArray: [0, 6, 5],
        },
        colors: ["#7467F0", "#BBC6CF"],
        title: {
          show: false,
        },
        legend: {
          show: false,
        },
        markers: {
          size: 0,
          hover: {
            sizeOffset: 6,
          },
        },
        // xaxis: {
        //     categories: ['18.01', '19.01', '20.01', '21.01', '22.01'],
        // },
        xaxis: {
          categories: [
            "1k",
            "2k",
            "3k",
            "4k",
            "5k",
            "6k",
            "7k",
            "8k",
            "9k",
            "10k",
            "11k",
            "12k",
          ],
        },
        tooltip: {
          y: [
            {
              title: {
                formatter: function (val: string) {
                  return val + " (mins)";
                },
              },
            },
            {
              title: {
                formatter: function (val: string) {
                  return val + " per session";
                },
              },
            },
            {
              title: {
                formatter: function (val: unknown) {
                  return val;
                },
              },
            },
          ],
        },
        grid: {
          borderColor: "#f1f1f1",
        },
      },
    };
  },
});
</script>