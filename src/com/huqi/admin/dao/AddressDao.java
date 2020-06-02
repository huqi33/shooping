package com.huqi.admin.dao;

import com.huqi.admin.entry.Address;

import java.util.List;

public interface AddressDao {

    List<Address> getAddress(int id);

}
