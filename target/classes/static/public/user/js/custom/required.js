function moneyFormat(price) {
    if (typeof price !== "number") price = parseInt(price);
    return price.toLocaleString('vi', {style: 'currency', currency: 'VND'});
}

const GHN_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api"
const GHN_TOKEN = "d2271479-e6a8-11ed-bc91-ba0234fcde32"