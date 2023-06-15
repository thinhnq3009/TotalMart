package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.entities.Address;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
public class AddressRestController {

    private final Logger logger = LoggerFactory.getLogger(AddressRestController.class);

    @Autowired
    AddressService addressService;



    @GetMapping("/all")
    public String getAllAddress() {
        return "all address";
    }

    @PostMapping("/new")
    public ResponseEntity<ResponseObject> createNewAddress(
            Address address
    ) {
        ResponseObject responseObject = ResponseObject
                .builder()
                .status("error")
                .message("Address not saved")
                .action("saveAddress")
                .build();
        return addressService
                .save(address)
                .map(add -> responseObject.toBuilder()
                        .data(add)
                        .status("success")
                        .message("Address saved")
                        .build()
                        .toResponseEntity())
                .orElse(
                        responseObject.toResponseEntity()
                );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteAddress(
            Address address
    ) {
        ResponseObject responseObject = ResponseObject
                .builder()
                .status("error")
                .message("Address not deleted")
                .action("deleteAddress")
                .build();



        return addressService
                .delete(address)
                .map(add -> responseObject.toBuilder()
                        .data(add)
                        .status("success")
                        .message("Address deleted")
                        .build()
                        .toResponseEntity())
                .orElse(
                        responseObject.toResponseEntity()
                );
    }

}
