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
                SALE REFERENCE
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
            <tr>
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
                  <span class="text-optional fs-14 ms-2">26/09/2023</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                S - 2234
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Jane Ronan
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fs-14 fw-normal"
                  >Delivered</span
                >
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a
                    href="javascript:void(0)"
                    title="View"
                    data-bs-toggle="modal"
                    data-bs-target="#filterModal"
                  >
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </a>
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
            <tr>
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input
                      class="form-check-input"
                      type="checkbox"
                      id="test_3"
                    />
                    <label class="form-check-label text-optional" for="test_3">
                    </label>
                  </div>
                  <span class="text-optional fs-14 ms-2">25/09/2023</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                S - 1234
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Victor james
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-alert fs-14 fw-normal">Packed</span>
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a
                    href="javascript:void(0)"
                    title="View"
                    data-bs-toggle="modal"
                    data-bs-target="#filterModal"
                  >
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </a>
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
            <tr>
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input
                      class="form-check-input"
                      type="checkbox"
                      id="test_4"
                    />
                    <label class="form-check-label text-optional" for="test_4">
                    </label>
                  </div>
                  <span class="text-optional fs-14 ms-2">24/09/2023</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                S - 1234
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Jhon Doe
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 02
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fs-14 fw-normal"
                  >Delivered</span
                >
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a
                    href="javascript:void(0)"
                    title="View"
                    data-bs-toggle="modal"
                    data-bs-target="#filterModal"
                  >
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </a>
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
            <tr>
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input
                      class="form-check-input"
                      type="checkbox"
                      id="test_5"
                    />
                    <label class="form-check-label text-optional" for="test_5">
                    </label>
                  </div>
                  <span class="text-optional fs-14 ms-2">23/09/2023</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                S- 3453
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Angela Carter
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 01
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-success fs-14 fw-normal"
                  >Delivered</span
                >
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a
                    href="javascript:void(0)"
                    title="View"
                    data-bs-toggle="modal"
                    data-bs-target="#filterModal"
                  >
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </a>
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
            <tr>
              <td class="shadow-none fw-normal text-black title ps-0">
                <div class="d-flex align-items-center">
                  <div class="form-check checkbox style-three">
                    <input
                      class="form-check-input"
                      type="checkbox"
                      id="test_6"
                    />
                    <label class="form-check-label text-optional" for="test_6">
                    </label>
                  </div>
                  <span class="text-optional fs-14 ms-2">21/09/2023</span>
                </div>
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                5389607
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                S - 22345
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Jenifer Lora
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                Warehouse 03
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <span class="badge badge-danger fs-14 fw-normal"
                  >Cancelled</span
                >
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group d-flex flex-wrap align-items-center">
                  <a
                    href="javascript:void(0)"
                    title="View"
                    data-bs-toggle="modal"
                    data-bs-target="#filterModal"
                  >
                    <img src="../../../assets/img/icons/edit.svg" alt="Image" />
                  </a>
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
  name: "ShipmentList",
};
</script>