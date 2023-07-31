package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.*;
import eco.mart.totalmart.enums.OrderStatus;
import eco.mart.totalmart.enums.PaymentMethod;
import eco.mart.totalmart.exceptions.VoucherException;
import eco.mart.totalmart.module.MyPage;
import eco.mart.totalmart.repositories.OrderRepository;
import eco.mart.totalmart.vnpay.VNPayGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.IntStream;

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

    @Autowired
    VnpService payService;

    public boolean authenticUser(Order order) {
        User user = userService.getUser();
        return order.getUser().getId().equals(user.getId());
    }

    @Transactional
    public Optional<Order> createOrder(Address address,
                                       List<Long> cartIds,
                                       Long shippingFee,
                                       String voucherCode,
                                       String note,
                                       String paymentMethod) throws VoucherException {
        try {
            User user = userService.getUser();

            PaymentMethod paymentMethodEnum;

            try {
                paymentMethodEnum = PaymentMethod.valueOf(paymentMethod);
            } catch (Exception e) {
                paymentMethodEnum = PaymentMethod.COD;
            }


            Order order = Order
                    .builder()
                    .user(user)
                    .address(address)
                    .shippingFee(shippingFee)
                    .note(note)
                    .timeCreated(new Timestamp(System.currentTimeMillis()))
                    .paymentMethod(paymentMethodEnum)
                    .build();

            // Thêm Voucher nếu có vào order
            if (voucherCode != null) {
                Optional<Voucher> voucherOtn = voucherService.findByCode(voucherCode);
                if (voucherOtn.isPresent()) {
                    Voucher voucher = voucherOtn.get();
                    order.setVoucher(voucher);
                    voucher.use(); // Giảm số lượng còn lại đi 1
                    voucherService.save(voucher);
                }

            }

            // Luư order để lấy id
            order = orderRepository.saveAndFlush(order);

            List<OrderDetail> orderDetails = orderDetailService.createOrderDetails(cartIds, order);
            order.setOrderDetails(orderDetails);

            cartService.checkoutCarts(cartIds);

            //Generate payment url
            if (paymentMethodEnum == PaymentMethod.VN_PAY) {
                VNPayGenerator paymentUrl = payService.getGenerator(order);
                order.setLinkToPay(paymentUrl.generateUrl());
                order.setExpiredPayTime(
                        new Timestamp(paymentUrl.getExpiredTime().getTime())
                );
            }

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

    public MyPage<Order> findOrder(Pageable pageable, Integer dayAgo, OrderStatus status) {

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

        MyPage<Order> myPage = MyPage.of(page);

        return myPage
                .filterAndClone(
                        order -> status == null || order.getStatus() == status
                );
    }

    public List<Order> getOrderInMonth(int month, int year) {

        if (month == -1) {
            month = Calendar.getInstance().get(Calendar.MONTH);
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, month);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 1);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, month);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);

        if (year > 0) {
            startCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.YEAR, year);
        }

        return orderRepository.findAllByTimeCreatedBetween(startCalendar.getTime(), endCalendar.getTime());

    }

    public long getTotalIncomeInMonth(int month, int year) {
        return getOrderInMonth(month, year)
                .stream()
                .mapToLong(Order::getFinalTotal)
                .sum();
    }

    public long getTotalCost(int month, int year) {
        return getOrderInMonth(month, year)
                .stream()
                .mapToLong(Order::getCost)
                .sum();
    }

    public long getTotalProfit(int month, int year) {
        return getOrderInMonth(month, year)
                .stream()
                .mapToLong(
                        o -> o.getTotalBill() - o.getCost()
                )
                .sum();
    }

    public long getTotalShippingFee(int month, int year) {
        return getOrderInMonth(month, year)
                .stream()
                .mapToLong(Order::getShippingFee)
                .sum();
    }


    public Map<String, List<Long>> getDataChart(Integer year) {

        List<Long> cost = new ArrayList<>();
        List<Long> revenue = new ArrayList<>();
        List<Long> profit = new ArrayList<>();
        List<Long> shippingFee = new ArrayList<>();

        final int finalYear = year == null ? -1 : year;
        IntStream.range(1, 12).forEach(i -> {
            cost.add(getTotalCost(i, finalYear));
            revenue.add(getTotalIncomeInMonth(i, finalYear));
            profit.add(getTotalProfit(i, finalYear));
            shippingFee.add(getTotalShippingFee(i, finalYear));
        });

        Map<String, List<Long>> data = new HashMap<>();
        data.put("cost", cost);
        data.put("revenue", revenue);
        data.put("profit", profit);
        data.put("shippingFee", shippingFee);

        return data;
    }

    public List<Integer> getYearHaveOrder() {
        return orderRepository.getYearHaveOrder()
//                .stream()
//                .map(Date::getYear)
//                .collect(Collectors.toList())
                ;

    }

    public void updateOrderAfterPaid(String orderId) {
        Long id = Long.parseLong(orderId);

        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setTimeConfirmed(new Timestamp(System.currentTimeMillis()));
            order.setPaid(true);

            orderRepository.save(order);
        }


    }
}
