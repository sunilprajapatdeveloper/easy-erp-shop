export interface PermissionGroup {
  name: string;
  children: { id: number; name: string }[];
}

export const PERMISSIONS: PermissionGroup[] = [
  {
    name: "DASHBOARD",
    children: [
      { id: 2, name: "STATUS" },
      { id: 3, name: "TOP_SELLING_PRODUCT_CHART" },
      { id: 4, name: "THIS_WEEK_SALES_PURCHASE_CHART" },
      { id: 5, name: "STOCK_ALERT" },
      { id: 6, name: "TOP_CUSTOMER_CHART" },
      { id: 7, name: "SALES_TARGET_CHART" },
      { id: 8, name: "PAYMENT_SENT_RECEIVED_CHART" },
      { id: 9, name: "RECENT_INVOICES" },
      { id: 10, name: "RECENT_SALES" },
    ],
  },
  {
    name: "PRODUCTS",
    children: [
      { id: 11, name: "CREATE" },
      { id: 12, name: "LIST" },
      { id: 13, name: "VIEW" },
      { id: 14, name: "EDIT" },
      { id: 15, name: "DELETE" },
      { id: 16, name: "PRINT_BARCODE" },
    ],
  },
  {
    name: "ADJUSTMENTS",
    children: [
      { id: 17, name: "CREATE" },
      { id: 18, name: "LIST" },
      { id: 19, name: "VIEW" },
      { id: 20, name: "EDIT" },
      { id: 21, name: "DELETE" },
    ],
  },
  {
    name: "TRANSFER",
    children: [
      { id: 22, name: "CREATE" },
      { id: 23, name: "LIST" },
      { id: 24, name: "VIEW" },
      { id: 25, name: "EDIT" },
      { id: 26, name: "DELETE" },
    ],
  },
  {
    name: "EXPENSES",
    children: [
      { id: 27, name: "CREATE" },
      { id: 28, name: "LIST" },
      { id: 29, name: "VIEW" },
      { id: 30, name: "EDIT" },
      { id: 31, name: "DELETE" },
    ],
  },
  {
    name: "QUOTATIONS",
    children: [
      { id: 32, name: "CREATE" },
      { id: 33, name: "LIST" },
      { id: 34, name: "VIEW" },
      { id: 35, name: "EDIT" },
      { id: 36, name: "DELETE" },
    ],
  },
  {
    name: "PURCHASE",
    children: [
      { id: 37, name: "CREATE" },
      { id: 38, name: "LIST" },
      { id: 39, name: "VIEW" },
      { id: 40, name: "EDIT" },
      { id: 41, name: "DELETE" },
    ],
  },
  {
    name: "PURCHASE_RETUN",
    children: [
      { id: 54, name: "CREATE" },
      { id: 55, name: "LIST" },
      { id: 56, name: "VIEW" },
      { id: 57, name: "EDIT" },
      { id: 58, name: "DELETE" },
    ],
  },
  {
    name: "SALES",
    children: [
      { id: 42, name: "CREATE" },
      { id: 43, name: "LIST" },
      { id: 44, name: "VIEW" },
      { id: 45, name: "EDIT" },
      { id: 46, name: "DELETE" },
      { id: 47, name: "POS" },
      { id: 48, name: "SHIPMENT" },
    ],
  },
  {
    name: "SALES_RETURN",
    children: [
      { id: 49, name: "CREATE" },
      { id: 50, name: "LIST" },
      { id: 51, name: "VIEW" },
      { id: 52, name: "EDIT" },
      { id: 53, name: "DELETE" },
    ],
  },
  {
    name: "USERS_MANGEMENT",
    children: [
      { id: 69, name: "CREATE" },
      { id: 70, name: "LIST" },
      { id: 71, name: "VIEW" },
      { id: 72, name: "EDIT" },
      { id: 73, name: "DELETE" },
    ],
  },
  {
    name: "SUPPLIER_LIST",
    children: [
      { id: 64, name: "CREATE" },
      { id: 65, name: "LIST" },
      { id: 66, name: "VIEW" },
      { id: 67, name: "EDIT" },
      { id: 68, name: "DELETE" },
    ],
  },
  {
    name: "CUSTOMER_LIST",
    children: [
      { id: 59, name: "CREATE" },
      { id: 60, name: "LIST" },
      { id: 61, name: "VIEW" },
      { id: 62, name: "EDIT" },
      { id: 63, name: "DELETE" },
    ],
  },
  {
    name: "SYSTEM_SETTING",
    children: [
      { id: 75, name: "POS_RECEIPT" },
      { id: 76, name: "PAYMENT_GATEWAY" },
      { id: 77, name: "SMS_CONFIGURATION" },
      { id: 78, name: "SMTP_CONFIGURATION" },
      { id: 79, name: "CLEAR_CAHCE" },
    ],
  },
  {
    name: "CATEGORY_SETTING",
    children: [
      { id: 88, name: "LIST" },
      { id: 89, name: "CREATE" },
      { id: 90, name: "EDIT" },
      { id: 91, name: "DELETE" },
    ],
  },
  {
    name: "BRAND_SETTING",
    children: [
      { id: 92, name: "LIST" },
      { id: 93, name: "CREATE" },
      { id: 94, name: "EDIT" },
      { id: 95, name: "DELETE" },
    ],
  },
  {
    name: "CURRENCY_SETTING",
    children: [
      { id: 96, name: "LIST" },
      { id: 97, name: "CREATE" },
      { id: 98, name: "EDIT" },
      { id: 99, name: "DELETE" },
    ],
  },
  {
    name: "WAREHOUSE_SETTING",
    children: [
      { id: 84, name: "LIST" },
      { id: 85, name: "CREATE" },
      { id: 86, name: "EDIT" },
      { id: 87, name: "DELETE" },
    ],
  },
  {
    name: "UNIT_SETTING",
    children: [
      { id: 100, name: "LIST" },
      { id: 101, name: "CREATE" },
      { id: 102, name: "EDIT" },
      { id: 103, name: "DELETE" },
    ],
  },
  {
    name: "BACKUP_SETTING",
    children: [
      { id: 105, name: "GENERATE" },
      { id: 106, name: "EDIT" },
      { id: 107, name: "DELETE" },
    ],
  },
  {
    name: "REPORTS",
    children: [
      { id: 108, name: "PURCHASE" },
      { id: 109, name: "PURCHASE_PAYMENT" },
      { id: 110, name: "PURCHASE_RETURN" },
      { id: 111, name: "SALES" },
      { id: 112, name: "SALES_PAYMENT" },
      { id: 113, name: "SALES_RETURN" },
      { id: 114, name: "PRODUCT_QUANTITY_ALERT" },
      { id: 115, name: "PROFIT_AND_LOSS" },
      { id: 116, name: "PRODUCTS" },
      { id: 117, name: "STOCK" },
      { id: 118, name: "WAREHOUSE" },
      { id: 119, name: "CUSTOMER" },
      { id: 120, name: "SUPPLIER" },
      { id: 121, name: "USER" },
    ],
  },
];
