<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                <div class="form-check checkbox">
                  <input class="form-check-input" type="checkbox" id="test_1" />
                  <label class="form-check-label" for="test_1">
                    DATE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </label>
                </div>
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                REFERENCE
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                CUSTOMER
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                WAREHOUSE
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                TOTAL ITEMS
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                GRAND TOTAL
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                STATUS
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                ACTION
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="quotation in quotations" :key="quotation.id">
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input
                      class="form-check-input"
                      type="checkbox"
                      id="test_2"
                    />
                    <label class="form-check-label text-optional" for="test_2">
                    </label>
                  </div>
                  <span class="text-optional fs-14 ms-2">{{
                    quotation.date
                  }}</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ quotation.reference }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ quotation.costomer }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ quotation.warehouse }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ quotation.totalItems }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                ${{ quotation.grandTotal }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fs-14 fw-normal">
                  {{ quotation.status }}
                </span>
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a
                    href="javascript:void(0)"
                    title="View"
                    data-bs-toggle="modal"
                    data-bs-target="#detailsModal"
                  >
                    <img src="../../../assets/img/icons/eye.svg" alt="Image" />
                  </a>
                  <router-link to="/edit-quotation" title="Edit">
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </router-link>
                  <a
                    class="delete-btn"
                    data-bs-toggle="offcanvas"
                    href="#deletePopup"
                    role="button"
                    aria-controls="deletePopup"
                  >
                    <img
                      src="../../../assets/img/icons/close.svg"
                      alt="Image"
                    />
                  </a>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "QuotationList",
  data() {
    return {
      quotations: [
        {
          id: 1,
          date: "26/12/2023",
          reference: "3489603",
          costomer: "Warren Smith",
          warehouse: "Warehouse 01",
          totalItems: "05",
          grandTotal: "120.00",
          status: "Completed",
        },
        {
          id: 2,
          date: "25/12/2023",
          reference: "3289605",
          costomer: "Victor James",
          warehouse: "Warehouse 01",
          totalItems: "07",
          grandTotal: "234.00",
          status: "Completed",
        },
        {
          id: 3,
          date: "24/12/2023",
          reference: "4589603",
          costomer: "Jennifer Smith",
          warehouse: "Warehouse 01",
          totalItems: "10",
          grandTotal: "213.00",
          status: "Completed",
        },
        {
          id: 4,
          date: "23/12/2023",
          reference: "6789606",
          costomer: "Angela Jolly",
          warehouse: "Warehouse 01",
          totalItems: "12",
          grandTotal: "140.00",
          status: "Completed",
        },
        {
          id: 5,
          date: "22/12/2023",
          reference: "2896034",
          costomer: "Jane Ronan",
          warehouse: "Warehouse 01",
          totalItems: "15",
          grandTotal: "432.00",
          status: "Completed",
        },
        {
          id: 6,
          date: "21/12/2023",
          reference: "8389605",
          costomer: "Jenifer Smith",
          warehouse: "Warehouse 01",
          totalItems: "03",
          grandTotal: "543.00",
          status: "Completed",
        },
      ],
    };
  },
});
</script>