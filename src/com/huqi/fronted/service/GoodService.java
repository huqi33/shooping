package com.huqi.fronted.service;

import com.huqi.fronted.domain.Goods;

import java.util.List;

public interface GoodService {

    List<Goods> getAll();

    List<Goods> getGoodsByIds(Integer[] ids);
}
