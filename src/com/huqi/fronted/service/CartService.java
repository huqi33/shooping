package com.huqi.fronted.service;

import java.util.Map;

public interface CartService {

    // 合并购物车数据
    Map<String,Integer> mergeCartData(Map<String ,Integer> browserCartData, Integer userId);

    // 根据用户id查询购物车数据
    Map<String , Integer> getCartInfoOfUser(Integer userId);

    //根据用户id 商品id 来更新购物车数据库
    void addGood(Integer userId, Integer goodId);

    // 修改数量
    void updateGoodNum(Integer userId, Integer goodId, Integer num);

    //删除商品
    void deleteCartData(Integer userId, Integer goodId);

    // 根据用户id查询某件商品是否在数据库中
    Integer goodsIsCartByUser(Integer userId, Integer gooid);
}
