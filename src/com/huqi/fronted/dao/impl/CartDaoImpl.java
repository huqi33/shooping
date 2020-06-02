package com.huqi.fronted.dao.impl;

import com.huqi.fronted.dao.CartDao;
import com.huqi.util.DataUtils;
import com.huqi.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartDaoImpl implements CartDao {

    /**
     * 根据用户id获取其购物车数据
     * @param userId
     * @return
     */
    @Override
    public Map<String, Integer> getCartInfoOfUser(Integer userId) {

        String sql = "SELECT good_id , num FROM cart WHERE u_id = ?";

        Map<String, Integer> map = new HashMap<>();

        Connection connection = DbUtil.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                map.put(resultSet.getString("good_id"),resultSet.getInt("num"));
            }

            DbUtil.close(connection,preparedStatement,resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 插入数据到用户的购物车
     * @param goodId
     * @param num
     * @param userId
     * @param connection
     */
    @Override
    public void insertShoppingCartData(Integer goodId, Integer num, Integer userId, Connection connection) {

        String insertSql = "insert into cart(good_id, u_id, num, create_time) values(?, ?, ?, ?)";

        Connection conn = connection;
        if(conn == null){
            conn = DbUtil.getConnection();
        }

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSql);
            preparedStatement.setObject(1,goodId);
            preparedStatement.setObject(2,userId);
            preparedStatement.setObject(3,num);
            preparedStatement.setObject(4,new Date());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改用户的购物车商品数量
     * @param goodId
     * @param num
     * @param userId
     * @param connection
     */
    @Override
    public void updateShoppingCartData(Integer goodId, Integer num, Integer userId, Connection connection) {

        String sql = "update cart set num = ?, update_time = ? where good_id = ? and u_id = ?";

        Connection conn = connection;
        if(conn == null){
            conn = DbUtil.getConnection();
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, num);
            ps.setObject(2, new Date());
            ps.setObject(3, goodId);
            ps.setObject(4, userId);

            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除对应的商品
     * @param userId
     * @param goodId
     */
    @Override
    public void deleteCartData(Integer userId, Integer goodId) {

        String sql = "delete from cart where good_id = ? and u_id = ?";
        DataUtils.executeOne(sql, goodId, userId);
    }

    /**
     * 查询用户购物车中某件商品需要的件数
     * @param userId
     * @param goodId
     * @return
     */
    @Override
    public Integer goodsIsCartByUser(Integer userId, Integer goodId) {

        String sql = "select num from cart where u_id = ? and good_id = ? limit 1";

        Connection connection = DbUtil.getConnection();
        Integer c_num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setObject(1,userId);
            preparedStatement.setObject(2,goodId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                c_num = resultSet.getInt("num");
            }
            DbUtil.close(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return c_num;
    }

    /**
     * 删除用户的购物车数据
     * @param userId
     * @param connection
     * @throws SQLException
     */
    @Override
    public void deleteShoppingCartDataOfUser(Integer userId, Connection connection) throws SQLException {

        String deleteSql = "delete from cart where u_id = ?";

        PreparedStatement ps = connection.prepareStatement(deleteSql);
        ps.setObject(1, userId);
        ps.executeUpdate();

        ps.close();
    }

}
