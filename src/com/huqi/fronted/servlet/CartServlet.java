package com.huqi.fronted.servlet;

import com.huqi.base.BaseSevlet;
import com.huqi.fronted.domain.Goods;
import com.huqi.fronted.service.GoodService;
import com.huqi.fronted.service.impl.GoodServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@WebServlet("/cart")
public class CartServlet extends BaseSevlet {

    private GoodService goodService = new GoodServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idsStr = req.getParameter("ids");
        String[] idsArr = idsStr.split(",");

        System.out.println(idsStr + "-----------");

        // 将字符串转为数组
        Integer[] ids = new Integer[idsArr.length];
        for (int i = 0; i < idsArr.length; i++) {
            ids[i] = Integer.parseInt(idsArr[i]);
        }

        List<Goods> goodsByIds = goodService.getGoodsByIds(ids);

        System.out.println(goodsByIds + "++++++++++++++++++++");
        sendJson(resp, goodsByIds);

    }
}
