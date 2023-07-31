package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.entities.OrderDetail;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderDetailService {

    @Autowired
    CartService cartService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;


    public List<OrderDetail> createOrderDetails(List<Long> cartIds, Order order) {
        return cartService.findCartsByIds(cartIds)
                .stream()
                .map((cart) -> {
                    OrderDetail orderDetail = new OrderDetail();
                    Product product = cart.getProduct();
                    int buyQuantity = cart.getQuantity();
                    if (product.canSale(buyQuantity)) { // Luôn đúng vì đã kiểm tra ở client
                        orderDetail.setProduct(product);
                        orderDetail.setOrder(order);
                        orderDetail.setQuantity(buyQuantity);
                        orderDetail.setSellPrice(cart.getProduct().getSalePrice());
                        orderDetailRepository.saveAndFlush(orderDetail);
                        return orderDetail;
                    } else {
                        return null;
                    }


                })
                .filter(Objects::nonNull)
                .toList();
    }


}
