package com.huqi.fronted.servlet;

import com.huqi.admin.entry.Address;
import com.huqi.base.BaseSevlet;
import com.huqi.commons.SendData;
import com.huqi.fronted.service.AddressService;
import com.huqi.fronted.service.impl.AddressServiceImpl;
import javafx.scene.SnapshotParameters;
import sun.security.jgss.spnego.SpNegoContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet("/addressServlet")
public class AddressServlet extends BaseSevlet {
    private AddressService addressService = new AddressServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("method");

        try {
            Method method = AddressServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     *  根据用户id查到这个用户的所有地址
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void getAddressOfUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        // HttpSession session = req.getSession();
        // User user = (User) session.getAttribute("user");
        List<Address> addressByUserId = addressService.getAddressByUserId(1);

        sendJson(resp, addressByUserId);

    }

    /**
     *  修改用户的默认地址
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void changeTakeDeliveryAddress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // HttpSession session = req.getSession();
        // User user = (User) session.getAttribute("user");

        String addressId = req.getParameter("addressId");

        SendData sendData = null;
        try {
            addressService.changeTakeDeliveryAddress(1, Integer.parseInt(addressId));
            sendData = new SendData(1, "更改成功");
        }catch (Exception e){
            e.printStackTrace();
            sendData = new SendData(-1, "修改失败");
        }

        sendJson(resp, sendData);

    }

}
