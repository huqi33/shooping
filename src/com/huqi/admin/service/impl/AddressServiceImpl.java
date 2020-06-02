package com.huqi.admin.service.impl;

import com.huqi.admin.dao.AddressDao;
import com.huqi.admin.dao.impl.AddressDaoImpl;
import com.huqi.admin.entry.Address;
import com.huqi.admin.service.AddressService;

import java.util.List;

public class AddressServiceImpl implements AddressService {

    private AddressDao addressDao = new AddressDaoImpl();

    @Override
    public List<Address> getAddress(int id) {
        return addressDao.getAddress(id);
    }
}
