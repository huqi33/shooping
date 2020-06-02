package com.huqi.fronted.servlet;

import com.huqi.base.BaseSevlet;
import com.huqi.commons.SendData;
import com.huqi.fronted.domain.SmsCode;
import com.huqi.fronted.service.UserService;
import com.huqi.fronted.service.impl.SmsService;
import com.huqi.fronted.service.impl.UserServiceImpl;
import org.apache.commons.text.RandomStringGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/sms")
public class SmsServlet extends BaseSevlet {

    private UserService userService = new UserServiceImpl();

    private SmsService smsService =  new SmsService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //要发送的数据
        SendData sendData = null;

        //获取电话号码
        String phone = req.getParameter("phone");

        //验证手机号是否注册过
        boolean check = userService.checkUsernameOrPassword(phone);
        if(check){
            sendData = new SendData(-2,"手机号注册过");
            sendJson(resp,sendData);
            return;
        }

        //随机生成4位的验证码
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange(new char[]{'0','9'}).build();
        String code = randomStringGenerator.generate(4);

        SmsCode smsCode = new SmsCode(phone,code,120);
        //存储信息到session中
        req.getSession().setAttribute("smsCode",smsCode);

        System.out.println(req.getSession().getAttribute("smsCode"));

        try{
            smsService.sendSms(phone, code);
            sendData = new SendData(1,"发送成功");
        }catch(Exception e){
            e.printStackTrace();
            sendData = new SendData(-1,"发送失败");
        }

        sendJson(resp,sendData);


    }
}
