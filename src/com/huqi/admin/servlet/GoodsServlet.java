
package com.huqi.admin.servlet;

import com.huqi.admin.service.GoodsService;
import com.huqi.admin.service.impl.GoodsServiceImpl;
import com.huqi.base.BaseSevlet;
import com.huqi.commons.TableData;
import com.huqi.fronted.domain.Goods;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

//@WebServlet("/admin-goods")
@WebServlet(value = "/admin-goods", name = "GoodsServlet")
public class GoodsServlet extends BaseSevlet {

    private GoodsService goodsService = new GoodsServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("method");
        if(null == methodName) {
            methodName = "showList";
        }
        try {
            Method method = GoodsServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/**
     * 展示所有的商品
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */

    /*private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String limit = req.getParameter("limit"); // 每页展示的数据量
        String offset = req.getParameter("offset"); // 偏移量

        TableData<Goods> pageData = goodsService.getPageData(Integer.parseInt(limit), Integer.parseInt(offset));

        sendJson(resp, pageData);
    }
*/



}

