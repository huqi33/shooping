package com.huqi.fronted.servlet;

import com.huqi.admin.entry.User;
import com.huqi.base.BaseSevlet;
import com.huqi.commons.SendData;
import com.huqi.fronted.service.UserService;
import com.huqi.fronted.service.impl.UserServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseSevlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        SendData sendData = null;
        User user = null;
        try{
            user = userService.checkUsernameAndPassword(username, DigestUtils.md5Hex(password));

        }catch (Exception e){
            e.printStackTrace();
            sendData = new SendData(-2,"登录失败");
        }




        if(user != null){

            // 存session
            req.getSession().setAttribute("user",user);

            sendData = new SendData(1,"登录成功");
        }else {
            sendData = new SendData(-1, "登录失败");
        }

        sendJson(resp, sendData);


    }
}
