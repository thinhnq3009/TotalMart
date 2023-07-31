package eco.mart.totalmart.vnpay;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static eco.mart.totalmart.vnpay.VnpParameter.*;


public class VNPayGenerator {

    @Setter
    @Getter
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    @Setter
    @Getter
    public static String vnp_Returnurl = "http://localhost:8080/payment/done";

    @Setter
    @Getter
    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    @Setter
    @Getter
    private int expiresInMinutes = 15;

    private final String SECURE_HASH = "vnp_SecureHash";

    @Setter
    private String datetimePattern = "yyyyMMddHHmmss";



    private final VnpParameter[] REQUIRED_PARAMS = new VnpParameter[]{
            CURR_CODE,
            ORDER_TYPE,
            RETURN_URL,
            TMN_CODE,
            TXN_REF,
            ORDER_INFO,
            AMOUNT,
            IP_ADDR,
            LOCALE,
            COMMAND,
            EXPIRE_DATE,
            CREATE_DATE,
            VERSION,
    };

    private final Map<String, String> params = new HashMap<>();

    @Setter
    private String vnpHashSecretKey = "";

    private boolean isGenerated = false;
    private String payUrl = "";

    public VNPayGenerator(String vnpHashSecretKey, String TmnCode) {
        initDefaultValue(TmnCode);
        this.vnpHashSecretKey = vnpHashSecretKey;
    }

    public VNPayGenerator(String TmnCode) {
        initDefaultValue(TmnCode);
    }

    public VNPayGenerator() {
        initDefaultValue("");
    }

    private void initDefaultValue(String TmnCode) {
        setParameter(LOCALE, "vn");
        setParameter(VERSION, "2.0.0");
        setParameter(COMMAND, "pay");
        setParameter(CURR_CODE, "VND");
        setParameter(TMN_CODE, TmnCode);
    }


    public void setHashSecretKey(String key) {
        this.vnpHashSecretKey = key;
    }

    public void setAmount(long amount) {
        setParameter(AMOUNT, String.valueOf(amount));
    }


    public void setIpAddress(@NonNull HttpServletRequest request) {
        String ipAddr = request.getHeader("X-FORWARDED-FOR");
        if (ipAddr == null) {
            ipAddr = request.getRemoteAddr();
        }
        setParameter(IP_ADDR, ipAddr);
    }

    public void setReturnUrl(String url) {
        setParameter(RETURN_URL, url);
    }

    public void setOrderInfo(String orderInfo) {
        setParameter(ORDER_INFO, orderInfo);
    }

    public void setOrderType(String orderType) {
        setParameter(ORDER_TYPE, orderType);
    }

    public void setTxnRef(String txnRef) {
        setParameter(TXN_REF, txnRef);
    }

    private void validate() {
        // Validate required params
        List<String> missParam = new ArrayList<>();

        for (VnpParameter param : REQUIRED_PARAMS) {
            if (!params.containsKey(param.getValue())) {
                missParam.add(param.name());
            }
        }

        if (!missParam.isEmpty()) {
            throw new RuntimeException("Missing required params: " + Arrays.toString(missParam.toArray()));
        }

        // Validate hash secret key
        if (vnpHashSecretKey.isEmpty()) {
            throw new RuntimeException("Missing hash secret key");
        }

        //Validate TmnCode
        if (params.get(TMN_CODE.getValue()).isEmpty()) {
            throw new RuntimeException("Missing TmnCode");
        }
    }

    public void setParameter(@NonNull VnpParameter param, String value) {
        params.put(param.getValue(), value);
    }

    public void setParameters(@NonNull Map<VnpParameter, String> map) {
        for (Map.Entry<VnpParameter, String> entry : map.entrySet()) {
            setParameter(entry.getKey(), entry.getValue());
        }
    }

    private String toQueryUrl() {

        Set<String> keys = params.keySet();

        Iterator<String> iterator = keys.iterator();

        StringBuilder sb = new StringBuilder();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);

            sb
                    .append(URLEncoder.encode(key, StandardCharsets.US_ASCII))
                    .append("=")
                    .append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

            if (iterator.hasNext()) {
                sb.append("&");
            }

        }


        return sb.toString();

    }

    private String getDateTimeFromNow(int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minutes);
        SimpleDateFormat sdf = new SimpleDateFormat(datetimePattern);
        return sdf.format(cal.getTime());
    }

    public boolean isSetParameter(@NonNull VnpParameter param) {
        return params.containsKey(param.getValue()) && !params.get(param.getValue()).isEmpty();
    }

    public Date getExpiredTime() {
        if (isSetParameter(EXPIRE_DATE)) {
            SimpleDateFormat sdf = new SimpleDateFormat(datetimePattern);
            String timeString = params.get(EXPIRE_DATE.getValue());
            try {
                return sdf.parse(timeString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }


    public String generateUrl() {

        if (isGenerated) return payUrl;

        if (!isSetParameter(CREATE_DATE)) {
            setParameter(CREATE_DATE, getDateTimeFromNow(0));
        }

        if (!isSetParameter(EXPIRE_DATE)) {
            setParameter(EXPIRE_DATE, getDateTimeFromNow(expiresInMinutes));
        }

        //Validate required params and hash secret key
        validate();

        //Set time for hash
        String secureHash = Config.hashAllFields(params, vnpHashSecretKey);

        params.put(SECURE_HASH, secureHash);

        isGenerated = true;

        payUrl = vnp_PayUrl + "?" + toQueryUrl();

        return payUrl;

    }


}
