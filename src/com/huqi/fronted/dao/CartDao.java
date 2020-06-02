package com.huqi.fronted.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface CartDao {

    // 根据用户id查询购物车数据
    Map<String , Integer> getCartInfoOfUser(Integer userId);


    //  传入用户id, 商品id, 商品数量, conn, 插入数据
    void insertShoppingCartData(Integer goodId, Integer num, Integer userId, Connection connection);

    //  传入用户id, 商品id, 商品数量, conn 更新数据库
    void updateShoppingCartData(Integer goodId, Integer num, Integer userId, Connection connection);

    //删除商品
    void deleteCartData(Integer userId, Integer goodId);

    // 根据用户id查询某件商品是否在数据库中
    Integer goodsIsCartByUser(Integer userId, Integer gooid);

    //删除对应用户的购物车数据
    void deleteShoppingCartDataOfUser(Integer userId, Connection connection) throws SQLException;
}
