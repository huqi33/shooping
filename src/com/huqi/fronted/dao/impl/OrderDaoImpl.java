package com.huqi.fronted.dao.impl;

import com.huqi.fronted.dao.OrderDao;
import com.huqi.fronted.domain.Order;
import com.huqi.util.DataUtils;
import com.huqi.util.DbUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OrderDaoImpl implements OrderDao {


    /**
     *  生成订单
     * @param orderNo
     * @param price
     * @param num
     * @param goodsImg
     * @param goodsTitle
     * @param orderStatus
     * @param appraiseStatus
     * @param userId
     * @param goodId
     * @param conn
     * @throws SQLException
     */
    @Override
    public void insertOrder(String orderNo, BigDecimal price, Integer num, String goodsImg, String goodsTitle, Integer orderStatus, Integer appraiseStatus, Integer userId, Integer goodId, Connection conn) throws SQLException {

        StringBuffer insertSql  = new StringBuffer("insert into orders(o_no, o_good_price, o_num, o_status, o_appraise_status, ")
                .append("o_user_id, o_create_time, o_good_id, o_good_img, o_good_title) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");


        PreparedStatement ps = conn.prepareStatement(insertSql.toString());
        ps.setObject(1, orderNo);
        ps.setObject(2, price);
        ps.setObject(3, num);
        ps.setObject(4, orderStatus);
        ps.setObject(5, appraiseStatus);
        ps.setObject(6, userId);
        ps.setObject(7, new Date());
        ps.setObject(8, goodId);
        ps.setObject(9, goodsImg);
        ps.setObject(10, goodsTitle);

        ps.executeUpdate();

        ps.close();

    }

    /**
     * 生成订单对应地址
     * @param orderNo
     * @param defaultTakeDelivertyAddressId
     * @param conn
     * @throws SQLException
     */
    @Override
    public void insertOrderAddress(String orderNo, Integer defaultTakeDelivertyAddressId, Connection conn) throws SQLException {

        String insertSql  = "insert into order_address(o_no, o_address) values(?, ?)";

        PreparedStatement ps = conn.prepareStatement(insertSql);
        ps.setObject(1, orderNo);
        ps.setObject(2, defaultTakeDelivertyAddressId);
        ps.executeUpdate();

        ps.close();
    }

    /**
     * 根据订单号返回订单
     * @param orderNo
     * @return
     */
    @Override
    public List<Order> getOrdersOfOrderNo(String orderNo) {
        List<Order> list = null;
        String sql = "select o_good_price goodPrice, o_num goodNum from orders where o_no = ?";
        Connection connection = DbUtil.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setObject(1, orderNo);
            ResultSet rs = ps.executeQuery();

            list = DataUtils.getAll(Order.class, rs);
            DbUtil.close(connection,ps, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 返回用户某个状态的所有订单
     * @param status
     * @param userId
     * @return
     */
    @Override
    public List<Order> getOrdersByStatus(Integer status, Integer userId) {
        List<Order> list = null;
        StringBuffer sql = new StringBuffer("select o_id id, o_no orderNo, o_good_price goodPrice, o_num goodNum, o_good_img goodsImg, ")
                .append(" o_status status, o_appraise_status praiseStatus, o_create_time createTime, o_pay_time payTime, o_good_title goodsTitle ")
                .append(" from orders where o_status = ? and o_user_id = ?");

        Connection connection = DbUtil.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            ps.setObject(1, status);
            ps.setObject(2, userId);

            ResultSet rs = ps.executeQuery();

            list = DataUtils.getAll(Order.class, rs);
            DbUtil.close(connection,ps, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 修改某个订单的状态
     * @param orderNo        订单编号
     * @param newStatus      最新的状态
     * @param connection
     * @throws SQLException
     */
    @Override
    public void updateOrderStatus(String orderNo, Integer newStatus, Connection connection) throws SQLException {
        String updateStatusSql = "update orders set o_status = ?, o_pay_time = ? where o_no = ?";

        PreparedStatement ps = connection.prepareStatement(updateStatusSql);
        ps.setObject(1, newStatus);
        ps.setObject(2, new Date());
        ps.setObject(3, orderNo);

        ps.executeUpdate();
    }
}
