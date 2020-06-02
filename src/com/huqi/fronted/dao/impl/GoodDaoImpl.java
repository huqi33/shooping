package com.huqi.fronted.dao.impl;

import com.huqi.fronted.dao.GoodDao;
import com.huqi.fronted.domain.Goods;
import com.huqi.util.DataUtils;
import com.huqi.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GoodDaoImpl implements GoodDao {

    /**
     * 查出所有的商品
     * @return
     */
    @Override
    public List<Goods> getAll() {

        StringBuffer sql = new StringBuffer("SELECT g.id, i.name type, g.price, g.small_pic imageSrc, g.status, g.stock_num stock, g.create_time createTime, ")
                .append(" g.update_time updateTime, g.title, g.goods_id goodId ")
                .append(" from goods g JOIN item i on g.item_id = i.id ");

        return DataUtils.getAllPro(Goods.class,sql.toString(),new Object[0]);
    }

    /**
     *  根据商品的id查出商品的具体所有信息
     * @param ids
     * @return
     */
    @Override
    public List<Goods> getGoodsByIds(Integer[] ids) {

        StringBuffer sql = new StringBuffer("select goods_id goodId, title, small_pic imageSrc, price, stock_num stock from goods where goods_id in ( ");

        int length = ids.length;

        // 拼接sql  ->  ?, ?, ?)
        for(int i = 0; i < length; i++){
            if(i == (length - 1)){
                sql.append("?)");
            }else {
                sql.append("?,");
            }
        }
        return DataUtils.getAllPro(Goods.class, sql.toString(), ids);
    }

    /**
     * 根据id查询商品库存
     * @param ids
     * @return
     */
    @Override
    public Integer getStockOfSpecifiedGoods(Integer ids) {
        String sql = "select stock_num from goods where goods_id = ?";
        Connection connection = DbUtil.getConnection();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Integer num = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,ids);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                num = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbUtil.close(connection, preparedStatement, resultSet);
        }
        return num;
    }

    /**
     * 修改商品库存
     * @param newStockNum   最新库存
     * @param goodId        商品的id
     * @param conn
     */
    @Override
    public void updateStockNum(Integer newStockNum, Integer goodId, Connection conn) {

        String sql = "UPDATE goods set stock_num = ? WHERE goods_id = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1,newStockNum);
            preparedStatement.setObject(2,goodId);
            preparedStatement.executeUpdate();

            DbUtil.close(null,preparedStatement,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
