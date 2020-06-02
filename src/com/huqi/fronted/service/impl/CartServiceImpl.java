package com.huqi.fronted.service.impl;

import com.huqi.fronted.dao.CartDao;
import com.huqi.fronted.dao.impl.CartDaoImpl;
import com.huqi.fronted.service.CartService;
import com.huqi.util.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CartServiceImpl implements CartService{

    private CartDao cartDao = new CartDaoImpl();

    @Override
    public Map<String, Integer> mergeCartData(Map<String, Integer> browserCartData, Integer userId) {

        /**
         *
         *  1> servlet 传输浏览器的物品信息和用户的id到这边来之后
         *  2> 取出这个用户数据库中购物车的数据
         *  3> 使用一个遍历 浏览器物品信息或者数据库购物车信息,找出两者相交的物品goodid
         *  4> 然后遍历浏览器的物品信息(数据库的也行),
         *          a. 如果这个物品在set中,则执行dao层的更新语句,将浏览器的书香更新到数据库
         *          b. 如果这个物品不在set中,将执行dao层的插入语句,更新数据库
         *  5> 遍历完毕之后,执行dao的查询,根据用户id将最新的数据库数据查询到map返回
         *
         */

        // 取出对应用户id数据库的数据
        Map<String, Integer> cartInfoOfUser = cartDao.getCartInfoOfUser(userId);


        //开启一个事务
        Connection connection = DbUtil.getConnection();
        try {
            // 设置不自动提交
            connection.setAutoCommit(false);

            Set<String> strings = browserCartData.keySet();

            //如果购物车中没有数据,则不用进行合并,直接将浏览器的数据插入到数据库中即可
            if(cartInfoOfUser != null && cartInfoOfUser.size() != 0){

                //存放重复值的set
                Set set = new HashSet();
                // 计算重复值,将key放到set中
                cartInfoOfUser.forEach((k,v) -> {
                    if(browserCartData.containsKey(k)){
                        set.add(k);
                    }
                });

                for (String s : strings){
                    // 获取数量
                    int num = browserCartData.get(s);

                    // 如果商品重复了,则进行数量的更新
                    if(set.contains(s)){
                        // 如果浏览器和数据库的相同商品的数量一样就不进行更新
                        if(num != cartInfoOfUser.get(s)){
                            cartDao.updateShoppingCartData( Integer.parseInt(s),num, userId,connection);
                        }
                    }else { // 商品没有重复,则进行商品的插入
                        cartDao.insertShoppingCartData(Integer.parseInt(s), num,userId, connection);
                    }
                }

            }else { // 如果数据库没有数据直接将所有商品插入数据库

                strings.forEach(s -> {
                    int num = browserCartData.get(s);
                    cartDao.insertShoppingCartData(Integer.parseInt(s), num,userId, connection);
                });

            }
            // 开启事务
            connection.commit();

            // 重新查询数据
            cartInfoOfUser = cartDao.getCartInfoOfUser(userId);


        } catch (SQLException e) {
            e.printStackTrace();
            try {
                //事务的回滚
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return cartInfoOfUser;
    }

    @Override
    public Map<String, Integer> getCartInfoOfUser(Integer userId) {
        return cartDao.getCartInfoOfUser(userId);
    }

    @Override
    public void addGood(Integer userId, Integer goodId) {
        cartDao.insertShoppingCartData(goodId, 1, userId, null);
    }

    /**
     *  更新数量
     * @param userId
     * @param goodId
     * @param num
     */
    @Override
    public void updateGoodNum(Integer userId, Integer goodId, Integer num) {

        cartDao.updateShoppingCartData(goodId,num,userId, null);
    }

    /**
     * 删除商品
     * @param userId
     * @param goodId
     */
    @Override
    public void deleteCartData(Integer userId, Integer goodId) {
        cartDao.deleteCartData(userId, goodId);
    }

    @Override
    public Integer goodsIsCartByUser(Integer userId, Integer gooid) {
        return cartDao.goodsIsCartByUser(userId, gooid);
    }
}
