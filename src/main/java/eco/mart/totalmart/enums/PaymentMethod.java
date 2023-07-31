package eco.mart.totalmart.enums;

import eco.mart.totalmart.defaultes.DefaultValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum PaymentMethod {

    COD(
            "Thanh toán khi nhận hàng",
            "COD",
            DefaultValue.DEFAULT_IMG_PAYMENT_METHOD,
            "Thanh toán khi nhận hàng"),
    VN_PAY(
            "VN Pay",
            "VN Pay",
            DefaultValue.VNPAY_IMG_PAYMENT_METHOD,
            "Chọn phương thức thanh toán Quét mã và app ngân hàng đang sử dụng để thanh toán đơn giản và thuận tiện nhất.");

    @Getter
    private final String name;

    @Getter
    private final String sortName;

    @Getter
    private final String image;

    @Getter
    private final String description;
}
