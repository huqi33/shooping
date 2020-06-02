package com.huqi.fronted.servlet;

import com.huqi.base.BaseSevlet;
import com.huqi.commons.SendData;
import com.huqi.fronted.domain.SmsCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/validate")
public class ValidateCodeAndPhoneServlet extends BaseSevlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("-----");

        //验证手机号是否位发送验证码的手机号
        SmsCode smsCode = (SmsCode) req.getSession().getAttribute("smsCode");

        String phone = req.getParameter("phone");
        String code = req.getParameter("validateCode");

        SendData sendData = null;

        if(!smsCode.isExpire()){
            sendData = new SendData(-1,"验证码已过期");
            System.out.println(1);
            sendJson(resp,sendData);
            return;
        }

        if (code == null || !smsCode.getCode().equals(code)){
            sendData = new SendData(-2,"验证码错误");
            System.out.println(2);
            sendJson(resp,sendData);
            return;
        }

        if(!smsCode.getPhone().equals(phone)){
            sendData = new SendData(-3,"恶意用户");
            System.out.println(3);
            sendJson(resp,sendData);
            return;
        }

        sendData = new SendData(1,"验证通过");
        sendJson(resp, sendData);


    }
}
