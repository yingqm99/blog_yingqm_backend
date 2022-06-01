package com.yingqm.blog.db.mappers;

import com.yingqm.blog.db.po.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    User selectUserByName(String name);
}