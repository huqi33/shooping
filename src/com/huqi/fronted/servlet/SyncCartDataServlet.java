package com.huqi.fronted.servlet;

import com.alibaba.fastjson.JSONObject;
import com.huqi.base.BaseSevlet;
import com.huqi.commons.SendData;
import com.huqi.fronted.service.CartService;
import com.huqi.fronted.service.impl.CartServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet("/sync-cart")
public class SyncCartDataServlet extends BaseSevlet {

    private CartService cartService = new CartServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String methodName = req.getParameter("method");
        // 反射执行方法
        try {
            Method declaredMethod = SyncCartDataServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            declaredMethod.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *  合并购物车的数据用
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void getCartInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("来了..");
        String browserShoppingCartData = req.getParameter("cartData");


        /**
         * 用户登录之后分为两种情况
         *      1> 用户的本地浏览器中没有数据
         *             a.从session中获取用户的id,直接查询出用户的购物车信息,判断非空返回
         *      2> 用户的本地浏览器中有数据
         *             a.将String数据转换为Map集合,其中的内容是,{gooid: xx, num : num}
         *             b.将转换后的本地浏览器数据传输到service层进行处理
         */

        // HttpSession session = req.getSession();
        // User user = (User) session.getAttribute("user");

        System.out.println("browserShoppingCartData = " +  browserShoppingCartData);

        //存在数据
        if(browserShoppingCartData != null && !browserShoppingCartData.equals("")){


            Map<String, Integer> cartData = JSONObject.parseObject(browserShoppingCartData, Map.class);

            Map<String, Integer> cartInfo = cartService.mergeCartData(cartData, 18);


            SendData sendData = new SendData(1,"更新成功");
            if(cartInfo != null){
                sendData.setObj(cartInfo);
            }
            sendJson(resp, sendData);
        }else { // 没有数据
            //直接从购物车查询数据返回
            Map<String, Integer> cartInfoOfUser = cartService.getCartInfoOfUser(18);
            SendData sendData = null;
            if(cartInfoOfUser.size() == 0){
                sendData = new SendData(-1, "购物车中没有数据");
            }else {
                sendData =  new SendData(2 , "购物车中有数据");
                sendData.setObj(cartInfoOfUser);
            }
            sendJson(resp, sendData);
        }
    }

    /**
     * 根据商品商品的id将商品添加到购物车中
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void addGood(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String goodId = req.getParameter("goodId");

        // HttpSession session = req.getSession();
        // User user = (User) session.getAttribute("user");

        SendData sendData = null;


        //此处需要判断这件商品是否存在于用户购物车中,如果存在只用将该商品的数量+1即可,否则是添加新的商品数据
        try {
            Integer gid = Integer.parseInt(goodId);
            Integer num = cartService.goodsIsCartByUser(18,gid);
            if(num > 0){
                cartService.updateGoodNum(18,gid,++num);
            }else {
                cartService.addGood(18, gid);
            }
            sendData = new SendData(1, "添加成功");
        }catch (Exception e){
            e.printStackTrace();
            sendData = new SendData(-1, "添加失败");
        }

        sendJson(resp, sendData);

    }

    /**
     *  改变商品的数量
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void changeGoodNum(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String goodId = req.getParameter("goodId");
        String num = req.getParameter("num");

        // HttpSession session = req.getSession();
        // User user = (User) session.getAttribute("user");

        SendData sendData = null;
        try {
            cartService.updateGoodNum(18, Integer.parseInt(goodId), Integer.parseInt(num));
            sendData = new SendData(1, "更新成功");
        }catch (Exception e){
            e.printStackTrace();
            sendData = new SendData(-1, "更新失败");
        }

        sendJson(resp, sendData);
    }


    /**
     * 移除商品
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void deleteGood(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String goodId = req.getParameter("goodId");

        // HttpSession session = req.getSession();
        // User user = (User) session.getAttribute("user");

        cartService.deleteCartData(18,Integer.parseInt(goodId));

    }


}
