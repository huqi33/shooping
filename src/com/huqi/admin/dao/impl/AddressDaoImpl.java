package com.huqi.admin.dao.impl;

import com.huqi.admin.dao.AddressDao;
import com.huqi.admin.entry.Address;
import com.huqi.util.DataUtils;

import java.util.List;

public class AddressDaoImpl implements AddressDao {
    @Override
    public List<Address> getAddress(int id) {

        String sql = "select a_id id,a_name name, a_phone phone, a_detail detail, a_state state from address where u_id = ? ";
        List<Address> allPro = DataUtils.getAllPro(Address.class, sql, id);

        return allPro;
    }
}
