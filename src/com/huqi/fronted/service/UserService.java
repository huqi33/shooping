package com.huqi.fronted.service;

import com.huqi.admin.entry.User;

public interface UserService {


    //检查用户名是否被注册过
    boolean checkUsernameOrPassword(String username);

    //注册的代码
    boolean register(String phone, String username, String password, String email, String sex);

    // 登录验证的代码
    User checkUsernameAndPassword(String username, String password);
}
