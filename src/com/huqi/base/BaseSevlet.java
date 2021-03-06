package com.huqi.base;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseSevlet extends HttpServlet {

    protected void sendJson(HttpServletResponse resp, Object obj) throws IOException {
        String jsonStr = JSONObject.toJSONString(obj);

        // 反会json的MIME类型
        resp.setContentType("application/json;charset=utf-8");
        // 告诉浏览器，我这个谁都可以拿
        resp.setHeader("Access-Control-Allow-Origin", "*");

        PrintWriter writer = resp.getWriter();
        writer.write(jsonStr);

        writer.flush();
        writer.close();
    }

}
