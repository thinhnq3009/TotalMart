package eco.mart.totalmart.services;

import eco.mart.totalmart.defaultes.DefaultValue;
import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.vnpay.VNPayGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static eco.mart.totalmart.defaultes.DefaultValue.HASH_SECRET;
import static eco.mart.totalmart.defaultes.DefaultValue.TMN_CODE;

@Service
@AllArgsConstructor
public class VnpService {

    private final HttpServletRequest req;

    private VNPayGenerator getGenerator() {
        VNPayGenerator generator = new VNPayGenerator(HASH_SECRET, TMN_CODE);
        generator.setIpAddress(req);
        generator.setOrderType("TotalMart");
        generator.setReturnUrl(DefaultValue.VPN_RETURN_URL);

        return generator;
    }

    public VNPayGenerator getGenerator(Order order) {
        VNPayGenerator generator = getGenerator();

        generator.setAmount(order.getFinalTotal() * 100);
        generator.setOrderInfo("Thanh toan don hang " + order.getId());
        generator.setTxnRef(order.getId().toString());

        return generator;
    }

    public String generateLinkToPay(Order order) {
        return getGenerator(order).generateUrl();
    }

}
