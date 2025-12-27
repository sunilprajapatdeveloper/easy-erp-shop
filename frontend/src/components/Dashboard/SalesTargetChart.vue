<template>
  <div class="card-body p-xl-40">
    <h6 class="card-title fw-semiBold fs-18 mb-0">Sales Target</h6>
    <apexchart
      type="radialBar"
      height="400"
      id="salesTargetChart"
      :options="salesTargetChart"
      :series="target"
    ></apexchart>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "SalesTargetChart",
  data: function () {
    return {
      target: [75, 55, 67],
      salesTargetChart: {
        chart: {
          height: 400,
          type: "radialBar",
        },
        toolbar: {
          show: false,
        },
        plotOptions: {
          radialBar: {
            hollow: {
              margin: 5,
              size: "33%",
            },
            track: {
              strokeWidth: "57%",
              margin: 10,
            },
            dataLabels: {
              name: {
                fontSize: "15px",
              },
              value: {
                fontSize: "16px",
              },
            },
          },
        },
        colors: ["#6366F1", "#2172FF", "#F65738"],
        labels: ["Weekly", "Monthly", "Yearly"],
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
          lineCap: "round",
        },
        fill: {
          type: "gradient",
          gradient: {
            shade: "dark",
            type: "horizontal",
            shadeIntensity: 0.5,
            gradientToColors: ["#4F46E5", "#21BDFD", "#FAB134"],
            inverseColors: true,
            opacityFrom: 1,
            opacityTo: 1,
            stops: [0, 100],
          },
        },
      },
    };
  },
});
</script>