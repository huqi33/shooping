package com.huqi.fronted.dao;

import com.huqi.fronted.domain.Goods;

import java.sql.Connection;
import java.util.List;

public interface GoodDao {

    /**
     * 获取所有商品的信息
     * @return
     */
    List<Goods> getAll();


    List<Goods> getGoodsByIds(Integer[] ids);

    /**
     * 查询指定商品的库存
     */
    Integer getStockOfSpecifiedGoods(Integer ids);

    /**
     * 更新库存
     * @param newStockNum   最新库存
     * @param goodId        商品的id
     */
    void updateStockNum(Integer newStockNum, Integer goodId, Connection conn);

}
