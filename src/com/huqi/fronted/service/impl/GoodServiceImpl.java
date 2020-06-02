package com.huqi.fronted.service.impl;

import com.huqi.fronted.dao.GoodDao;
import com.huqi.fronted.dao.impl.GoodDaoImpl;
import com.huqi.fronted.domain.Goods;
import com.huqi.fronted.service.GoodService;

import java.util.List;

public class GoodServiceImpl implements GoodService {

    private GoodDao goodDao = new GoodDaoImpl();
    // 图片服务器的地址
    private final String imageServerLocation = "http://localhost/";

    /**
     * 返回所有的商品
     * @return
     */
    @Override
    public List<Goods> getAll() {
        List<Goods> all = goodDao.getAll();

        all.forEach(k -> k.setImageSrc(imageServerLocation + k.getImageSrc()));
        return all;
    }

    @Override
    public List<Goods> getGoodsByIds(Integer[] ids) {
        List<Goods> goodsByIds = goodDao.getGoodsByIds(ids);
        goodsByIds.forEach( k -> k.setImageSrc(imageServerLocation + k.getImageSrc()));
        return goodsByIds;
    }
}
