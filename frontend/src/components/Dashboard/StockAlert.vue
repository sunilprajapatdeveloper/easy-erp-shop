<template>
  <div class="card-body p-xl-40">
    <h6 class="card-title fw-semiBold fs-18 mb-30">Stock Alert</h6>
    <div class="table-responsive style-five">
      <table class="table text-nowrap align-middle mb-0">
        <thead>
          <tr>
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
              CODE
            </th>
            <th scope="col" class="text-title fw-normal fs-14 pt-0">PRODUCT</th>
            <th scope="col" class="text-title fw-normal fs-14 pt-0">
              WAREHOUSE
            </th>
            <th scope="col" class="text-title fw-normal fs-14 pt-0">
              QUANTITY
            </th>
            <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
              ALERT QUANTITY
            </th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td class="shadow-none fw-normal text-black title ps-0">
              <span class="text-optional fs-14">87305928</span>
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Smartphone
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Warehouse 02
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">05</td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              <span class="text-red fw-bold">10</span>
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-normal text-black title ps-0">
              <span class="text-optional fs-14">87305912</span>
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Mask
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Warehoues 02
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">10</td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              <span class="text-red fw-bold">05</span>
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-normal text-black title ps-0">
              <span class="text-optional fs-14">87305452</span>
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Laptop
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Warehouse 01
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">100</td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              <span class="text-red fw-bold">05</span>
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-normal text-black title ps-0">
              <span class="text-optional fs-14">87305231</span>
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Earphone
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Warhouse 03
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">10</td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              <span class="text-red fw-bold">50</span>
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-normal text-black title ps-0">
              <span class="text-optional fs-14">87305452</span>
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Laptop
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Warehouse 01
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">100</td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              <span class="text-red fw-bold">05</span>
            </td>
          </tr>
          <tr>
            <td class="shadow-none fw-normal text-black title ps-0">
              <span class="text-optional fs-14">87303231</span>
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Earphone
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Warhouse 02
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">10</td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              <span class="text-red fw-bold">50</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: "StockAlert",
};
</script>