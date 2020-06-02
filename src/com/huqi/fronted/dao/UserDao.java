package com.huqi.fronted.dao;

import com.huqi.admin.entry.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {

    // 检查用户名或密码是否存在
    boolean CheckUsernameOrPassword(String username);

    // 登录验证的代码
    User checkUsernameAndPassword(String username, String password);

    // 注册
    boolean register(String phone, String username, String password, String email, String sex);

    // 返回用户的余额
    BigDecimal getBalanceOfUser(Integer userId);

    // 修改用户的余额
    void updateBalanceOfUser(BigDecimal remainBalance, Integer userId, Connection connection) throws SQLException;
}
