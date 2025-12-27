<template>
  <div
    class="modal fade"
    id="detailsModal"
    tabindex="-1"
    aria-labelledby="detailsModalLabel"
    aria-hidden="true"
  >
    <div
      class="modal-dialog modal-rl modal-dialog-centered modal-dialog-scrollable"
    >
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="detailsModalLabel">
            Expenses Details
          </h5>
          <button
            type="button"
            class="btn-close p-0"
            data-bs-dismiss="modal"
            aria-label="Close"
          >
            <img
              src="../../../assets/img/icons/close-circle-2.svg"
              alt="Image"
            />
          </button>
        </div>
        <div class="modal-body">
          <ul class="details-title list-style mb-40">
            <li class="fs-14 fw-medium text-title lh-1">
              DATE :<span class="fw-semibold ms-1">26/01/2022</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              TIME :<span class="fw-semibold ms-1">20:31</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              REFERENCE :<span class="fw-semibold ms-1">5389607</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              FROM WAREHOUSE :<span class="fw-semibold ms-1">Warehouse 01</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              CATEGORY :<span class="fw-semibold ms-1">Electronics</span>
            </li>
            <li class="fs-14 fw-medium text-title lh-1">
              GRAND TOTAL :<span class="fw-semibold ms-1">$ 5000.00</span>
            </li>
          </ul>
          <div class="table-responsive style-two">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">
                    NO.
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">
                    PRODUCT
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">
                    CODE
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">
                    QUANTITY
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1">
                    TYPE
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    01.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Headphone
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    9860532
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    100pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Addition
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    02.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Headphone
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    9860532
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    100pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Addition
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    03.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Headphone
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    9860532
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    100pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Addition
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    04.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Headphone
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    9860532
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    100pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Addition
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    05.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Headphone
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    9860532
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    100pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Addition
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    06.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Headphone
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    9860532
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    100pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Addition
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="modal-footer pt-20 pb-35">
          <a
            class="btn style-five upload-btn px-xxl-6 ms-auto"
            onclick="window.print();"
          >
            Print<img
              src="../../../assets/img/icons/download.svg"
              alt="Image"
            />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DetailsModal",
};
</script>