package com.yingqm.blog.db.dao;

import com.yingqm.blog.db.mappers.BlogMapper;
import com.yingqm.blog.db.po.Blog;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class BlogDaoImpl implements BlogDao {
    @Resource
    BlogMapper blogMapper;

    @Override
    public void insertBlog(Blog blog) {
        blogMapper.insert(blog);
    }

    @Override
    public List<Blog> selectBlogsByUserName(String userName) {
        return blogMapper.selectByUserName(userName);
    }

    @Override
    public List<Blog> getTopBlogs() {
        return blogMapper.selectTopBlogs();
    }

    @Override
    public Blog selectBlogById(Long Id) {
        return blogMapper.selectByPrimaryKey(Id);
    }

}
