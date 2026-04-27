package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.OrderRepository;
import com.aweqy.jewelrypurchaseweb.jpw.Orders;
import com.aweqy.jewelrypurchaseweb.jpw.PaymentMethodEnum;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import com.aweqy.jewelrypurchaseweb.jpw.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Result<?> addOrder(Orders order) {
        Orders newOrder = new Orders();
        try {
            newOrder = orderRepository.save(order);
            orderRepository.flush();
        } catch (Exception e) {
            return new Result("404 ","失败"+e.getMessage(),null);
        }
        return new Result("200","保存完成",newOrder);
    }

    public Result<?> updateOrder(Long orderId, int statusCode, BigDecimal totalAmount,String paymentMethodCode, Integer addressId) {
        // 1. 查找订单（假设根据某种条件）
        Optional<Orders> order = orderRepository.findById(orderId);
        if(!order.isPresent()) {
            return new Result("400", "未找到订单", order);
        }
        // 2. 动态更新非空字段

        if (totalAmount != null) {
            order.get().setTotalAmount(totalAmount);
        }
        if (paymentMethodCode != null) {
            PaymentMethodEnum paymentMethod = PaymentMethodEnum.fromCode(Integer.parseInt(paymentMethodCode));
            order.get().setPaymentMethod(String.valueOf(paymentMethod));
        }
        if (addressId != null) {
            order.get().setAddressId(addressId);
        }
        StatusEnum status = StatusEnum.fromCode(statusCode);
        order.get().setStatus(status.toString());
        // 3. 保存更新
        try {
            orderRepository.save(order.get());
        } catch (Exception e) {
            return new Result<>("404", "失败"+e.getMessage(), null);
        }
        return new Result<>("200", "更新成功", order.get());
    }

    public Result<?> deleteOrder(Long orderId) {
        Optional<Orders> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isPresent()) {
            orderRepository.deleteById(orderId);
            return new Result<>("200","成功",null);
        }
        return new Result<>("404","删除失败，未找到数据或接受到错误数据",null);
    }

    public Result<Orders> getOrder(Long orderId) {
        Optional<Orders> orders = orderRepository.findById(orderId);
        if(orders.isPresent()) {
           return new Result<>("200","成功",orders.get());
        }
        return new Result<>("404","失败，未找到相应订单",null);
    }

    public Result<List<Orders>> getOrderList(int userId) {
        List<Orders> orders = orderRepository.findAllByUserId(userId);
        if(orders.isEmpty()) {
            return new Result<>("400","未找到任何相关订单",null);
        }
        return new Result<>("200","成功获取到订单",orders);
    }
}
