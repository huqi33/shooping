package com.huqi.admin.servlet;


import com.huqi.base.BaseSevlet;
import com.huqi.admin.entry.Address;
import com.huqi.admin.service.AddressService;
import com.huqi.admin.service.impl.AddressServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/address")
public class AddressServlet extends BaseSevlet {

    private AddressService addressService = new AddressServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取id
        int id = Integer.parseInt(req.getParameter("id"));

        List<Address> address = addressService.getAddress(id);

        System.out.println("address : " + address);
        sendJson(resp, address);

    }
}
