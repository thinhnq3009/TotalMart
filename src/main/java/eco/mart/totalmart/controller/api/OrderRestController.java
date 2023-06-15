package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.entities.Address;
import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.exceptions.VoucherException;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.AddressService;
import eco.mart.totalmart.services.CartService;
import eco.mart.totalmart.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order")
public class OrderRestController {

    private final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    AddressService addressService;

    @Autowired
    private CartService cartService;

    @PostMapping("/checkout")
    ResponseEntity<ResponseObject> checkout(
            @RequestParam("addressId") Long addressId,
            @RequestParam("cartIds[]") List<Long> cartIds,
            @RequestParam("shippingFee") Long shippingFee,
            @RequestParam(value = "voucherCode", required = false) String voucherCode,
            @RequestParam(value = "note", required = false) String note
    ) {

        ResponseEntity<ResponseObject> response = ResponseObject
                .builder()
                .message("Checkout failed")
                .build().toResponseEntity();

        Optional<Address> addressOtn = addressService.findById(addressId);

        try {
            if (addressOtn.isPresent()) {
                Address address = addressOtn.get();
                Optional<Order> orderOtn = orderService.createOrder(address, cartIds, shippingFee, voucherCode, note);
                return orderOtn
                        .map(order -> ResponseObject
                                .builder()
                                .message("Checkout successfully")
                                .data(order)
                                .status("success")
                                .build().toResponseEntity()
                        ).orElse(response);
            }
        } catch (VoucherException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            response.getBody().message("Voucher is out of stock. Please remove voucher or choose another one.");
        }

        return response;

    }

    @PutMapping("/confirm/{id}")
    ResponseEntity<ResponseObject> confirm(@PathVariable("id") Long id) {

        Optional<Order> orderOtn = orderService.findById(id);
        ResponseObject response = ResponseObject
                .builder()
                .message("Confirm failed")
                .data(orderOtn)
                .build();
        if (orderOtn.isPresent()) {
            Order order = orderOtn.get();
            order.setTimeConfirmed(new Timestamp(System.currentTimeMillis()));
            orderService.save(order);
            response.message("Confirm successfully");
            response.setData(order);
        }
        return response.toResponseEntity();
    }

    @PutMapping("/cancel/{id}")
    ResponseEntity<ResponseObject> cancel(@PathVariable("id") Long id) {

        Optional<Order> orderOtn = orderService.findById(id);
        ResponseObject response = ResponseObject
                .builder()
                .message("Cancel failed")
                .data(orderOtn)
                .build();
        if (orderOtn.isPresent()) {
            Order order = orderOtn.get();
            order.setTimeCanceled(new Timestamp(System.currentTimeMillis()));
            orderService.save(order);
            response.message("Cancel successfully");
            response.setData(order);
        }
        return response.toResponseEntity();
    }


}
