<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <div class="table-responsive style-three">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                CODE
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                PRODUCT
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
                QUANTITY
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
              <th scope="col" class="text-title fw-normal fs-14 pt-0">
                ALERT QUANTITY
                <img
                  class="ms-2"
                  src="../../../assets/img/icons/up-down-aroow.svg"
                  alt="Image"
                />
              </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Headphone
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                10
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="text-red fw-bold fs-14">10</span>
              </td>
            </tr>
            <tr>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Mouse
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                27
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="text-red fw-bold fs-14">100</span>
              </td>
            </tr>
            <tr>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Smart Phones
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                33
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="text-red fw-bold fs-14">50</span>
              </td>
            </tr>
            <tr>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Smart Watches
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                12
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="text-red fw-bold fs-14">10</span>
              </td>
            </tr>
            <tr>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Television
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                16
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="text-red fw-bold fs-14">100</span>
              </td>
            </tr>
            <tr>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Gaming Laptop
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                21
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="text-red fw-bold fs-14">10</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <div class="row pb-45 align-items-center">
    <div class="col-sm-6">
      <div
        class="d-flex flex-wrap align-items-center justify-content-center justify-content-sm-start page-unit"
      >
        <span class="fs-13">Showing product per page</span>
        <select class="text-title border-0 fs-14 bg-transparent">
          <option value="0">10</option>
          <option value="1">20</option>
          <option value="2">30</option>
        </select>
      </div>
    </div>
    <div class="col-sm-6 text-sm-end text-center">
      <ul class="page-nav list-style">
        <li>
          <a href="#">
            <img
              src="../../../assets/img/icons/left-arrow-purple.svg"
              alt="Image"
            />
          </a>
        </li>
        <li><a href="#" class="active">1</a></li>
        <li><a href="#">2</a></li>
        <li><a href="#">3</a></li>
        <li>
          <a href="#">
            <img
              src="../../../assets/img/icons/right-arrow-purple.svg"
              alt="Image"
            />
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductQuantityAlert",
};
</script>