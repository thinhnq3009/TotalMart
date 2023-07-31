package eco.mart.totalmart.controller.rest;

import eco.mart.totalmart.entities.Address;
import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.services.AddressService;
import eco.mart.totalmart.services.CartService;
import eco.mart.totalmart.services.ShippingFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipping-fee")
public class ShippingFeeRestController {
    @Autowired
    ShippingFeeService shippingFeeService;

    @Autowired
    AddressService addressService;

    @Autowired
    CartService cartService;

    private final Logger logger = LoggerFactory.getLogger(ShippingFeeRestController.class);

    @GetMapping("")
    public Object getShippingFee(
            @RequestParam("addressId") Long addressId,
            @RequestParam("cartIds[]") List<Long> cartIds
    ) {
        Address address = addressService.findById(addressId).orElseThrow();
        List<Cart> carts = cartService.findByIdIn(cartIds);

        String value = shippingFeeService.getShippingFee(carts, address);
        JacksonJsonParser parser = new JacksonJsonParser();
        return parser.parseMap(value);
    }


}
