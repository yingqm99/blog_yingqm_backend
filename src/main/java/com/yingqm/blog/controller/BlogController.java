package com.yingqm.blog.controller;

import com.yingqm.blog.db.dao.BlogDao;
import com.yingqm.blog.db.dao.UserDao;
import com.yingqm.blog.db.po.Blog;
import com.yingqm.blog.service.LoginService;
import com.yingqm.blog.util.BlogGenerator;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class BlogController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private LoginService loginService;

//    @Autowired
//    private S3Service s3Service;

    private BlogGenerator blogGenerator = new BlogGenerator();

    /**
     * Controller for uploading a blog
     * @param body
     * @param session
     * @return
     */
    @PostMapping("/blog")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
    public Map<String, Object> uploadBlog(@RequestBody Map<String, Object> body,
                           HttpSession session) {
        String name = body.get("name").toString();
        String title = body.get("title").toString();
        String text = body.get("text").toString();
        log.info(name);
        Map<String, Object> resp = new HashMap<>();

        if (loginService.validateLogin(name, session)) {
            Blog blog = new Blog();
            blog.setName(title);
            blog.setUserId(userDao.selectUserByName(name).getUserId());
            blog.setUserName(name);
            blog.setCategory("");
            blogDao.insertBlog(blog);
            blogGenerator.generateFile(title, name, text);
//            s3Service.addBlog(title, name);
            resp.put("status", true);
        } else {
            resp.put("status", false);
            resp.put("err", "DID NOT LOGIN");
        }
        return resp;
    }


    /**
     * Controller for retrieving all blogs
     * @param userName
     * @param session
     * @return
     */
    @GetMapping("/blogs")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
    public Map<String, Object> getBlogs(@RequestParam("name") String userName, HttpSession session) {
        Map<String, Object> resp = new HashMap<>();
        if (!loginService.validateLogin(userName, session)) {
            resp.put("status", false);
            resp.put("err", "DID NOT LOGIN");
            log.info("FAILED TO LOAD BLOG INFO");
            return resp;
        }

        List<Blog> blogs = blogDao.selectBlogsByUserName(userName);
        resp.put("status", true);
//        List<String> blogTitles = new ArrayList<>();
//        for (Blog blog : blogs) {
//            blogTitles.add(blog.getName());
//        }
        resp.put("blogs", blogs);
        return resp;

    }

}
