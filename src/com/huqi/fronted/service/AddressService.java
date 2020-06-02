package com.huqi.fronted.service;

import com.huqi.admin.entry.Address;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户id查出所有地址
     * @param userId
     * @return
     */
    List<Address> getAddressByUserId(Integer userId);


    void changeTakeDeliveryAddress(Integer userId, Integer addressId);
}
