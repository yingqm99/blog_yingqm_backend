package com.yingqm.blog.db.dao;

import com.yingqm.blog.db.po.Blog;

import java.util.List;

public interface BlogDao {
    public void insertBlog(Blog blog);

    public List<Blog> selectBlogsByUserName(String userName);

    List<Blog> getTopBlogs();
}
