package com.yingqm.blog.db.dao;


import com.yingqm.blog.db.po.User;

public interface UserDao {
    public void insertUser(User user);

    public User selectUser(Long userId);

    public User selectUserByName(String name);
}
