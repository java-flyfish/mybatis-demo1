package com.my.batis.dao;

import com.my.batis.bean.User;

import java.util.List;

public interface UserMapper {

    List<User> findAll();

    List<User> getUser(int lfPartyId);

    Integer insertUser(User user);

    Integer updateByUser(User user);

    Integer deleteByUserById(int id);
}
