<template>
  <apexchart
    type="bar"
    height="350"
    :options="thisWeeksSalesPurchasesChart"
    :series="purchases"
  ></apexchart>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "ThisWeeksSalesPurchasesChart",
  data: function () {
    return {
      purchases: [
        {
          name: "Sales",
          data: [44, 55, 57, 56, 61, 58, 63],
        },
        {
          name: "Purchase",
          data: [76, 85, 101, 98, 87, 105, 91],
        },
      ],
      thisWeeksSalesPurchasesChart: {
        chart: {
          type: "bar",
          height: 350,
          toolbar: {
            show: false,
          },
        },
        colors: ["#4F46E5", "#BBBCF1"],
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: "60%",
            endingShape: "rounded",
          },
        },
        dataLabels: {
          enabled: false,
        },
        stroke: {
          show: true,
          width: 1,
          colors: ["transparent"],
        },
        xaxis: {
          categories: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          colors: "#EEF3FA",
        },
        yaxis: {
          title: {
            text: "$ (thousands)",
          },
        },
        // grid: {
        //     borderColor: '#EEF3FA',
        // },
        grid: {
          show: true,
          borderColor: "#EEF3FA",
          strokeDashArray: 0,
          position: "back",
        },
        legend: {
          show: true,
          position: "top",
          horizontalAlign: "right",
          floating: false,
          fontSize: "16px",
          fontWeight: 500,
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
        fill: {
          opacity: 1,
        },
        tooltip: {
          y: {
            formatter: function (val: string) {
              return "$ " + val + " thousands";
            },
          },
        },
      },
    };
  },
});
</script>