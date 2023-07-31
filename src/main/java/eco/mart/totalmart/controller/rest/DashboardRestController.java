package eco.mart.totalmart.controller.rest;

import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardRestController {

    @Autowired
    OrderService orderService;

    @GetMapping("/chart-data")
    ResponseEntity<ResponseObject> getData(
            Model model,
            @RequestParam(value = "year", required = false) Integer year
    ) {


        Map<String, List<Long>> data = orderService.getDataChart(year);



        return ResponseObject
                .builder()
                .data(data)
                .message("success")
                .build()
                .toResponseEntity();
    }

}
