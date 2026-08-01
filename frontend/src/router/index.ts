declare module "vue-router" {
  interface RouteMeta {
    requiresAuth?: boolean;
    permission?: string;
  }
}

import { createWebHistory, createRouter, RouteRecordRaw } from "vue-router";
import DashboardPage from "../pages/Dashboard/DashboardPage.vue";
import CreateProductsPage from "../pages/Products/CreateProductsPage.vue";
import ProductsListPage from "../pages/Products/ProductsListPage.vue";
import EditProductsPage from "../pages/Products/EditProductsPage.vue";
import ManageProductToWarehousePage from "@/pages/Products/ManageProductToWarehousePage.vue";
import PrintBarcodePage from "../pages/Products/PrintBarcodePage.vue";
import ProductDetailsPage from "../pages/Products/ProductDetailsPage.vue";
import CreateAdjustmentsPage from "../pages/Adjustments/CreateAdjustmentsPage.vue";
import AdjustmentListPage from "../pages/Adjustments/AdjustmentListPage.vue";
import EditAdjustmentPage from "../pages/Adjustments/EditAdjustmentPage.vue";
// import CreateTransferPage from "../pages/Transfer/CreateTransferPage.vue";
// import TransferListPage from "../pages/Transfer/TransferListPage.vue";
// import EditTransferPage from "../pages/Transfer/EditTransferPage.vue";
import CreateExpensesPage from "../pages/Expenses/CreateExpensesPage.vue";
import ExpensesListPage from "../pages/Expenses/ExpensesListPage.vue";
import EditExpensesPage from "../pages/Expenses/EditExpensesPage.vue";
import ExpenseCategoryPage from "../pages/Expenses/ExpenseCategoryPage.vue";
// import CreateQuotationPage from "../pages/Quotations/CreateQuotationPage.vue";
// import QuotationListPage from "../pages/Quotations/QuotationListPage.vue";
// import EditQuotationPage from "../pages/Quotations/EditQuotationPage.vue";
import CreatePurchasePage from "../pages/Purchases/CreatePurchasePage.vue";
import PurchaseListPage from "../pages/Purchases/PurchaseListPage.vue";
import EditPurchasePage from "../pages/Purchases/EditPurchasePage.vue";
import CreateSalesPage from "../pages/Sales/CreateSalesPage.vue";
import SalesListPage from "../pages/Sales/SalesListPage.vue";
import EditSalesPage from "../pages/Sales/EditSalesPage.vue";
import PosPage from "../pages/Sales/PosPage.vue";
import ShipmentPage from "../pages/Sales/ShipmentPage.vue";
import CreateSalesReturnPage from "../pages/SalesReturn/CreateSalesReturnPage.vue";
import SalesReturnListPage from "../pages/SalesReturn/SalesReturnListPage.vue";
import EditSalesReturnPage from "../pages/SalesReturn/EditSalesReturnPage.vue";
import CreatePurchaseReturnPage from "../pages/PurchaseReturn/CreatePurchaseReturnPage.vue";
import PurchaseReturnListPage from "../pages/PurchaseReturn/PurchaseReturnListPage.vue";
import EditPurchaseReturnPage from "../pages/PurchaseReturn/EditPurchaseReturnPage.vue";
import CustomerListPage from "../pages/People/CustomerListPage.vue";
import SupplierListPage from "../pages/People/SupplierListPage.vue";
import UserListPage from "../pages/People/UserListPage.vue";
import SystemSettingsPage from "../pages/Settings/SystemSettingsPage.vue";
import WarehouseSettingsPage from "@/pages/Settings/WarehouseSettingsPage.vue";
import MyProfilePage from "../pages/Settings/MyProfilePage.vue";
import GroupPermissionPage from "../pages/Settings/GroupPermissionPage.vue";
import CreateGroupPermissionPage from "../pages/Settings/CreateGroupPermissionPage.vue";
import EditGroupPermissionPage from "../pages/Settings/EditGroupPermissionPage.vue";
import WarehousePage from "../pages/Settings/WarehousePage.vue";
import CategoryPage from "../pages/Settings/CategoryPage.vue";
import BrandListPage from "../pages/Settings/BrandListPage.vue";
import CurrncyListPage from "../pages/Settings/CurrncyListPage.vue";
import UnitListPage from "../pages/Settings/UnitListPage.vue";
import BackupPage from "../pages/Settings/BackupPage.vue";
// import PurchaseReportPage from "../pages/Reports/PurchaseReportPage.vue";
// import PurchasePaymentReportPage from "../pages/Reports/PurchasePaymentReportPage.vue";
// import PurchaseReturnReportPage from "../pages/Reports/PurchaseReturnReportPage.vue";
// import SalesReportPage from "../pages/Reports/SalesReportPage.vue";
// import SalesPaymentReportPage from "../pages/Reports/SalesPaymentReportPage.vue";
// import SalesReturnReportPage from "../pages/Reports/SalesReturnReportPage.vue";
// import ProductQuantityAlertPage from "../pages/Reports/ProductQuantityAlertPage.vue";
// import ProfitLossPage from "../pages/Reports/ProfitLossPage.vue";
// import ProductReportPage from "../pages/Reports/ProductReportPage.vue";
// import StockReportPage from "../pages/Reports/StockReportPage.vue";
// import StockReportDetailsPage from "../pages/Reports/StockReportDetailsPage.vue";
// import WarehouseReportPage from "../pages/Reports/WarehouseReportPage.vue";
// import CustomerReportPage from "../pages/Reports/CustomerReportPage.vue";
// import SupplierReportPage from "../pages/Reports/SupplierReportPage.vue";
// import UserReportPage from "../pages/Reports/UserReportPage.vue";
import PaymentGatewayPage from "@/pages/Payments/PaymentGatewayPage.vue";
import LandingPage from "../pages/BestElectronicsShop/LandingPage.vue";
import ShopRightSidebarPage from "../pages/BestElectronicsShop/ShopRightSidebarPage.vue";
import ShopLeftSidebarPage from "../pages/BestElectronicsShop/ShopLeftSidebarPage.vue";
import ShopGridPage from "../pages/BestElectronicsShop/ShopGridPage.vue";
import ShopDetailsPage from "../pages/BestElectronicsShop/ShopDetailsPage.vue";
import CartPage from "../pages/BestElectronicsShop/CartPage.vue";
import WishlistPage from "../pages/BestElectronicsShop/WishlistPage.vue";
import CheckoutPage from "../pages/BestElectronicsShop/CheckoutPage.vue";
import LoginPage from "../pages/BestElectronicsShop/LoginPage.vue";
import SignupPage from "../pages/BestElectronicsShop/SignupPage.vue";
import LogoutPage from "../pages/BestElectronicsShop/LogoutPage.vue";
import TermsConditionsPage from "../pages/BestElectronicsShop/TermsConditionsPage.vue";
import OfferPage from "../pages/BestElectronicsShop/OfferPage.vue";
import FaqPage from "../pages/BestElectronicsShop/FaqPage.vue";
import ContactPage from "../pages/BestElectronicsShop/ContactPage.vue";
import { getAuthToken } from "@/services/authService";
import { useUserStore } from "@/stores/userStore";
import NotFoundPage from "@/pages/NotFound/NotFoundPage.vue";
import CreateWarehousePage from "@/pages/Settings/CreateWarehousePage.vue";
import PosSettingsPage from "@/pages/Settings/PosSettingsPage.vue";
import CreateUserPage from "@/pages/People/CreateUserPage.vue";
import UpdateUserPage from "@/pages/People/UpdateUserPage.vue";
import MobileScannerPage from "@/pages/MobileScannerPage.vue";
import HomePage from "@/pages/Onboarding/HomePage.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/not-found",
    name: "NotFoundPage",
    component: NotFoundPage,
    meta: { requiresAuth: false },
  },
  {
    path: "/dashboard",
    name: "DashboardPage",
    component: DashboardPage,
    meta: { requiresAuth: true },
  },
  {
    path: "/mobile-scanner",
    name: "MobileScannerPage",
    component: MobileScannerPage,
    meta: {
      requiresAuth: false,
      layout: "empty",
    },
  },
  {
    path: "/create-product",
    name: "CreateProductsPage",
    component: CreateProductsPage,
    meta: { requiresAuth: true, permission: "USER_CREATE" },
  },
  {
    path: "/create-user",
    name: "CreateUserPage",
    component: CreateUserPage,
    meta: { requiresAuth: true, permission: "USER_EDIT" },
  },
  {
    path: "/update-user/:id",
    name: "UpdateUserPage",
    component: UpdateUserPage,
    meta: { requiresAuth: true, permission: "PRODUCT_CREATE" },
  },
  {
    path: "/products/manage-to-warehouse",
    name: "ManageProductToWarehouse",
    component: ManageProductToWarehousePage,
    meta: { requiresAuth: true, permission: "PRODUCT_CREATE" },
  },
  {
    path: "/product-list",
    name: "ProductsListPage",
    component: ProductsListPage,
    meta: { requiresAuth: true, permission: "PRODUCT_LIST" },
  },
  {
    path: "/edit-product/:id",
    name: "EditProductsPage",
    component: EditProductsPage,
    meta: { requiresAuth: true, permission: "PRODUCT_EDIT" },
  },
  {
    path: "/product-details/:id",
    name: "ProductDetailsPage",
    component: ProductDetailsPage,
    meta: { requiresAuth: true, permission: "PRODUCT_VIEW" },
  },
  {
    path: "/print-barcode",
    name: "PrintBarcodePage",
    component: PrintBarcodePage,
    meta: { requiresAuth: true, permission: "PRINT_BARCODE" },
  },
  {
    path: "/create-adjustment",
    name: "CreateAdjustmentsPage",
    component: CreateAdjustmentsPage,
    meta: { requiresAuth: true, permission: "ADJUSTMENT_CREATE" },
  },
  {
    path: "/adjustment-list",
    name: "AdjustmentListPage",
    component: AdjustmentListPage,
    meta: { requiresAuth: true, permission: "ADJUSTMENT_LIST" },
  },
  {
    path: "/edit-adjustment/:id",
    name: "EditAdjustmentPage",
    component: EditAdjustmentPage,
    meta: { requiresAuth: true, permission: "ADJUSTMENT_EDIT" },
  },
  // {
  //   path: "/create-transfer",
  //   name: "CreateTransferPage",
  //   component: CreateTransferPage,
  //   meta: { requiresAuth: true, permission: "TRANSFER_CREATE" },
  // },
  // {
  //   path: "/transfer-list",
  //   name: "TransferListPage",
  //   component: TransferListPage,
  //   meta: { requiresAuth: true, permission: "TRANSFER_LIST" },
  // },
  // {
  //   path: "/edit-transfer/:id",
  //   name: "EditTransferPage",
  //   component: EditTransferPage,
  //   meta: { requiresAuth: true, permission: "TRANSFER_EDIT" },
  // },
  {
    path: "/create-expense",
    name: "CreateExpensesPage",
    component: CreateExpensesPage,
    meta: { requiresAuth: true, permission: "EXPENSE_CREATE" },
  },
  {
    path: "/expense-list",
    name: "ExpensesListPage",
    component: ExpensesListPage,
    meta: { requiresAuth: true, permission: "EXPENSE_LIST" },
  },
  {
    path: "/edit-expense",
    name: "EditExpensesPage",
    component: EditExpensesPage,
    meta: { requiresAuth: true, permission: "EXPENSE_EDIT" },
  },
  {
    path: "/expense-category",
    name: "ExpenseCategoryPage",
    component: ExpenseCategoryPage,
    meta: { requiresAuth: true, permission: "EXPENSE_LIST" },
  },
  // {
  //   path: "/create-quotation",
  //   name: "CreateQuotationPage",
  //   component: CreateQuotationPage,
  //   meta: { requiresAuth: true, permission: "QUOTATION_CREATE" },
  // },
  // {
  //   path: "/quotation-list",
  //   name: "QuotationListPage",
  //   component: QuotationListPage,
  //   meta: { requiresAuth: true, permission: "QUOTATION_LIST" },
  // },
  // {
  //   path: "/edit-quotation",
  //   name: "EditQuotationPage",
  //   component: EditQuotationPage,
  //   meta: { requiresAuth: true, permission: "QUOTATION_EDIT" },
  // },
  {
    path: "/create-purchase",
    name: "CreatePurchasePage",
    component: CreatePurchasePage,
    meta: { requiresAuth: true, permission: "PURCHASE_CREATE" },
  },
  {
    path: "/purchase-list",
    name: "PurchaseListPage",
    component: PurchaseListPage,
    meta: { requiresAuth: true, permission: "PURCHASE_LIST" },
  },
  {
    path: "/edit-purchase/:id",
    name: "EditPurchasePage",
    component: EditPurchasePage,
    meta: { requiresAuth: true, permission: "PURCHASE_EDIT" },
  },
  {
    path: "/create-sales",
    name: "CreateSalesPage",
    component: CreateSalesPage,
    meta: { requiresAuth: true, permission: "SALE_CREATE" },
  },
  {
    path: "/sales-list",
    name: "SalesListPage",
    component: SalesListPage,
    meta: { requiresAuth: true, permission: "SALE_LIST" },
  },
  {
    path: "/edit-sales/:id",
    name: "EditSalesPage",
    component: EditSalesPage,
    meta: { requiresAuth: true, permission: "SALE_EDIT" },
  },
  {
    path: "/pos",
    name: "PosPage",
    component: PosPage,
    meta: { requiresAuth: true, permission: "SALE_POS" },
  },
  // {
  //   path: "/shipment-list",
  //   name: "ShipmentPage",
  //   component: ShipmentPage,
  //   meta: { requiresAuth: true, permission: "SALE_SHIPMENT" },
  // },
  {
    path: "/create-sales-return",
    name: "CreateSalesReturnPage",
    component: CreateSalesReturnPage,
    meta: { requiresAuth: true, permission: "SALE_RETURN_CREATE" },
  },
  {
    path: "/sales-return-list",
    name: "SalesReturnListPage",
    component: SalesReturnListPage,
    meta: { requiresAuth: true, permission: "SALE_RETURN_LIST" },
  },
  {
    path: "/edit-sales-return/:id",
    name: "EditSalesReturnPage",
    component: EditSalesReturnPage,
    meta: { requiresAuth: true, permission: "SALE_RETURN_EDIT" },
  },
  {
    path: "/create-purchase-return",
    name: "CreatePurchaseReturnPage",
    component: CreatePurchaseReturnPage,
    meta: { requiresAuth: true, permission: "PURCHASE_RETURN_CREATE" },
  },
  {
    path: "/purchase-return-list",
    name: "PurchaseReturnListPage",
    component: PurchaseReturnListPage,
    meta: { requiresAuth: true, permission: "PURCHASE_RETURN_LIST" },
  },
  {
    path: "/edit-purchase-return",
    name: "EditPurchaseReturnPage",
    component: EditPurchaseReturnPage,
    meta: { requiresAuth: true, permission: "PURCHASE_RETURN_EDIT" },
  },
  {
    path: "/customer-list",
    name: "CustomerListPage",
    component: CustomerListPage,
    meta: { requiresAuth: true, permission: "CUSTOMER_LIST" },
  },
  {
    path: "/supplier-list",
    name: "SupplierListPage",
    component: SupplierListPage,
    meta: { requiresAuth: true, permission: "SUPPLIER_LIST" },
  },
  {
    path: "/user-list",
    name: "UserListPage",
    component: UserListPage,
    meta: { requiresAuth: true, permission: "USER_LIST" },
  },
  {
    path: "/system-settings",
    name: "SystemSettingsPage",
    component: SystemSettingsPage,
    meta: { requiresAuth: true, permission: "SYSTEM_SETTING" },
  },
  {
    path: "/pos-settings",
    name: "PosSettingsPage",
    component: PosSettingsPage,
    meta: { requiresAuth: true, permission: "SYSTEM_SETTING" },
  },
  {
    path: "/create-warehouse",
    name: "CreateWarehousePage",
    component: CreateWarehousePage,
    meta: { requiresAuth: true, permission: "SYSTEM_SETTING" },
  },
  {
    path: "/warehouse-settings/:id",
    name: "WarehouseSettingsPage",
    component: WarehouseSettingsPage,
    meta: { requiresAuth: true, permission: "SYSTEM_SETTING" },
  },
  {
    path: "/my-profile",
    name: "MyProfilePage",
    component: MyProfilePage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/group-permission",
    name: "GroupPermissionPage",
    component: GroupPermissionPage,
    meta: { requiresAuth: true, permission: "PERMISSION_LIST" },
  },
  {
    path: "/create-group-permission",
    name: "CreateGroupPermissionPage",
    component: CreateGroupPermissionPage,
    meta: { requiresAuth: true, permission: "PERMISSION_CREATE" },
  },
  {
    path: "/edit-group-permission/:id",
    name: "EditGroupPermissionPage",
    component: EditGroupPermissionPage,
    meta: { requiresAuth: true, permission: "PERMISSION_EDIT" },
  },
  {
    path: "/warehouse-list",
    name: "WarehousePage",
    component: WarehousePage,
    meta: { requiresAuth: true, permission: "WAREHOUSE_LIST" },
  },
  {
    path: "/category-list",
    name: "CategoryPage",
    component: CategoryPage,
    meta: { requiresAuth: true, permission: "CATEGORY_LIST" },
  },
  {
    path: "/brand-list",
    name: "BrandListPage",
    component: BrandListPage,
    meta: { requiresAuth: true, permission: "BRAND_LIST" },
  },
  {
    path: "/currency-list",
    name: "CurrncyListPage",
    component: CurrncyListPage,
    meta: { requiresAuth: true, permission: "CURRENCY_LIST" },
  },
  {
    path: "/unit-list",
    name: "UnitListPage",
    component: UnitListPage,
    meta: { requiresAuth: true, permission: "UNIT_LIST" },
  },
  {
    path: "/backup",
    name: "BackupPage",
    component: BackupPage,
    meta: { requiresAuth: true, permission: "BACKUP" },
  },
  // {
  //   path: "/purchase-report",
  //   name: "PurchaseReportPage",
  //   component: PurchaseReportPage,
  //   meta: { requiresAuth: true, permission: "PURCHASE_REPORT" },
  // },
  // {
  //   path: "/purchase-payment-report",
  //   name: "PurchasePaymentReportPage",
  //   component: PurchasePaymentReportPage,
  //   meta: { requiresAuth: true, permission: "PURCHASE_PAYMENT_REPORT" },
  // },
  // {
  //   path: "/purchase-return-report",
  //   name: "PurchaseReturnReportPage",
  //   component: PurchaseReturnReportPage,
  //   meta: { requiresAuth: true, permission: "PURCHASE_RETURN_REPORT" },
  // },
  // {
  //   path: "/sales-report",
  //   name: "SalesReportPage",
  //   component: SalesReportPage,
  //   meta: { requiresAuth: true, permission: "SALE_REPORT" },
  // },
  // {
  //   path: "/sales-payment-report",
  //   name: "SalesPaymentReportPage",
  //   component: SalesPaymentReportPage,
  //   meta: { requiresAuth: true, permission: "SALE_PAYMENT_REPORT" },
  // },
  // {
  //   path: "/sales-return-report",
  //   name: "SalesReturnReportPage",
  //   component: SalesReturnReportPage,
  //   meta: { requiresAuth: true, permission: "SALE_RETURN_REPORT" },
  // },
  // {
  //   path: "/product-quantity-alert",
  //   name: "ProductQuantityAlertPage",
  //   component: ProductQuantityAlertPage,
  //   meta: { requiresAuth: true, permission: "PRODUCT_QUANTITY_ALERT" },
  // },
  // {
  //   path: "/profit-loss",
  //   name: "ProfitLossPage",
  //   component: ProfitLossPage,
  //   meta: { requiresAuth: true, permission: "PROFIT_AND_LOSS" },
  // },
  // {
  //   path: "/product-report",
  //   name: "ProductReportPage",
  //   component: ProductReportPage,
  //   meta: { requiresAuth: true, permission: "PRODUCT_REPORT" },
  // },
  // {
  //   path: "/stock-report",
  //   name: "StockReportPage",
  //   component: StockReportPage,
  //   meta: { requiresAuth: true, permission: "STOCK_REPORT" },
  // },
  // {
  //   path: "/stock-report-details",
  //   name: "StockReportDetailsPage",
  //   component: StockReportDetailsPage,
  //   meta: { requiresAuth: true, permission: "" },
  // },
  // {
  //   path: "/warehouse-report",
  //   name: "WarehouseReportPage",
  //   component: WarehouseReportPage,
  //   meta: { requiresAuth: true, permission: "WAREHOUSE_REPORT" },
  // },
  // {
  //   path: "/customer-report",
  //   name: "CustomerReportPage",
  //   component: CustomerReportPage,
  //   meta: { requiresAuth: true, permission: "CUSTOMER_REPORT" },
  // },
  // {
  //   path: "/supplier-report",
  //   name: "SupplierReportPage",
  //   component: SupplierReportPage,
  //   meta: { requiresAuth: true, permission: "SUPPLIER_REPORT" },
  // },
  // {
  //   path: "/user-report",
  //   name: "UserReportPage",
  //   component: UserReportPage,
  //   meta: { requiresAuth: true, permission: "USER_REPORT" },
  // },
  {
    path: "/payment-gateway",
    name: "PaymentGatewayPage",
    component: PaymentGatewayPage,
    meta: { requiresAuth: true, permission: "SYSTEM_SETTING_PAYMENT_GATEWAY" },
  },
  {
    path: "/home-page",
    name: "HomePage",
    component: HomePage,
    meta: { requiresAuth: false, permission: "" },
  },
  {
    path: "/landing-page",
    name: "LandingPage",
    component: LandingPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/shop-right-sidebar",
    name: "ShopRightSidebarPage",
    component: ShopRightSidebarPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/shop-left-sidebar",
    name: "ShopLeftSidebarPage",
    component: ShopLeftSidebarPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/shop-grid",
    name: "ShopGridPage",
    component: ShopGridPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/shop-details",
    name: "ShopDetailsPage",
    component: ShopDetailsPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/cart",
    name: "CartPage",
    component: CartPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/wishlist",
    name: "WishlistPage",
    component: WishlistPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/checkout",
    name: "CheckoutPage",
    component: CheckoutPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/login",
    name: "LoginPage",
    component: LoginPage,
    meta: { requiresAuth: false, permission: "" },
  },
  {
    path: "/signup",
    name: "SignupPage",
    component: SignupPage,
    meta: { requiresAuth: false, permission: "" },
  },
  {
    path: "/logout",
    name: "LogoutPage",
    component: LogoutPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/terms-conditions",
    name: "TermsConditionsPage",
    component: TermsConditionsPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/offers",
    name: "OfferPage",
    component: OfferPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/faq",
    name: "FaqPage",
    component: FaqPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/contact",
    name: "ContactPage",
    component: ContactPage,
    meta: { requiresAuth: true, permission: "" },
  },
  {
    path: "/",
    name: "HomePage",
    component: () => import("@/pages/Onboarding/HomePage.vue"),
    meta: {
      layout: "PublicLayout",
      requiresAuth: false,
    },
  },
  // {
  //   path: "/signup",
  //   name: "Signup",
  //   component: () => import("@/pages/Onboarding/SignupStep.vue"),
  //   meta: {
  //     layout: "PublicLayout",
  //     requiresAuth: false,
  //   },
  // },
  {
    path: "/setup",
    name: "SetupWizard",
    component: () => import("@/pages/Onboarding/SetupWizard.vue"),
    meta: {
      layout: "OnboardingLayout",
      requiresAuth: false,
      requiresOnboarding: true,
    },
  },
  // {
  //   path: "/setup/company",
  //   name: "CompanySetup",
  //   component: () => import("@/pages/Onboarding/CompanySetup.vue"),
  //   meta: {
  //     layout: "OnboardingLayout",
  //     requiresAuth: false,
  //     requiresOnboarding: true,
  //   },
  // },
  // {
  //   path: "/setup/plans",
  //   name: "PlansPage",
  //   component: () => import("@/pages/Onboarding/PlansPage.vue"),
  //   meta: {
  //     layout: "OnboardingLayout",
  //     requiresAuth: false,
  //     requiresOnboarding: true,
  //   },
  // },
  // {
  //   path: "/setup/warehouse",
  //   name: "WarehouseSetup",
  //   component: () => import("@/pages/Onboarding/WarehouseSetup.vue"),
  //   meta: {
  //     layout: "OnboardingLayout",
  //     requiresAuth: true,
  //     requiresOnboarding: true,
  //   },
  // },
  // {
  //   path: "/setup/configuration",
  //   name: "Configuration",
  //   component: () => import("@/pages/Onboarding/Configuration.vue"),
  //   meta: {
  //     layout: "OnboardingLayout",
  //     requiresAuth: true,
  //     requiresOnboarding: true,
  //   },
  // },
  // {
  //   path: "/setup/complete",
  //   name: "SetupComplete",
  //   component: () => import("@/pages/Onboarding/SetupComplete.vue"),
  //   meta: {
  //     layout: "OnboardingLayout",
  //     requiresAuth: true,
  //     requiresOnboarding: true,
  //   },
  // },
  // {
  //   path: "/verify-email",
  //   name: "EmailVerification",
  //   component: () => import("@/pages/Onboarding/EmailVerification.vue"),
  //   meta: {
  //     layout: "PublicLayout",
  //     requiresAuth: false,
  //   },
  // },
  // {
  //   path: "/payment",
  //   name: "PaymentPage",
  //   component: () => import("@/pages/Onboarding/PaymentPage.vue"),
  //   meta: {
  //     layout: "OnboardingLayout",
  //     requiresAuth: true,
  //     requiresOnboarding: true,
  //   },
  // },
];

const router = createRouter({
  history: createWebHistory(),
  linkExactActiveClass: "active",
  routes,
  scrollBehavior() {
    return { top: 0, behavior: "smooth" };
  },
});

router.beforeEach((to, from, next) => {
  const authToken = getAuthToken();
  const userStore = useUserStore();

  // Not authenticated
  if (to.meta.requiresAuth && !authToken) {
    return next({ name: "LoginPage" });
  }

  // If a permission is required, check it
  const requiredPermission = to.meta.permission as string | undefined;
  if (requiredPermission) {
    const hasPermission =
      userStore.userPermissions.includes(requiredPermission);

    if (!hasPermission) {
      console.warn(`Access denied. Missing permission: ${requiredPermission}`);
      return next({ name: "DashboardPage" }); // or redirect to an "AccessDeniedPage"
    }
  }

  return next();
});

export default router;
