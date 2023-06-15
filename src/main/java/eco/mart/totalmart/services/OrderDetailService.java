package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.entities.OrderDetail;
import eco.mart.totalmart.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                    orderDetail.setProduct(cart.getProduct());
                    orderDetail.setOrder(order);
                    orderDetail.setQuantity(cart.getQuantity());
                    orderDetail.setSellPrice(cart.getProduct().getSalePrice());
                    orderDetailRepository.saveAndFlush(orderDetail);
                    return orderDetail;
                }).toList();
    }


}
