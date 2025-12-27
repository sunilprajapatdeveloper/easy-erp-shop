<template>
  <div class="card-body p-xl-40">
    <h6 class="card-title fw-semiBold fs-18 mb-30">Recent Invoices</h6>
    <div class="table-responsive style-four">
      <table class="table text-nowrap align-middle mb-0">
        <thead>
          <tr>
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">
              REF. CODE
            </th>
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">
              CUSTOMER
            </th>
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">
              AMOUNT
            </th>
            <th scope="col" class="text-title fw-normal fs-14 pt-0 ls-1">
              STATUS
            </th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th class="shadow-none lh-1 fw-bold ps-0">
              <router-link
                to="/sales-report"
                class="text-light-purple text-decoration-underline fs-14 fw-semibold"
              >
                87305915
              </router-link>
            </th>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Jhon Doe
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              $1200.00
            </td>
            <td class="shadow-none lh-1 fw-medium">
              <span class="badge badge-outline-red style-two fs-14 fw-medium">
                Due
              </span>
            </td>
          </tr>
          <tr>
            <th class="shadow-none lh-1 fw-bold ps-0">
              <router-link
                to="/sales-report"
                class="text-light-purple text-decoration-underline fs-14 fw-semibold"
              >
                87305913
              </router-link>
            </th>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Victor James
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              $300.00
            </td>
            <td class="shadow-none lh-1 fs-14 text-paragraph">
              <span class="badge badge-outline-green style-two fs-14 fw-medium">
                Paid
              </span>
            </td>
          </tr>
          <tr>
            <th class="shadow-none lh-1 fw-bold ps-0">
              <router-link
                to="/sales-report"
                class="text-light-purple text-decoration-underline fs-14 fw-semibold"
              >
                87305912
              </router-link>
            </th>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Jonathon Ronan
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              $530.50
            </td>
            <td class="shadow-none lh-1 fs-14 text-paragraph">
              <span class="badge badge-outline-green style-two fs-14 fw-medium">
                Paid
              </span>
            </td>
          </tr>
          <tr>
            <th class="shadow-none lh-1 fw-bold ps-0 border-0">
              <router-link
                to="/sales-report"
                class="text-light-purple text-decoration-underline fs-14 fw-semibold"
              >
                87305925
              </router-link>
            </th>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Angela Carter
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              $633.50
            </td>
            <td class="shadow-none lh-1 fs-14 text-paragraph">
              <span class="badge badge-outline-red style-two fs-14 fw-medium">
                Due
              </span>
            </td>
          </tr>
          <tr>
            <th class="shadow-none lh-1 fw-bold ps-0 border-0">
              <router-link
                to="/sales-report"
                class="text-light-purple text-decoration-underline fs-14 fw-semibold"
              >
                57305925
              </router-link>
            </th>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              Josef Stalin
            </td>
            <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
              $133.50
            </td>
            <td class="shadow-none lh-1 fs-14 text-paragraph">
              <span class="badge badge-outline-red style-two fs-14 fw-medium">
                Paid
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: "RecentInvoices",
};
</script>