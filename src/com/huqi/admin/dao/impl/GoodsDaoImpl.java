package com.huqi.admin.dao.impl;

import com.huqi.admin.dao.GoodsDao;
import com.huqi.commons.TableData;
import com.huqi.fronted.domain.Goods;
import com.huqi.util.DataUtils;
import com.huqi.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GoodsDaoImpl implements GoodsDao {

    /**
     * 查询出需要的分页数据
     * @param limit      每页的数据量
     * @param offset     每页开始的索引
     * @return
     */
    @Override
    public TableData<Goods> getPageData(Integer limit, Integer offset) {
        TableData<Goods> tableData = new TableData<>();
        List<Goods> goodsList = null;

        StringBuffer dataSql = new StringBuffer("SELECT g.id, i.name type, g.price, g.small_pic imageSrc, g.status, g.stock_num stock, g.create_time createTime, ")
                .append(" g.update_time updateTime, g.title, g.goods_id goodId ")
                .append(" from goods g JOIN item i on g.item_id = i.id ")
                .append(" limit ?, ?");

        // 查询总数的sql
        String countSql = "select count(1) from goods g JOIN item i on g.item_id = i.id";

        goodsList = DataUtils.getAllPro(Goods.class, dataSql.toString(),offset, limit);

        Connection connection = DbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(countSql);

            ResultSet resultSet = preparedStatement.executeQuery();
            int anInt = 0;
            if(resultSet.next()){
                 anInt = resultSet.getInt(1);
            }

            tableData.setRows(goodsList);
            tableData.setTotal(anInt);

            DbUtil.close(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableData;
    }

    /**
     * 修改对应的商品状态
     * @param goodsId
     */
    @Override
    public void deleteGoods(Integer goodsId) {

    }
}
