package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.AddressRepository;
import com.aweqy.jewelrypurchaseweb.jpw.Address;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> searchAddress(String userId) {
        return addressRepository.findByUserId(userId);
    }

    public Result<Address> addAddress(String userId, String addressName, String phone, String username) {
        try {
            addressRepository.save(new Address(userId, addressName, phone, username));
        } catch (Exception e) {
            return new Result<>("404","失败",null);
        }
        return new Result<>("200","成功",new Address(userId, addressName, phone, username));
    }

    public Result<Address> deleteAddress(int addressId) {
        Optional<Address> existingAddress = addressRepository.findById(addressId);
        if (existingAddress.isPresent()) {
            addressRepository.deleteById(addressId);
            return new Result<>("200","成功",null);
        }
        return new Result<>("404","删除失败，未找到数据或接受到错误数据",null);
    }

    public Result<?> getAddress(int addressId) {

        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (!optionalAddress.isPresent()) {
            return new Result<>("404","未找到相关地址",null);
        }
        return new Result<>("200","成功获取到地址！",optionalAddress.get());
    }
}
