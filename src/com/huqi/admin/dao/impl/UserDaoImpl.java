package com.huqi.admin.dao.impl;

import com.huqi.commons.TableData;
import com.huqi.admin.dao.UserDao;
import com.huqi.admin.entry.User;
import com.huqi.util.DataUtils;
import com.huqi.util.DbUtil;
import com.huqi.util.FormatUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserDaoImpl implements UserDao {

    /**
     * 查询数据
     * @param name
     * @param gender
     * @param email
     * @param beginRegisterDate
     * @param endRegisterDate
     * @param offset
     * @param pageSize
     * @return
     */
    @Override
    public TableData<User> getPageData(String name, String gender, Integer status, String email, Date beginRegisterDate, Date endRegisterDate, Integer offset, Integer pageSize) {

        System.out.println("XXXXXXXXXXX" + offset);

        List<User> user = getUser(name, gender, status, email, beginRegisterDate, endRegisterDate, offset, pageSize);
        int total = getTotal(name, gender, status , email, beginRegisterDate, endRegisterDate, offset, pageSize);

        return new TableData<>(user, total);
    }

    /**
     * 激活用户
     * @param id
     * @return
     */
    @Override
    public boolean activeUser(int id) {
        String sql = "UPDATE USER SET u_status = 1 WHERE u_id = ?";
        System.out.println("id " + id);
        return DataUtils.executeOne(sql, id);
    }

    /**
     * 获得用户分页数据

     */
    private List<User> getUser(String name, String gender, Integer status, String email, Date beginRegisterDate, Date endRegisterDate, Integer currentPage, Integer pageSize){

        StringBuilder sql = new StringBuilder("select u_id id, u_name name, u_email email, u_sex sex, u_status status, u_phone phone, u_register_date registerDate " +
                "from user where 1 = 1 ");

        StringBuilder stringBuilder = commonQuery(sql, true, name, gender,status, email, beginRegisterDate, endRegisterDate, currentPage, pageSize);

        return DataUtils.getAllPro(User.class, stringBuilder.toString());
    }

    /**
     * 获得总页数
     * @param name
     * @param gender
     * @param email
     * @param beginRegisterDate
     * @param endRegisterDate
     * @param currentPage
     * @param pageSize
     * @return
     */
    private int getTotal(String name, String gender, Integer status, String email, Date beginRegisterDate, Date endRegisterDate, Integer currentPage, Integer pageSize){
        StringBuilder sql2 = new StringBuilder("select count(u_id) from user where 1 = 1 ");
        StringBuilder totalNum = commonQuery(sql2, false, name, gender, status, email, beginRegisterDate, endRegisterDate, currentPage, pageSize);

        Connection conn = DbUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int total = 0;
        try {
            preparedStatement = conn.prepareStatement(totalNum.toString());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            total = resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbUtil.close(conn,preparedStatement, resultSet);
        }
        return total;
    }


    /**
     * 传入参数,直接生成对应的sql语句

     */
    private StringBuilder commonQuery(StringBuilder sql,Boolean isPage, String name, String gender, Integer status, String email, Date beginRegisterDate, Date endRegisterDate, Integer offset, Integer pageSize){
        if (name != null && !"".equals(name)){
            sql.append(" and u_name like " + "'%" + name + "%' ");
        }

        if (gender != null && !gender.equals("-1")){
            sql.append(" and u_sex = '" + gender + "' ");
        }

        if(email != null && !email.equals("")){
            sql.append(" and u_email like '%" + email + "%' ");
        }

        if (beginRegisterDate != null && !beginRegisterDate.equals("")){
            sql.append(" and u_register_date  >=  '" + FormatUtil.getDateFormatString(beginRegisterDate,"yyyy-MM-dd HH:mm:ss") + "' ");
        }

        if(endRegisterDate != null && !endRegisterDate.equals("")){
            sql.append(" and u_register_date <=  '" + FormatUtil.getDateFormatString(endRegisterDate,"yyyy-MM-dd HH:mm:ss") + "' ");
        }

        if(status != -1){
            sql.append(" and u_status =  " + status + " ");
        }

        if(isPage){
            sql.append(" limit " + offset + "," + pageSize);
        }

        System.out.println(sql);
        return sql;
    }

}
