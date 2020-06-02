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
import java.util.List;

@WebServlet("/good")
public class GoodServlet extends BaseSevlet {

    private GoodService goodService = new GoodServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Goods> all = goodService.getAll();

        sendJson(resp, all);
    }
}
