package com.huqi.fronted.dao.impl;

import com.alibaba.druid.sql.dialect.db2.visitor.DB2ASTVisitor;
import com.huqi.admin.entry.Address;
import com.huqi.fronted.dao.AddressDao;
import com.huqi.util.DataUtils;
import com.huqi.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AddressDaoImpl implements AddressDao {


    /**
     * 根据用户id查询地址信息
     * @param userId
     * @return
     */
    @Override
    public List<Address> getAddressByUserId(Integer userId) {
        String sql = "SELECT a_id id, a_name name, a_phone phone, a_detail detail, a_state state from address where u_id = ?";

        return DataUtils.getAllPro(Address.class, sql, userId);
    }

    @Override
    public void changeTakeDeliveryAddress(Integer userId, Integer addressId) {

        String sql = "UPDATE address set a_state = 0 where u_id = ? and a_id = 1";
        Connection connection = DbUtil.getConnection();
        try {
            //开启事务
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,userId);

            preparedStatement.executeUpdate();

            sql = "UPDATE address set a_state = 1 where u_id = ? and a_id = ?";

            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setObject(1,userId);
            preparedStatement1.setObject(2,addressId);
            preparedStatement1.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }
}
