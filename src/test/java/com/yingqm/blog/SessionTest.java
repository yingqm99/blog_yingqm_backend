package com.yingqm.blog;

import com.yingqm.blog.controller.UserController;
import com.yingqm.blog.db.po.User;
import com.yingqm.blog.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class SessionTest {
    @Resource
    LoginService loginService;

    @Resource
    UserController controller;

    @Test
    public void addSessionTest() {
        HttpSession session = new MockHttpSession();

        Map<String, Object> info = new HashMap<>();
        info.put("name", "test2");
        info.put("email", "yingqm2@umich.edu");
        info.put("password", "111");
        info.put("repassword", "111");
        controller.register(info, session);

        assert(loginService.validateLogin("test2", session));
        Set<String> users = (Set<String>)session.getAttribute("users");

        assert(users.contains("test1"));

    }
}
