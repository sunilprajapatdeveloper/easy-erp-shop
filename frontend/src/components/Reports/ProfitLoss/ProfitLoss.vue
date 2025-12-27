<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <ul class="nav nav-tabs table-tablist" role="tablist">
        <li class="nav-item">
          <button
            class="nav-link active"
            data-bs-toggle="tab"
            data-bs-target="#tab_1"
            type="button"
            role="tab"
          >
            Profit
          </button>
        </li>
        <li class="nav-item">
          <button
            class="nav-link"
            data-bs-toggle="tab"
            data-bs-target="#tab_2"
            type="button"
            role="tab"
          >
            Loss
          </button>
        </li>
      </ul>
      <div class="tab-content product-tab-content">
        <div class="tab-pane fade show active" id="tab_1" role="tabpanel">
          <div class="table-responsive style-three">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                    <div class="form-check checkbox">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        id="test_1"
                      />
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
                    SALES
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PURCHASES
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    SALES RETURN
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PURCHASE RETURN
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    REVENUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    PROFIT NET
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
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_2"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_2"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $7420.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $10994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $315.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1324.00
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
                        <label
                          class="form-check-label text-optional"
                          for="test_5"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2420.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $20994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $415.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1524.00
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
                        <label
                          class="form-check-label text-optional"
                          for="test_6"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">22/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $4520.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $7994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $215.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2324.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_7"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_7"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $8420.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $6994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $12.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1324.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_20"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_20"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $12340.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $21994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1225.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1324.00
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="tab-pane fade" id="tab_2" role="tabpanel">
          <div class="table-responsive style-three">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                    <div class="form-check checkbox">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        id="test_1"
                      />
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
                    SALES
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PURCHASES
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    SALES RETURN
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PURCHASE RETURN
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    REVENUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    LOSS NET
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
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_31"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_31"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $7420.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $10994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $315.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1324.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_32"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_32"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2420.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $20994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $415.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1524.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_33"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_33"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">22/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $4520.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $7994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $215.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2324.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_34"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_34"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $8420.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $6994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $12.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1324.00
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_35"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_35"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $12340.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $21994.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $0.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1225.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1324.00
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
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
  name: "ProfitLoss",
};
</script>