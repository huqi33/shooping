package com.huqi.fronted.service;

import com.huqi.commons.SendData;
import com.huqi.fronted.domain.WebOrder;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * 生成订单
     * @param goodsInf  所有的商品编号的数量
     * @param defaultTakeDelivertyAddressId  收货地址编号
     * @param userId  用户id
     * @return
     */
    SendData ensureOrder(Map<Integer, Integer> goodsInf, Integer defaultTakeDelivertyAddressId, Integer userId);

    /**
     * 用户确认支付
     * @param orderNo
     * @param userId
     * @return
     */
    SendData confirmPay(String orderNo, Integer userId);

    /**
     *  查询用户某个订单状态的所有订单
     * @param status
     * @param userId
     * @return
     */
    List<WebOrder> getOrdersByStatus(Integer status, Integer userId);
}
