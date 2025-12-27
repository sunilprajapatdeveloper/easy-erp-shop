<template>
  <div class="card border-0 shadow-none rounded-1 mb-25">
    <div class="card-body p-xl-40">
      <ul class="nav nav-tabs table-tablist style-two" role="tablist">
        <li class="nav-item">
          <button
            class="nav-link active"
            data-bs-toggle="tab"
            data-bs-target="#tab_1"
            type="button"
            role="tab"
          >
            Sale
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
            Purchase
          </button>
        </li>
        <li class="nav-item">
          <button
            class="nav-link"
            data-bs-toggle="tab"
            data-bs-target="#tab_3"
            type="button"
            role="tab"
          >
            Quotation
          </button>
        </li>
        <li class="nav-item">
          <button
            class="nav-link"
            data-bs-toggle="tab"
            data-bs-target="#tab_4"
            type="button"
            role="tab"
          >
            Sales Return
          </button>
        </li>
        <li class="nav-item">
          <button
            class="nav-link"
            data-bs-toggle="tab"
            data-bs-target="#tab_5"
            type="button"
            role="tab"
          >
            Purchase Return
          </button>
        </li>
        <li class="nav-item">
          <button
            class="nav-link"
            data-bs-toggle="tab"
            data-bs-target="#tab_6"
            type="button"
            role="tab"
          >
            Transfer
          </button>
        </li>
        <li class="nav-item">
          <button
            class="nav-link"
            data-bs-toggle="tab"
            data-bs-target="#tab_7"
            type="button"
            role="tab"
          >
            Adjustments
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
                    QUANTITY
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
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    GRAND TOTAL
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PAID
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    DUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    PAYMENT STATUS
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
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jane Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    01
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
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
                        <label
                          class="form-check-label text-optional"
                          for="test_3"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Victor James
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    14
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Ordered</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal">Due</span>
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
                        <label
                          class="form-check-label text-optional"
                          for="test_4"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">24/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Anda Smith
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    10
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
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
                      <span class="text-optional fs-14 ms-2">23/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jennifer Lora
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    22
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
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
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Angela Carter
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    23
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal"
                      >Pending</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1000.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Partial</span
                    >
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
                      <span class="text-optional fs-14 ms-2">19/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jonathon Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    15
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
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
                        id="test_8"
                      />
                      <label class="form-check-label" for="test_8">
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
                    QUANTITY
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
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    GRAND TOTAL
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PAID
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    DUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    PAYMENT STATUS
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
                          id="test_8"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_8"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jane Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    01
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_9"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_9"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Victor James
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    14
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Ordered</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal">Due</span>
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_10"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_10"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">24/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Anda Smith
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    10
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_11"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_11"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">23/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jennifer Lora
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    22
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_12"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_12"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Angela Carter
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    23
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal"
                      >Pending</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1000.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Partial</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_13"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_13"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">19/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jonathon Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    15
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="tab-pane fade" id="tab_3" role="tabpanel">
          <div class="table-responsive style-three">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                    <div class="form-check checkbox">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        id="test_14"
                      />
                      <label class="form-check-label" for="test_14">
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
                    QUANTITY
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
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    GRAND TOTAL
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PAID
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    DUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    PAYMENT STATUS
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
                          id="test_15"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_15"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jane Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    01
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_16"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_16"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Victor James
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    14
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Ordered</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal">Due</span>
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_17"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_17"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">24/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Anda Smith
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    10
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_18"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_18"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">23/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jennifer Lora
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    22
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_19"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_19"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Angela Carter
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    23
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal"
                      >Pending</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1000.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Partial</span
                    >
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
                      <span class="text-optional fs-14 ms-2">19/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jonathon Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    15
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="tab-pane fade" id="tab_4" role="tabpanel">
          <div class="table-responsive style-three">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                    <div class="form-check checkbox">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        id="test_21"
                      />
                      <label class="form-check-label" for="test_21">
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
                    QUANTITY
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
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    GRAND TOTAL
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PAID
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    DUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    PAYMENT STATUS
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
                          id="test_22"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_22"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jane Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    01
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_23"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_23"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Victor James
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    14
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Ordered</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal">Due</span>
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_24"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_24"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">24/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Anda Smith
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    10
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_25"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_25"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">23/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jennifer Lora
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    22
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_26"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_26"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Angela Carter
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    23
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal"
                      >Pending</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1000.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Partial</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_27"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_27"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">19/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jonathon Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    15
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="tab-pane fade" id="tab_5" role="tabpanel">
          <div class="table-responsive style-three">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                    <div class="form-check checkbox">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        id="test_31"
                      />
                      <label class="form-check-label" for="test_31">
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
                    QUANTITY
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
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    GRAND TOTAL
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PAID
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    DUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    PAYMENT STATUS
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
                          id="test_32"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_32"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jane Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    01
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_333"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_333"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Victor James
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    14
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Ordered</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal">Due</span>
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
                      <span class="text-optional fs-14 ms-2">24/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Anda Smith
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    10
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_335"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_335"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">23/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jennifer Lora
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    22
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_36"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_36"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Angela Carter
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    23
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal"
                      >Pending</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1000.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Partial</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_37"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_37"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">19/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jonathon Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    15
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="tab-pane fade" id="tab_6" role="tabpanel">
          <div class="table-responsive style-three">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                    <div class="form-check checkbox">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        id="test_41"
                      />
                      <label class="form-check-label" for="test_41">
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
                    QUANTITY
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
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    GRAND TOTAL
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PAID
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    DUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    PAYMENT STATUS
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
                          id="test_42"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_42"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jane Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    01
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_43"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_43"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Victor James
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    14
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Ordered</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal">Due</span>
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_44"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_44"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">24/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Anda Smith
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    10
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_45"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_45"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">23/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jennifer Lora
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    22
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_46"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_46"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Angela Carter
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    23
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal"
                      >Pending</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1000.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Partial</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_47"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_47"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">19/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jonathon Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    15
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="tab-pane fade" id="tab_7" role="tabpanel">
          <div class="table-responsive style-three">
            <table class="table text-nowrap align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 ps-0">
                    <div class="form-check checkbox">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        id="test_51"
                      />
                      <label class="form-check-label" for="test_51">
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
                    QUANTITY
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
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    GRAND TOTAL
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    PAID
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0">
                    DUE
                    <img
                      class="ms-2"
                      src="../../../assets/img/icons/up-down-aroow.svg"
                      alt="Image"
                    />
                  </th>
                  <th scope="col" class="text-title fw-normal fs-14 pt-0 pe-0">
                    PAYMENT STATUS
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
                          id="test_52"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_52"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">26/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jane Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    01
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $120.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_53"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_53"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">25/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Victor James
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    14
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Ordered</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $ 00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $2100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal">Due</span>
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_54"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_54"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">24/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Anda Smith
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    10
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1210.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_55"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_55"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">23/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jennifer Lora
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    22
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1500.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Paid</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_56"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_56"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">21/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Angela Carter
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    23
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-danger fs-14 fw-normal"
                      >Pending</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $1000.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $100.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-alert fs-14 fw-normal"
                      >Partial</span
                    >
                  </td>
                </tr>
                <tr>
                  <td class="shadow-none fw-normal text-black title ps-0">
                    <div class="d-flex align-items-center">
                      <div class="form-check checkbox style-three">
                        <input
                          class="form-check-input"
                          type="checkbox"
                          id="test_57"
                        />
                        <label
                          class="form-check-label text-optional"
                          for="test_57"
                        >
                        </label>
                      </div>
                      <span class="text-optional fs-14 ms-2">19/09/2023</span>
                    </div>
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    5389607
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    Jonathon Ronan
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    15
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $15300.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    $00.00
                  </td>
                  <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                    <span class="badge badge-success fs-14 fw-normal"
                      >Received</span
                    >
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
  name: "StockReportDetails",
};
</script>