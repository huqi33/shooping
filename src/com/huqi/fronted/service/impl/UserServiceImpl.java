package com.huqi.fronted.service.impl;

import com.huqi.admin.entry.User;
import com.huqi.fronted.dao.UserDao;
import com.huqi.fronted.dao.impl.UserDaoImpl;
import com.huqi.fronted.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();


    @Override
    public boolean checkUsernameOrPassword(String username) {
        return userDao.CheckUsernameOrPassword(username);
    }

    @Override
    public boolean register(String phone, String username, String password, String email, String sex) {
        return  userDao.register( phone,  username,  password,  email,  sex);
    }

    @Override
    public User checkUsernameAndPassword(String username, String password) {
        return userDao.checkUsernameAndPassword(username, password);
    }
}
