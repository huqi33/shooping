package com.huqi.fronted.service.impl;

import com.huqi.commons.SendData;
import com.huqi.fronted.dao.CartDao;
import com.huqi.fronted.dao.GoodDao;
import com.huqi.fronted.dao.OrderDao;
import com.huqi.fronted.dao.UserDao;
import com.huqi.fronted.dao.impl.CartDaoImpl;
import com.huqi.fronted.dao.impl.GoodDaoImpl;
import com.huqi.fronted.dao.impl.OrderDaoImpl;
import com.huqi.fronted.dao.impl.UserDaoImpl;
import com.huqi.fronted.domain.Goods;
import com.huqi.fronted.domain.Order;
import com.huqi.fronted.domain.WebOrder;
import com.huqi.fronted.service.OrderService;
import com.huqi.util.DbUtil;
import org.apache.commons.text.RandomStringGenerator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderServiceImpl implements OrderService {
    private final String imageServerLocation = "http://localhost/";

    private OrderDao orderDao = new OrderDaoImpl();
    private GoodDao goodDao = new GoodDaoImpl();
    private CartDao cartDao = new CartDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    /**
     *  生成订单
     * @param goodsInfo
     * @param defaultTakeDelivertyAddressId
     * @param userId
     * @return
     */
    @Override
    public SendData ensureOrder(Map<Integer, Integer> goodsInfo, Integer defaultTakeDelivertyAddressId, Integer userId) {

        /**
         *
         *  用户传来商品id和商品的数量
         *  可能有多件商品要购买
         *  开启一个事务进行检测库存,给用商品的id给相同的商品加上同步锁
         *  如果库存够可以正常扣款
         *  如果库存不足则进行数据回滚
         *  当所有的商品库存够减了之后
         *  进行订单的插入,如果出现异常就进行回滚,连前面的一起,相当于一个事务中还有另一个事务
         *  生成订单之后删除用户购物车的数据,算是完成了这个状态
         *
         */

        Set<Integer> goodIds = goodsInfo.keySet();


        Connection conn = DbUtil.getConnection();
        SendData sendData = null;

        //总的价格， 要在前端支付的价格
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        // 需要返回到前端的数据
        Map<String, Object> resultData = new HashMap<>(); // {"totalPrice": , "orderNo": }

        String oderNo = generateOrderNo(); //获取订单编号

        System.out.println("订单编号 : " + oderNo);
        try {
            conn.setAutoCommit(false);

            // 将集合转换一个对应类型的数组
            Integer[] ids = goodIds.toArray(new Integer[goodIds.size()]);
            System.out.println("ids : " +  ids);

            // 根据商品id查出所有的商品信息
            List<Goods> goodsList = goodDao.getGoodsByIds(ids);

            int num = 0; // 购物车的单件商品数量
            int stockNum = 0; // 单件商品的库存


            // 判断每件商品的库存是否足够,如果足够就修改商品的库存
            for(Integer s : goodIds){
                num = goodsInfo.get(s);
                synchronized (s){
                    stockNum = goodDao.getStockOfSpecifiedGoods(s);
                    System.out.println("  stockNum  : " + stockNum + "  num : " + num);
                    if(stockNum >= num){
                        //执行修改语句
                        goodDao.updateStockNum(stockNum - num,s, conn);
                    }else {
                        conn.rollback();
                        conn.close();
                        sendData = new SendData(-1, "更新失败");
                        return sendData;
                    }
                }
            }


            // 进行订单的生成,每个商品一条记录,订单号是一样的
            for(Goods g : goodsList) {
                Integer goodId = g.getGoodId();
                BigDecimal price = g.getPrice(); //商品的价格
                String title = g.getTitle();
                String image = g.getImageSrc();
                num = goodsInfo.get(goodId); //实际购买的商品的数量
                System.out.println("goodId : " + goodId);
                // 插入订单
                orderDao.insertOrder(oderNo, g.getPrice(), num, image, title, 0, 0, userId, goodId, conn);

                /**
                 * add()   加上
                 * multiply()  乘法
                 * divide()   除法
                 * subtract() 减法
                 */
                totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(num)));
            }

            // 插入订单对应的地址
            orderDao.insertOrderAddress(oderNo, defaultTakeDelivertyAddressId, conn);

            // 删除用户的购物车数据
            cartDao.deleteShoppingCartDataOfUser(userId, conn);

            //提交事务
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
                conn.close();
                sendData = new SendData(-1, "更新失败");
                System.out.println("失败了...>>>>");
                return sendData;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        sendData = new SendData(1,"成功");

        // 商品的总价格
        resultData.put("totalPrice", totalPrice);
        resultData.put("orderNo", oderNo);

        sendData.setObj(resultData);  //设置总的价格

        return sendData;

    }

    /**
     *  用户确认支付
     * @param orderNo  订单编号
     * @param userId   用户id
     * @return
     */
    @Override
    public SendData confirmPay(String orderNo, Integer userId) {
        /**
         * 确认支付的流程：
         *      1.扣除金额(但是要先判断金额是否够)
         *      2.修改订单的状态，变为 “已支付”
         */
        BigDecimal balance = userDao.getBalanceOfUser(userId); //获取用户的余额

        // 查询对应订单编号的所有的订单，目的是为了计算订单的总金额
        List<Order> list = orderDao.getOrdersOfOrderNo(orderNo);

        // 需要支付的总金额
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        // 集合有流处理
        for(Order o : list) {
            BigDecimal price = o.getGoodPrice(); //获取商品的价格
            Integer num = o.getGoodNum(); //获取商品的数量

            // 单个商品的总价格
            BigDecimal singleGoodsTotalPrice = price.multiply(BigDecimal.valueOf(num));

            totalPrice = totalPrice.add(singleGoodsTotalPrice);
        }

        SendData data = null;

        BigDecimal remainBalance = null;
        /**
         * balance如果小于totalPrice，返回 -1
         * balance如果等于totalPrice，返回 0
         * balance如果大于totalPrice，返回 1
         */
        if(balance.compareTo(totalPrice) >= 0) {  // 用户的余额足够支付订单
            remainBalance = balance.subtract(totalPrice);
        }else {  //钱不够
            data = new SendData(-1, "你的余额不足.");
            return data;
        }

        Connection connection = DbUtil.getConnection();
        try {
            connection.setAutoCommit(false);  //设置事务不自动提交

            userDao.updateBalanceOfUser(remainBalance, userId, connection);  //更新用户的余额

            orderDao.updateOrderStatus(orderNo, 1, connection);

            connection.commit(); //提交事务
        }catch (Exception ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            data = new SendData(-2, "支付失败, 请稍后再试.");
            return data;
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        data = new SendData(1, "支付成功");
        return data;
    }

    /**
     *  查询用户某个订单状态的所有订单
     * @param status 状态
     * @param userId 用户id
     * @return  符合条件的所有订单
     */
    @Override
    public List<WebOrder> getOrdersByStatus(Integer status, Integer userId) {
        // 获取该用户该状态所有的订单数据
        List<Order> orderList = orderDao.getOrdersByStatus(status, userId);

        // 要返回的数据
        List<WebOrder> webOrderList = new ArrayList<>();

        /**
         * map作用是将订单按照订单编号来分类。key是订单编号，value是拥有相同订单编号的订单.
         */
        Map<String, List<Order>> map = new HashMap<>();

        /**
         * map的作用是存储对应订单的创建日期，key是订单编号，value是订单的创建日期
         */
        Map<String, Date> orderTimeMap = new HashMap<>();

        for(Order o : orderList) {
            String orderNo = o.getOrderNo(); //订单编号
            Date createTime = o.getCreateTime(); //订单的创建日期
            o.setGoodsImg(imageServerLocation + o.getGoodsImg());

            // 获取该订单编号的创建日期
            Date createDate = orderTimeMap.get(orderNo);
            if(null == createDate) {
                orderTimeMap.put(orderNo, createTime);
            }

            /**
             * [
             *    {orderNo: 1, XXXXX},  map -> {1: list[xxxxx]}
             *    {orderNo: 1, ZZZZ},  map -> {1: list[xxxxx, ZZZZ]}
             *    {orderNo: 2, yyyyy}, map ->{1: list[xxxxx, ZZZZ], 2: list[yyyyy]}
             *    {orderNo: 2, oooo},  map ->{1: list[xxxxx, ZZZZ], 2: list[yyyyy]}
             *    {orderNo: 2, XXXXX},
             * ]
             */
            // 获取该订单编号对应的所有的订单
            List<Order> orders = map.get(orderNo);
            if(null == orders) {  //该订单编号没有对应的订单
                orders = new ArrayList<>();
                orders.add(o);
                map.put(orderNo, orders);
            }else { //该订单编号有订单了
                orders.add(o);
            }
        }


        map.forEach((k, v) -> {
            WebOrder webOrder = new WebOrder();

            webOrder.setOrderNo(k);
            webOrder.setOrders(v);

            Date createTime = orderTimeMap.get(k);  //订单的创建日期

            webOrder.setCreateTime(createTime);

            webOrderList.add(webOrder);
        });

        return webOrderList;
    }

    /**
     * 生成订单号： 按照 年月日十分秒毫秒 + 6位随机码
     * @return
     */
    private String generateOrderNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        String datePrefix = sdf.format(date);

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(new char[]{'0', '9'}).build();

        String randomNum = generator.generate(6);

        return datePrefix + randomNum;
    }
}
