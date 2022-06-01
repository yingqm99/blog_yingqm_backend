package com.yingqm.blog.controller;

import com.yingqm.blog.db.dao.BlogDao;
import com.yingqm.blog.db.dao.UserDao;
import com.yingqm.blog.db.po.Blog;
import com.yingqm.blog.db.po.User;
import com.yingqm.blog.service.LoginService;
import com.yingqm.blog.util.UserResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Slf4j
@Controller
public class UserController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
    public UserResp register(@RequestBody Map<String, Object> body, HttpSession session) {
        String name = body.get("name").toString();
        String email = body.get("email").toString();
        String password = body.get("password").toString();
        String rePassword = body.get("repassword").toString();
        UserResp resp = new UserResp();
        if (!password.equals(rePassword)) {
            resp.status = false;
            resp.err = "PASSWORD NOT SAME";
            return resp;
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userDao.insertUser(user);
        resp.status = true;
        resp.err = "";
        resp.userName = name;

        if (!loginService.login(user, session)) {
            resp.status = false;
            resp.err = "AFTER REGISTRATION, LOGIN FAILED";
        }

        return resp;
    }

    @RequestMapping("/login")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
    public Map<String, Object> login(@RequestBody Map<String, Object> body,
                      HttpSession session) {
        String name = body.get("name").toString();
        String password = body.get("password").toString();
        Map<String, Object> resp = new HashMap<>();
        User user =  new User();
        user.setName(name);
        user.setPassword(password);

        resp.put("status", loginService.login(user, session));
        if ((boolean) resp.get("status")) {
            resp.put("userName", name);
        }
        List<Blog> blogs = blogDao.selectBlogsByUserName(name);
        resp.put("blogs", blogs);
        log.info(blogs.toString());


        return resp;
    }


    @DeleteMapping("/logout")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
    public UserResp logout(@RequestParam String name,
                       HttpSession session) {
        // String name = body.get("name").toString();
        UserResp resp = new UserResp();
        Set<String> users = (Set<String>) session.getAttribute("users");
        if (users.contains(name)) {
            users.remove(name);
            session.setAttribute("users", users);
            resp.status = true;
        } else {
            resp.status = false;
        }
        return resp;
    }


}
