package com.huqi.fronted.dao;

import com.huqi.admin.entry.Address;

import java.util.List;

public interface AddressDao {

    List<Address> getAddressByUserId(Integer userId);

    void changeTakeDeliveryAddress(Integer userId, Integer addressId);

}
