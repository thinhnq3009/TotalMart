package eco.mart.totalmart.controller.rest;

import eco.mart.totalmart.entities.Voucher;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/voucher")
public class VoucherRestController {

    private final Logger logger = LoggerFactory.getLogger(VoucherRestController.class);

    @Autowired
    VoucherService voucherService;

    @GetMapping("/all")
    public String getAllVoucher() {
        return "all voucher";
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getActiveVoucher(
            @PathVariable("id") Long id
    ) {

        ResponseObject responseObject = ResponseObject
                .builder()
                .action("getActiveVoucherById")
                .status("error")
                .build();

        Optional<Voucher> voucherOtn = voucherService.findById(id);

        return voucherOtn
                .map(voucher -> responseObject
                        .toBuilder()
                        .message("Voucher found")
                        .data(voucher)
                        .status("success")
                        .build()
                        .toResponseEntity()
                )
                .orElseGet(() -> responseObject
                        .toBuilder()
                        .message("Voucher not found")
                        .build()
                        .toResponseEntity()
                );
    }

    @GetMapping("/find")
    ResponseEntity<ResponseObject> findByCode(
            @RequestParam("code") String code
    ) {

        ResponseObject responseObject = ResponseObject
                .builder()
                .action("findByCode")
                .status("error")
                .build();

        return voucherService.findByCode(code)
                .map(
                        voucher -> responseObject
                                .toBuilder()
                                .message("Voucher found")
                                .data(voucher)
                                .status("success")
                                .build()
                                .toResponseEntity()

                ).orElse(
                        responseObject
                                .toBuilder()
                                .message("Voucher not found")
                                .build()
                                .toResponseEntity()
                );


    }


}
