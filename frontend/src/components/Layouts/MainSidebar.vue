<template>
  <div id="sidebar-wrap" class="sidebar-area position-fixed bg_primary transition">
    <div id="sidebar_menu_wrap" class="sidebar-menu-wrap">
      <ul class="sidebar-menu list-style">
        <li class="nav-item">
          <router-link to="/" class="nav-link">
            <img src="../../assets/img/icons/dashboard.svg" alt="Image" />
            <span class="nav-text d-block fs-14 fw-light"> Dashboard </span>
          </router-link>
        </li>
        <li class="nav-item" v-if="hasAnyPermission(['PRODUCT_CREATE', 'PRODUCT_LIST', 'PRINT_BARCODE'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/products.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Products </span>
          </a>
          <ul class="sidebar-secondary-menu list-style bg-white">
            <li class="nav-item" v-if="hasPermission('PRODUCT_CREATE')">
              <router-link to="/create-product">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Product
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PRODUCT_LIST')">
              <router-link to="/product-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Product List
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PRODUCT_LIST')">
              <router-link to="/products/manage-to-warehouse">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Manage Product
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PRINT_BARCODE')">
              <router-link to="/print-barcode">
                <img src="../../assets/img/icons/barcode.svg" alt="Image" />
                Print Barcode
              </router-link>
            </li>
          </ul>
        </li>
        <li class="nav-item" v-if="hasAnyPermission(['SALE_CREATE', 'SALE_LIST', 'SALE_POS', 'SALE_SHIPMENT'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/sale.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Sales </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('SALE_CREATE')">
              <router-link to="/create-sales">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Sale
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SALE_LIST')">
              <router-link to="/sales-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Sales List
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SALE_POS')">
              <router-link to="/pos">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                POS
              </router-link>
            </li>
            <!-- <li class="nav-item" v-if="hasPermission('SALE_SHIPMENT')">
              <router-link to="/shipment-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Shipment
              </router-link>
            </li> -->
          </ul>
        </li>
        <li class="nav-item" v-if="hasAnyPermission(['SALE_RETURN_CREATE', 'SALE_RETURN_LIST'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/sales-return.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Sales Return </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('SALE_RETURN_CREATE')">
              <router-link to="/create-sales-return">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Sales Return
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SALE_RETURN_LIST')">
              <router-link to="/sales-return-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Sales Return List
              </router-link>
            </li>
          </ul>
        </li>
        <li class="nav-item" v-if="hasAnyPermission(['PURCHASE_CREATE', 'PURCHASE_LIST'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/purchase.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Purchases </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('PURCHASE_CREATE')">
              <router-link to="/create-purchase">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Purchases
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PURCHASE_LIST')">
              <router-link to="/purchase-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Purchases List
              </router-link>
            </li>
          </ul>
        </li>
        <li class="nav-item" v-if="hasAnyPermission(['PURCHASE_RETURN_CREATE', 'PURCHASE_RETURN_LIST'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/purchase-return.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Purchase Return </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('PURCHASE_RETURN_CREATE')">
              <router-link to="/create-purchase-return">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Purchase Return
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PURCHASE_RETURN_LIST')">
              <router-link to="/purchase-return-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Purchase Return List
              </router-link>
            </li>
          </ul>
        </li>
        <li class="nav-item" v-if="hasAnyPermission(['ADJUSTMENT_CREATE', 'ADJUSTMENT_LIST'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/adjustments.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Adjustments </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('ADJUSTMENT_CREATE')">
              <router-link to="/create-adjustment">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Adjustments
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('ADJUSTMENT_LIST')">
              <router-link to="/adjustment-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Adjustment List
              </router-link>
            </li>
          </ul>
        </li>
        <!-- <li class="nav-item" v-if="hasAnyPermission(['TRANSFER_CREATE', 'TRANSFER_LIST'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/transfer.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Transfer </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('TRANSFER_CREATE')">
              <router-link to="/create-transfer">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Transfer
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('TRANSFER_LIST')">
              <router-link to="/transfer-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Transfer List
              </router-link>
            </li>
          </ul>
        </li> -->
        <li class="nav-item" v-if="hasAnyPermission(['EXPENSE_CREATE', 'EXPENSE_LIST', 'EXPENSE_CATEGORY'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/expenses.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Expenses </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('EXPENSE_CREATE')">
              <router-link to="/create-expense">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Expenses
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('EXPENSE_LIST')">
              <router-link to="/expense-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Expenses List
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('EXPENSE_CATEGORY')">
              <router-link to="/expense-category">
                <img src="../../assets/img/icons/expense-category.svg" alt="Image" />
                Expenses Category
              </router-link>
            </li>
          </ul>
        </li>
        <!-- <li class="nav-item" v-if="hasAnyPermission(['QUOTATION_CREATE', 'QUOTATION_LIST'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/quotation.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Quotations </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('QUOTATION_CREATE')">
              <router-link to="/create-quotation">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Create Quotation
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('QUOTATION_LIST')">
              <router-link to="/quotation-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Quotation List
              </router-link>
            </li>
          </ul>
        </li> -->
        <li class="nav-item" v-if="hasAnyPermission(['CUSTOMER_LIST', 'SUPPLIER_LIST', 'USER_LIST'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/adjustments.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> People </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('CUSTOMER_LIST')">
              <router-link to="/customer-list">
                <img src="../../assets/img/icons/user-square.svg" alt="Image" />
                Customer List
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SUPPLIER_LIST')">
              <router-link to="/supplier-list">
                <img src="../../assets/img/icons/item-list.svg" alt="Image" />
                Supplier List
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('USER_LIST')">
              <router-link to="/user-list">
                <img src="../../assets/img/icons/profile-circle.svg" alt="Image" />
                User List
              </router-link>
            </li>
          </ul>
        </li>
        <li class="nav-item"
          v-if="hasAnyPermission(['SYSTEM_SETTING', 'PERMISSION_LIST', 'WAREHOUSE_LIST', 'CATEGORY_LIST', 'BRAND_LIST', 'CURRENCY_LIST', 'UNIT_LIST', 'BACKUP'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/setting-2.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Settings </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('SYSTEM_SETTING')">
              <router-link to="/system-settings">
                <img src="../../assets/img/icons/setting-3.svg" alt="Image" />
                System Settings
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SYSTEM_SETTING')">
              <router-link to="/pos-settings">
                <img src="../../assets/img/icons/setting-3.svg" alt="Image" />
                POS General Settings
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PERMISSION_LIST')">
              <router-link to="/group-permission">
                <img src="../../assets/img/icons/group.svg" alt="Image" />Group
                Permission
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('WAREHOUSE_LIST')">
              <router-link to="/warehouse-list">
                <img src="../../assets/img/icons/warehouse.svg" alt="Image" />
                Warehouse
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('CATEGORY_LIST')">
              <router-link to="/category-list">
                <img src="../../assets/img/icons/category.svg" alt="Image" />
                Category
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('BRAND_LIST')">
              <router-link to="/brand-list">
                <img src="../../assets/img/icons/brand.svg" alt="Image" />
                Brand
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('CURRENCY_LIST')">
              <router-link to="/currency-list">
                <img src="../../assets/img/icons/currency.svg" alt="Image" />
                Currency
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('UNIT_LIST')">
              <router-link to="/unit-list">
                <img src="../../assets/img/icons/unit.svg" alt="Image" />
                Unit
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('BACKUP')">
              <router-link to="/backup">
                <img src="../../assets/img/icons/backup.svg" alt="Image" />
                Backup
              </router-link>
            </li>
          </ul>
        </li>
        <!-- <li class="nav-item"
          v-if="hasAnyPermission(['PURCHASE_REPORT', 'PURCHASE_PAYMENT_REPORT', 'PURCHASE_RETURN_REPORT', 'SALE_REPORT', 'SALE_PAYMENT_REPORT', 'SALE_RETURN_REPORT', 'PRODUCT_QUANTITY_ALERT', 'PROFIT_AND_LOSS', 'PRODUCT_REPORT', 'STOCK_REPORT', 'WAREHOUSE_REPORT', 'CUSTOMER_REPORT', 'SUPPLIER_REPORT', 'USER_REPORT'])">
          <a href="javascript:void(0)" class="nav-link">
            <img src="../../assets/img/icons/reports.svg" alt="Image" />
            <span class="nav-text d-block fs-14"> Reports </span>
          </a>
          <ul class="sidebar-secondary-menu bg-white list-style">
            <li class="nav-item" v-if="hasPermission('PURCHASE_REPORT')">
              <router-link to="/purchase-report">
                <img src="../../assets/img/icons/purchase-2.svg" alt="Image" />
                Purchase Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PURCHASE_PAYMENT_REPORT')">
              <router-link to="/purchase-payment-report">
                <img src="../../assets/img/icons/purchase-payment.svg" alt="Image" />
                Purchase Payment Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PURCHASE_RETURN_REPORT')">
              <router-link to="/purchase-return-report">
                <img src="../../assets/img/icons/purchase-return-2.svg" alt="Image" />
                Purchase Return Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SALE_REPORT')">
              <router-link to="/sales-report">
                <img src="../../assets/img/icons/money-3.svg" alt="Image" />
                Sales Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SALE_PAYMENT_REPORT')">
              <router-link to="/sales-payment-report">
                <img src="../../assets/img/icons/money-3.svg" alt="Image" />
                Sales Payment Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SALE_RETURN_REPORT')">
              <router-link to="/sales-return-report">
                <img src="../../assets/img/icons/sales-return-2.svg" alt="Image" />
                Sales Return Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PRODUCT_QUANTITY_ALERT')">
              <router-link to="/product-quantity-alert">
                <img src="../../assets/img/icons/quantity-alert.svg" alt="Image" />
                Product Quantity Alert
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PROFIT_AND_LOSS')">
              <router-link to="/profit-loss">
                <img src="../../assets/img/icons/dollar-circle.svg" alt="Image" />
                Profit & Loss
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('PRODUCT_REPORT')">
              <router-link to="/product-report">
                <img src="../../assets/img/icons/create-item.svg" alt="Image" />
                Product Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('STOCK_REPORT')">
              <router-link to="/stock-report">
                <img src="../../assets/img/icons/category.svg" alt="Image" />
                Stock Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('WAREHOUSE_REPORT')">
              <router-link to="/warehouse-report">
                <img src="../../assets/img/icons/warehouse-report.svg" alt="Image" />
                Warehouse Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('CUSTOMER_REPORT')">
              <router-link to="/customer-report">
                <img src="../../assets/img/icons/customer-report.svg" alt="Image" />
                Customer Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('SUPPLIER_REPORT')">
              <router-link to="/supplier-report">
                <img src="../../assets/img/icons/supplier-report.svg" alt="Image" />
                Supplier Report
              </router-link>
            </li>
            <li class="nav-item" v-if="hasPermission('USER_REPORT')">
              <router-link to="/user-report">
                <img src="../../assets/img/icons/user-square.svg" alt="Image" />
                User Report
              </router-link>
            </li>
          </ul>
        </li> -->
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from "@/stores/userStore";

const userStore = useUserStore();

const hasPermission = (permission: string): boolean =>
  userStore.userPermissions.includes(permission);

const hasAnyPermission = (permissions: string[]): boolean =>
  permissions.some((p) => hasPermission(p));
</script>

<style lang="scss" scoped>
.sidebar-area {
  top: 100px;
  left: 0;
  height: 100%;
  z-index: 2;
  transition: var(--transition);

  .sidebar-menu-wrap {
    padding: 40px;
    z-index: 2;

    .sidebar-menu {
      position: relative;
      z-index: 2;

      &>.nav-item {
        margin-bottom: 25px;
        display: block;

        &:last-child {
          margin-bottom: 0;
        }

        &>.nav-link {
          position: relative;
          background-color: #2c3a52;
          text-align: center;
          display: inline-block;
          width: 128px;
          height: 100px;
          border-radius: 4px;
          padding: 19px 5px 18px;
          transition: var(--transition);
          z-index: 1;

          &:after {
            position: absolute;
            bottom: 0;
            left: 0;
            content: "";
            width: 100%;
            height: 0;
            border-radius: 4px;
            background: var(--Purple-Gradient,
                linear-gradient(132deg, #4f46e5 4.27%, #6366f1 100%));
            visibility: hidden;
            opacity: 0;
            transition: var(--transition);
            z-index: -1;
          }

          img {
            display: block;
            margin: 0 auto;
          }

          .nav-text {
            color: #c6c7d3;
            margin-top: 5px;
          }

          &.active {
            &:after {
              visibility: visible;
              opacity: 1;
              height: 100%;
            }
          }
        }

        &.active,
        &:hover {
          &>.nav-link {
            &:after {
              visibility: visible;
              opacity: 1;
              height: 100%;
            }
          }
        }

        &:has(ul.sidebar-secondary-menu li a.active) {
          &>.nav-link {
            &:after {
              visibility: visible;
              opacity: 1;
              height: 100%;
            }
          }
        }

        .sidebar-secondary-menu {
          position: fixed;
          top: 100px;
          left: 208px;
          width: 280px;
          height: calc(100vh - 100px);
          visibility: hidden;
          opacity: 0;
          transition: var(--transition);
          z-index: 0;
          overflow-y: auto;
          box-shadow: 0px 4px 20px 0px rgba(0, 0, 0, 0.06);

          li {
            a {
              font-size: 14px;
              color: var(--primaryColor);
              transition: var(--transition);
              padding: 14px 25px;
              display: block;
              width: 100%;

              img {
                position: relative;
                margin-right: 8px;
                top: -1.5px;
              }

              &.active,
              &:hover {
                background-color: #f3f8ff;
              }
            }
          }
        }

        &:hover {
          .sidebar-secondary-menu {
            visibility: visible;
            opacity: 1;
          }
        }
      }
    }
  }
}

#sidebar_menu_wrap {
  position: relative;
  z-index: 1;
  height: calc(100vh - 100px);
  overflow-y: auto;

  &::-webkit-scrollbar {
    height: 7px;
    width: 7px;
    background: var(--primaryColor);
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.5);
    -webkit-border-radius: 1ex;
    -webkit-box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.75);
  }

  &::-webkit-scrollbar-corner {
    background: #000;
  }
}

@media only screen and (max-width: 575px) {
  .sidebar-area {
    top: 116px;

    .sidebar-menu-wrap {
      padding: 25px 15px;

      .sidebar-menu {
        &>.nav-item {
          margin-bottom: 10px;

          &:last-child {
            margin-bottom: 0;
          }

          &>.nav-link {
            width: 55px;
            height: 55px;
            padding: 14px 5px 12px;

            img {
              max-width: 27px;
            }

            .nav-text {
              display: none !important;
            }
          }

          .sidebar-secondary-menu {
            top: 116px;
            left: 80px;
            width: 250px;
            height: 100vh;

            li {
              a {
                font-size: 14px;
                padding: 14px 20px;
              }
            }
          }
        }
      }
    }
  }

  #sidebar_menu_wrap {
    height: calc(100vh - 116px);
  }
}

@media only screen and (min-width: 576px) and (max-width: 767px) {
  .sidebar-area {
    top: 80px;

    .sidebar-menu-wrap {
      .sidebar-menu {
        &>.nav-item {
          .sidebar-secondary-menu {
            top: 79px;
          }
        }
      }
    }
  }
}

@media only screen and (max-width: 1199px) {}
</style>