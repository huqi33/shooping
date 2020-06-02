package com.huqi.fronted.servlet;

import com.alibaba.fastjson.JSONObject;
import com.huqi.admin.entry.User;
import com.huqi.base.BaseSevlet;
import com.huqi.commons.SendData;
import com.huqi.fronted.domain.WebOrder;
import com.huqi.fronted.service.OrderService;
import com.huqi.fronted.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/order")
public class OrderServlet extends BaseSevlet {


    private OrderService orderService = new OrderServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String methodName = req.getParameter("method");

        try {
            Method method = OrderServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成订单
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void ensureOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 获取用户商品和地址信息
        String goodsInfo = req.getParameter("goodsInfo");
        String takeDeliveryAddressId = req.getParameter("takeDeliveryAddressId");


        // 将String转为map数据
        Map<String, Integer> cartData = JSONObject.parseObject(goodsInfo, Map.class);

        System.out.println("cartData : " + cartData);

        Map<Integer, Integer> goodInfos = new HashMap<>();
        cartData.forEach((k, v) -> goodInfos.put(Integer.parseInt(k), v));

        SendData sendData = orderService.ensureOrder(goodInfos, Integer.parseInt(takeDeliveryAddressId), 1);


        System.out.println("sendData : " + sendData);
        sendJson(resp, sendData);

    }

    /**
     * 用户付款
     * 修改用户的订单中的状态和从用户的账户扣款
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void confirmPay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderNo = req.getParameter("orderNo"); //获取订单编号

        SendData data = orderService.confirmPay(orderNo, 1);

        sendJson(resp, data);
    }

    /**
     * 查询用户对应状态的所有订单
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void getOrderOfStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");

        HttpSession session = req.getSession();

        User user = (User)session.getAttribute("user");

        Integer userId = null;
        if(user != null){
            userId = user.getId();  //获取用户id
        }else {
            userId = 1;
        }

        List<WebOrder> orderList = orderService.getOrdersByStatus(Integer.parseInt(status), userId);
//        List<WebOrder> orderList = orderService.getOrdersByStatus(1, 18);

        sendJson(resp, orderList);
    }
}
