package com.huqi.admin.service.impl;

import com.huqi.admin.dao.GoodsDao;
import com.huqi.admin.dao.impl.GoodsDaoImpl;
import com.huqi.admin.service.GoodsService;
import com.huqi.commons.TableData;
import com.huqi.fronted.domain.Goods;

public class GoodsServiceImpl implements GoodsService {

    private GoodsDao goodsDao = new GoodsDaoImpl();

    @Override
    public TableData<Goods> getPageData(Integer limit, Integer offset) {
        return goodsDao.getPageData(limit, offset);
    }

    @Override
    public void deleteGoods(Integer goodsId) {

    }
}
