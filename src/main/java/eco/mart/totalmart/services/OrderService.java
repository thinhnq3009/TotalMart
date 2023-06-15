package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.*;
import eco.mart.totalmart.exceptions.VoucherException;
import eco.mart.totalmart.module.MyPage;
import eco.mart.totalmart.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserService userService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    CartService cartService;

    @Transactional
    public Optional<Order> createOrder(Address address,
                                       List<Long> cartIds,
                                       Long shippingFee,
                                       String voucherCode,
                                       String note) throws VoucherException {
        try {
            User user = userService.getUser();


            Order order = new Order();
            order.setUser(user);
            order.setAddress(address);
            order.setShippingFee(shippingFee);
            order.setNote(note);

            if (voucherCode != null) {
                Optional<Voucher> voucherOtn = voucherService.findByCode(voucherCode);
                if (voucherOtn.isPresent()) {
                    Voucher voucher = voucherOtn.get();
                    order.setVoucher(voucher);
                    voucher.use();
                    voucherService.save(voucher);
                }

            }

            order = orderRepository.saveAndFlush(order);

            List<OrderDetail> orderDetails = orderDetailService.createOrderDetails(cartIds, order);
            order.setOrderDetails(orderDetails);

            cartService.deleteCarts(cartIds);


            return Optional.of(order);

        } catch (UsernameNotFoundException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public MyPage<Order> findOrder(Pageable pageable, Integer dayAgo) {

        Calendar calendar;
        Date start;
        Date end = new Date();

        if (dayAgo == 0) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);
        } else {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -dayAgo);
        }

        start = calendar.getTime();

        Page<Order> page = orderRepository.findAllOrderByTimeCreatedBetween(start, end, pageable);
        return MyPage.of(page);
    }
}
