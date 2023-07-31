package eco.mart.totalmart.vnpay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum VnpParameter {
    INV_CUSTOMER("vnp_Inv_Customer"),
    INV_ADDRESS("vnp_Inv_Address"),
    CURR_CODE("vnp_CurrCode"),
    RETURN_URL("vnp_ReturnUrl"),
    INV_COMPANY("vnp_Inv_Company"),
    TMN_CODE("vnp_TmnCode"),
    TXN_REF("vnp_TxnRef"),
    AMOUNT("vnp_Amount"),
    BILL_CITY("vnp_Bill_City"),
    INV_EMAIL("vnp_Inv_Email"),
    LOCALE("vnp_Locale"),
    BILL_EMAIL("vnp_Bill_Email"),
    BILL_MOBILE("vnp_Bill_Mobile"),
    EXPIRE_DATE("vnp_ExpireDate"),
    BILL_LAST_NAME("vnp_Bill_LastName"),
    CREATE_DATE("vnp_CreateDate"),
    VERSION("vnp_Version"),
    BANK_CODE("vnp_BankCode"),
    INV_TYPE("vnp_Inv_Type"),
    BILL_STATE("vnp_Bill_State"),
    ORDER_TYPE("vnp_OrderType"),
    BILL_FIRST_NAME("vnp_Bill_FirstName"),
    INV_TAX_CODE("vnp_Inv_Taxcode"),
    ORDER_INFO("vnp_OrderInfo"),
    IP_ADDR("vnp_IpAddr"),
    COMMAND("vnp_Command"),
    BILL_COUNTRY("vnp_Bill_Country"),
    INV_PHONE("vnp_Inv_Phone");

    @Getter
    final String value;

}
