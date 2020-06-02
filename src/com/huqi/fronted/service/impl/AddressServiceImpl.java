package com.huqi.fronted.service.impl;

import com.huqi.admin.entry.Address;
import com.huqi.fronted.dao.AddressDao;
import com.huqi.fronted.dao.impl.AddressDaoImpl;
import com.huqi.fronted.service.AddressService;

import java.util.List;

public class AddressServiceImpl implements AddressService {

    private AddressDao addressDao = new AddressDaoImpl();

    @Override
    public List<Address> getAddressByUserId(Integer userId) {
        return addressDao.getAddressByUserId(userId);
    }

    @Override
    public void changeTakeDeliveryAddress(Integer userId, Integer addressId) {

    }
}
