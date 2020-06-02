package com.huqi.fronted.servlet;

import com.huqi.base.BaseSevlet;
import com.huqi.commons.SendData;
import com.huqi.fronted.service.UserService;
import com.huqi.fronted.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends BaseSevlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取用户信息
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String gender = req.getParameter("gender");
        String phone = req.getParameter("phone");

        boolean register = userService.register(phone, username, password, email, gender);

        SendData sendData;
        if(register){
            sendData = new SendData(1,"注册成功");
        }else {
            sendData = new SendData(-1,"注册失败,请重试");
        }

        sendJson(resp, sendData);

    }
}
