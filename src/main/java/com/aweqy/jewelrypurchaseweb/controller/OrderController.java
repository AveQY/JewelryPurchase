package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.Util.OrderIdGenerator;
import com.aweqy.jewelrypurchaseweb.jpw.Orders;
import com.aweqy.jewelrypurchaseweb.jpw.PaymentMethodEnum;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import com.aweqy.jewelrypurchaseweb.jpw.StatusEnum;
import com.aweqy.jewelrypurchaseweb.service.CollaborativeFilteringService;
import com.aweqy.jewelrypurchaseweb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CollaborativeFilteringService recommendationService;

    @GetMapping("/order")
    public Result<Orders> getOrder(@RequestParam Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/orderList")
    public Result<List<Orders>> getOrderList(@RequestParam int userId) {
        return orderService.getOrderList(userId);
    }

    @PostMapping("/add/order")
    public Result<?> addOrder(@RequestParam int productId, @RequestParam int purchaseQuantity, @RequestParam int userId, @RequestParam int addressId,
                              @RequestParam BigDecimal totalAmount, @RequestParam int paymentMethodCode) {

        OrderIdGenerator generator = new OrderIdGenerator(16);
        long orderId = Long.parseLong(generator.nextId());
        PaymentMethodEnum paymentMethod = PaymentMethodEnum.fromCode(paymentMethodCode);
        StatusEnum status = StatusEnum.fromCode(1);
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime updateTime = LocalDateTime.now();
        Orders oreder = new Orders(orderId, productId, purchaseQuantity, userId, addressId, totalAmount,
                status.toString(), paymentMethod.toString(), createTime, updateTime);

        recommendationService.computeUserSimilarities();

        return orderService.addOrder(oreder);
    }

    @PostMapping("/update/order")
    public Result<?> updateOrder(@RequestParam Long orderId, @RequestParam int statusCode,
                                 @RequestParam(required = false) BigDecimal totalAmount,
                                 @RequestParam(required = false) String paymentMethod,
                                 @RequestParam(required = false) Integer addressId) {
        return orderService.updateOrder(orderId, statusCode, totalAmount,paymentMethod, addressId);
    }

    @DeleteMapping("/delete/order/{orderId}")
    public Result<?> deleteOrder(@PathVariable Long orderId) {
        return orderService.deleteOrder(orderId);
    }

}
