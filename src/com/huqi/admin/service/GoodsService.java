package com.huqi.admin.service;

import com.huqi.commons.TableData;
import com.huqi.fronted.domain.Goods;

public interface GoodsService {

    /**
     * 返回分页的数据
     * @param limit      每页的数据量
     * @param offset     每页开始的索引
     * @return
     */
    TableData<Goods> getPageData(Integer limit, Integer offset);

    /**
     * 根据id删除对应的商品
     * @param goodsId
     */
    void deleteGoods(Integer goodsId);
}
