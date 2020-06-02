package com.huqi.admin.service.impl;

import com.huqi.commons.TableData;
import com.huqi.admin.dao.UserDao;
import com.huqi.admin.dao.impl.UserDaoImpl;
import com.huqi.admin.entry.User;
import com.huqi.admin.service.UserService;

import java.util.Date;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public TableData<User> getPageData(String name, String gender, Integer status, String email, Date beginRegisterDate, Date endRegisterDate, Integer offset, Integer pageSize) {
        return userDao.getPageData( name,  gender,status, email,  beginRegisterDate,  endRegisterDate,  offset,  pageSize);
    }

    @Override
    public boolean activeUser(int id) {
        return userDao.activeUser(id);
    }
}
