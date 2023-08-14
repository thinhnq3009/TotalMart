package eco.mart.totalmart.enums;

public enum FilterProduct {
    NEWEST("Mới nhất"),
    PRICE_ASC("Giá tăng dần"),
    PRICE_DESC("Giá giảm dần"),
    NAME_ASC("Tên A-Z"),
    NAME_DESC("Tên Z-A");

    private final String value;

    FilterProduct(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
