package com.huqi.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 传入对象和结果集,利用反射进行赋值
 */
public class DataUtils {

    //执行一条插入或者删除修改语句
    public static boolean executeOne(String sql, Object... objects){
        int i = 0;
        Connection connection = DbUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, objects);

            i = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbUtil.close(connection,preparedStatement,null);
        }

        return  i > 0;
    }


    /**
     * 用户传入指定要存入的对象和一个查询的结果集,返回一个柏寒所有对象的结果集
     * @param tClass
     * @param resultSet
     * @param <T>
     * @return
     */
    public static <T> List<T> getAll(Class<T> tClass, ResultSet resultSet){

        List<T> list = new ArrayList<>();
        try{

            T t;
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取数据库的行数
            int count = metaData.getColumnCount();

            while (resultSet.next()) {
                t = tClass.newInstance();
                for (int i = 1; i <= count; i++) {
                    //通过别名获取列名
                    String name = metaData.getColumnLabel(i);

                    //获取方法的参数类型
                    Class<?> type = tClass.getDeclaredField(name).getType();

                    //通过别名和参数类型获取方法
                    String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    Method method = tClass.getMethod(methodName, type);

                    if(resultSet.getObject(i) != null){
                        //执行方法
                        method.invoke(t, resultSet.getObject(i));
                    }
                }
                //将封装好的对象存到list中
                list.add(t);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbUtil.close(null, null, resultSet);
        }

        return list;
    }

    /**
     * 用户传入要存的实体类,要执行的sql语句,sql的参数
     * @param tClass 要进行存储的实体类
     * @param sql sql语句
     * @param objects 用户要输入的参数
     * @param <T>
     * @return
     */
    public static <T> List<T> getAllPro(Class<T> tClass, String sql,Object... objects){
        //存放所有用户的list
        List<T> list = new ArrayList<>();

        //获取数据库连接
        Connection conn = DbUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //设置sql语句
            ps = conn.prepareStatement(sql);

            //插入数据
            setParams(ps, objects);

            //执行sql语句,获取结果集
            rs = ps.executeQuery();

            //获取数据库的元数据
            ResultSetMetaData metaData = ps.getMetaData();

            //获取数据库属性的个数
            int columnCount = metaData.getColumnCount();

            T t = null;
            Field field = null;

            //遍历结果集,将取到的每行结果都存到对应的对象中
            while (rs.next()){
                //创建一个存储数据的对象
                t = tClass.newInstance();

                for (int i = 1; i <= columnCount; i++) {

                    //一个个的获取属性(获取别名)
                    field = tClass.getDeclaredField(metaData.getColumnLabel(i));
//                    System.out.println(field.getName());

                    //破解权限
                    field.setAccessible(true);
                    if(rs.getObject(i) != null){
                        //设置单个属性的值
                        field.set(t, rs.getObject(i));
                    }
                }
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭所有的
            DbUtil.close(conn, ps, rs);
        }
        return list;
    }


    private static void setParams(PreparedStatement ps, Object... objects){

        if(objects != null){

            for (int i = 0; i < objects.length; i++) {
                try {
                    ps.setObject(i+1, objects[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
