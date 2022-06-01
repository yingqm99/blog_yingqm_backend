package com.yingqm.blog.controller;


import com.yingqm.blog.db.dao.BlogDao;
import com.yingqm.blog.db.po.Blog;
import com.yingqm.blog.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class BrowseController {

    @Autowired
    private BlogDao blogDao;

    @Resource
    private S3Service s3Service;

    @ResponseBody
    @GetMapping("/all_blogs")
    @CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
    public Map<String, Object> mainPage() {
        Map<String, Object> result = new HashMap<>();
        List<Blog> blogs = blogDao.getTopBlogs();
        System.out.println(blogs.size());
        result.put("status", true);
        result.put("blogs", blogs);
        return result;
    }

    @ResponseBody
    @GetMapping("/post")
    @CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
    public Map<String, Object> get_post(@RequestParam String id) {
        Map<String, Object> result = new HashMap<>();
        Blog blog = blogDao.selectBlogById(Long.parseLong(id));
        String blogText = s3Service.readBlog(blog.getName(), blog.getUserName());
        result.put("status", true);
        result.put("title", blog.getName());
        result.put("blogUser", blog.getUserName());
        result.put("blogText", result);
        return result;
    }

}
