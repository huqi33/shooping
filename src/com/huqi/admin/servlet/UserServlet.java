package com.huqi.admin.servlet;

import com.huqi.base.BaseSevlet;
import com.huqi.commons.TableData;
import com.huqi.admin.entry.User;
import com.huqi.admin.service.UserService;
import com.huqi.admin.service.impl.UserServiceImpl;
import com.huqi.util.FormatUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/user",name = "UserServlet")
public class UserServlet extends BaseSevlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String methodName = req.getParameter("method");
        System.out.println( "method : " + methodName);
        try {
            Method method = UserServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this,req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 查询用户
     * @param req
     * @param resp
     * @throws IOException
     */
    private void getUserPageData(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TableData<User> pageData = userService.getPageData(null, "-1", null, null, null, 1, 10);
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        int status = Integer.parseInt(req.getParameter("status"));
        String email = req.getParameter("email");
        String begin = req.getParameter("beginRegisterDate");
        String end = req.getParameter("endRegisterDate");
        int offset = Integer.parseInt(req.getParameter("offset"));
        int limit = Integer.parseInt(req.getParameter("limit"));


        Date beginRegisterDate = null;
        if(begin != null && !begin.equals("")){
            beginRegisterDate = FormatUtil.getFormatDate(begin,"yyyy-MM-dd HH:mm:ss");
        }
        Date endRegisterDate = null;
        if(end != null && !"".equals(end)){
            endRegisterDate = FormatUtil.getFormatDate(end,"yyyy-MM-dd HH:mm:ss");
        }

        System.out.println("----------------------------");

        System.out.println(begin);
        System.out.println(end);

        System.out.println(beginRegisterDate);
        System.out.println(endRegisterDate);

        System.out.println("-------------------------------");
        TableData<User> pageData = userService.getPageData(name, gender, status, email, beginRegisterDate, endRegisterDate, offset, limit);

        sendJson(resp,pageData);
    }

    /**
     * 激活用户
     * @param req
     * @param resp
     * @throws IOException
     */
    private void activeUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean isActive = userService.activeUser(Integer.parseInt(req.getParameter("id")));

        Map<String, String> map = new HashMap<>();

        if(isActive){
            map.put("code","1");
        }else{
            map.put("code","-1");
        }
        sendJson(resp, map);
    }

}
