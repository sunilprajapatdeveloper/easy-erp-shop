<template>
  <apexchart
    type="polarArea"
    width="100%"
    :options="topSellingProductsChart"
    :series="products"
  ></apexchart>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "TopSellingProductsChart",
  data: function () {
    return {
      products: [42, 47, 52, 58, 65],
      topSellingProductsChart: {
        chart: {
          width: "100%",
          type: "polarArea",
          offsetX: 0,
          offsetY: 0,
        },
        stroke: {
          width: 0,
          colors: undefined,
        },
        yaxis: {
          show: false,
        },
        fill: {
          opacity: 1,
        },
        colors: ["#B142E1", "#EA5BE6", "#7A2CB2", "#7E32C4", "#FC7483"],
        labels: ["Earphones", "Laptop", "Smartphone", "Camera", "Tablet"],
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
        plotOptions: {
          polarArea: {
            rings: {
              strokeWidth: 0,
            },
            spokes: {
              strokeWidth: 0,
            },
          },
        },
      },
    };
  },
});
</script>