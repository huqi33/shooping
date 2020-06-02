package com.huqi.admin.dao;

import com.huqi.commons.TableData;
import com.huqi.admin.entry.User;

import java.util.Date;

public interface UserDao {

    //查询数据
    TableData<User> getPageData(String name, String gender,Integer status, String email, Date beginRegisterDate, Date endRegisterDate, Integer offset, Integer pageSize);

    //激活用户
    boolean  activeUser(int id);
}
