package eco.mart.totalmart.enums;

public enum DefaultData {

    CATEGORY_IMAGE("/public/user/images/icons/snacks.svg"),
    PRODUCT_IMAGE("/public/user/images/icons/snacks.svg");

    private String value;

    DefaultData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
