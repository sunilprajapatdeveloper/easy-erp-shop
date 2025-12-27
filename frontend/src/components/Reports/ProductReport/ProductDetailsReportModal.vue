<template>
  <div class="modal fade" id="detailsModal" tabindex="-1" aria-labelledby="detailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-xxl modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="detailsModalLabel">
            Product Details Report
          </h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>
        <div class="modal-body pb-40">
          <div class="row">
            <div class="col-lg-4">
              <h6 class="fs-16 fw-bold text-title mb-20">Product info:</h6>
              <ul class="details-title list-style mb-40">
                <li class="fs-14 fw-medium text-title lh-1">
                  DATE :<span class="fw-semibold ms-1">26/01/2022</span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  TIME :<span class="fw-semibold ms-1">20:31</span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  STATUS :<span class="badge badge-outline-purple fw-semibold ms-1 fs-14">
                    Received
                  </span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  REFERENCE :<span class="fw-semibold ms-1">5389607</span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  WAREHOUSE :<span class="fw-semibold ms-1">Warehouse 01</span>
                </li>
                <li class="fs-14 fw-medium text-title lh-1">
                  PAYMENT STATUS :<span class="badge badge-outline-green fw-semibold ms-1 fs-14">
                    Paid
                  </span>
                </li>
              </ul>
            </div>
            <div class="col-lg-4 ps-xxl-6">
              <h6 class="fs-16 fw-bold text-title mb-20">Customer info:</h6>
              <ul class="details-title list-style mb-40">
                <li class="fs-14 fw-semibold text-title lh-1">
                  NAME :<span class="ms-1 text-optional">Jhon Smith</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  MAIL :
                  <span class="text-optional ms-1"> Smith123@gmail.com </span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  PHONE :<span class="text-optional ms-1">+00 983 234 396</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  ADDRESS :<span class="text-optional ms-1">
                    2750 Quadra Street, Canada
                  </span>
                </li>
              </ul>
            </div>
            <div class="col-lg-4 ps-xxl-6">
              <h6 class="fs-16 fw-bold text-title mb-20">Company info:</h6>
              <ul class="details-title list-style mb-40">
                <li class="fs-14 fw-semibold text-title lh-1">
                  NAME :<span class="ms-1 text-optional">EasyERPShop Corporati</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  MAIL :<span class="text-optional ms-1">hello@EasyERPShop.com</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  PHONE :<span class="text-optional ms-1">+00 793 234 609</span>
                </li>
                <li class="fs-14 fw-semibold text-title lh-1">
                  ADDRESS :
                  <span class="text-optional ms-1">
                    413 North Las Vegas, NV 89032
                  </span>
                </li>
              </ul>
            </div>
          </div>

          <div class="table-responsive style-two">
            <table class="table text-nowrap align-middle mb-0 border-0">
              <thead>
                <tr class="bg_mild">
                  <th scope="col" class="text-title fw-normal fs-14 lh-1 bg_mild">
                    NO.
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1 bg_mild">
                    PRODUCT
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1 bg_mild">
                    CODE
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1 bg_mild">
                    UNIT PRICE
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1 bg_mild">
                    QUANTITY
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1 bg_mild">
                    DISCOUNT
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1 bg_mild">
                    TAX
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 lh-1 bg_mild">
                    SUBTOTAL
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr class="br-s-1">
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph br-s-1">
                    01.
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Headphone
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    9860532
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 50.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    100pc
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $5000.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-title float-right br-s-1">
                    ORDER TAX
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph float-right">
                    $00.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-title float-right br-s-1">
                    DISCOUNT
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 150.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-title float-right br-s-1">
                    Shipping
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 50.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-title br-s-1">
                    GRAND TOTAL
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-black text-purple">
                    $ 200.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-title float-right br-s-1">
                    PAID
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 200.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph border-0"></td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-title float-right br-s-1 br-1">
                    DUE
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-semibold text-paragraph br-1">
                    $ 0.00
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="modal-footer pt-25 pb-35 d-flex flex-wrap justify-content-end me-xxl-3">
          <button class="btn style-six">SMS</button>
          <button class="btn style-seven">EMAIL</button>
          <a href="#" class="btn style-eight">pdf</a>
          <a class="btn style-five upload-btn px-xxl-6" onclick="window.print();">
            Print
            <img src="../../../assets/img/icons/download.svg" alt="Image" />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductDetailsReportModal",
};
</script>