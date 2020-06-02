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

@WebServlet("/validate-username")
public class ValidateUsernameServlet extends BaseSevlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");

        //System.out.println(username);
        boolean b = userService.checkUsernameOrPassword(username);

        System.out.println(b);
        SendData sendData = null;
        if (b){
            sendData = new SendData(-1,"用户名已经注册过");
        }else {
            sendData = new SendData(1,"用户没有注册过");
        }

        sendJson(resp, sendData);

    }
}
