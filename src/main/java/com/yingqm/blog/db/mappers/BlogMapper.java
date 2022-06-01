package com.yingqm.blog.db.mappers;

import com.yingqm.blog.db.po.Blog;

import java.util.List;

public interface BlogMapper {
    int deleteByPrimaryKey(Long blogId);

    int insert(Blog row);

    int insertSelective(Blog row);

    Blog selectByPrimaryKey(Long blogId);

    int updateByPrimaryKeySelective(Blog row);

    int updateByPrimaryKey(Blog row);

    List<Blog> selectByUserName(String userName);

    List<Blog> selectTopBlogs();
}