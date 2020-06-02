package com.huqi.fronted.dao.impl;

import com.huqi.admin.entry.User;
import com.huqi.fronted.dao.UserDao;
import com.huqi.util.DataUtils;
import com.huqi.util.DbUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserDaoImpl implements UserDao {

    /**
     * 检查用户名 或密码
     * @param username
     * @return
     */
    @Override
    public boolean CheckUsernameOrPassword(String username) {

        String sql = "select u_name from USER WHERE u_name = ? or u_phone = ?";

        return Check(sql, username);
    }

    /**
     *  验证登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public User checkUsernameAndPassword(String username, String password) {

        StringBuffer sql = new StringBuffer("select u_id id, u_name name, u_email email, u_sex sex, ")
                .append(" u_status status, u_phone phone, u_register_date registerDate ")
                .append(" from user ")
                .append(" where u_name = ? and u_password = ?");

        List<User> allPro = DataUtils.getAllPro(User.class, sql.toString(), username, password);
        System.out.println(allPro);

        if(allPro == null){
            return null;
        }else {
            return allPro.get(0);
        }
    }

    /**
     * 登录插入数据
     * @param phone
     * @param username
     * @param password
     * @param email
     * @param sex
     * @return
     */
    @Override
    public boolean register(String phone, String username, String password, String email, String sex) {

        String sql = "INSERT INTO USER(u_name,u_password,u_email,u_sex,u_status,u_phone,u_register_date) VALUE(?,?,?,?,?,?,?)";

        return DataUtils.executeOne(sql, username, password, email, sex, 1, phone, new Date());
    }

    /**
     *  返回用户的余额
     * @param userId
     * @return
     */
    @Override
    public BigDecimal getBalanceOfUser(Integer userId) {
        BigDecimal bigDecimal = null;
        String sql = "select u_balance from user where u_id = ?";
        Connection connection = DbUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();

            bigDecimal = rs.getBigDecimal("u_balance");
            DbUtil.close(connection,ps, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bigDecimal;
    }

    /**
     * 修改用户的余额
     * @param remainBalance
     * @param userId
     * @param connection
     * @throws SQLException
     */
    @Override
    public void updateBalanceOfUser(BigDecimal remainBalance, Integer userId, Connection connection) throws SQLException {
        String updateSql = "update user set u_balance = ? where u_id = ? ";

        PreparedStatement ps = connection.prepareStatement(updateSql);

        ps.setObject(1, remainBalance);
        ps.setObject(2, userId);
        ps.executeUpdate();

        ps.close();
    }

    /**
     * 通用的查询方法
     * @param sql
     * @param obj
     * @return
     */
    private boolean Check(String sql, String obj){
        Connection connection = DbUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean b = false;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,obj);
            preparedStatement.setString(2,obj);
            resultSet = preparedStatement.executeQuery();
            b = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(connection,preparedStatement, resultSet);
        }
        return b;
    }
}
