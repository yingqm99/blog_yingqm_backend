package com.yingqm.blog.db.dao;

import com.yingqm.blog.db.mappers.UserMapper;
import com.yingqm.blog.db.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserDaoImpl implements UserDao {
    @Resource
    private UserMapper userMapper;

    @Override
    public void insertUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public User selectUser(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public User selectUserByName(String name) {
        return userMapper.selectUserByName(name);
    }

}
