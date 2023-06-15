package eco.mart.totalmart.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import eco.mart.totalmart.entities.Address;
import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.Product;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ShippingFeeService {

    private final Logger logger = LoggerFactory.getLogger(ShippingFeeService.class);

    public String getShippingFee(List<Cart> carts, Address address) {

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Token", "d2271479-e6a8-11ed-bc91-ba0234fcde32");

        String jsonData = RequiredData.getJson(address, carts);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        return null;

    }


    @Data
    private static class RequiredData {
        int shop_id;
        int to_district_id;
        String to_ward_code;
        int service_id;
        int height;
        int length;
        int weight;
        int width;


        public  RequiredData (Address address, List<Cart> products) {
            this.shop_id = 124096;
            int volume = products
                    .stream()
                    .map(p -> p.getProduct().getLength() * p.getProduct().getHeight() * p.getProduct().getWeight()  * p.getQuantity())
                    .reduce(0, Integer::sum);
            int size = (int) Math.pow(volume, (double) 1 /3);
            this.height =  size;
            this.length =  size;
            this.width = size;
            this.weight = products
                    .stream()
                    .map(c -> c.getProduct().getWidth())
                    .reduce(0, Integer::sum);
            this.to_district_id = address.getDistrictId();
            this.to_ward_code = String.valueOf(address.getCommuneId());
            this.service_id = 53320;
        }

        public static String getJson(Address address, List<Cart> carts) {
            RequiredData requiredData = new RequiredData(address, carts);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(requiredData);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
