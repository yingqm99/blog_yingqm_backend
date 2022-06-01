package com.yingqm.blog.service;

import com.yingqm.blog.db.dao.UserDao;
import com.yingqm.blog.db.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class LoginService {
    @Autowired
    private UserDao userDao;


    public boolean login(User user, HttpSession session) {
        User userFromDb = userDao.selectUserByName(user.getName());

        if (userFromDb == null
                || !user.getPassword().equals(userFromDb.getPassword())) {
            if (userFromDb == null) {
                log.info("object is null");
                return false;
            }
            log.info(user.getPassword());
            log.info(userFromDb.getPassword());
            log.info("login failed: " + user.getName());
            return false;
        }

        Set<String> users = (Set<String>)session.getAttribute("users");
        if (users == null) {
            users = new HashSet<String>();
        }
        users.add(user.getName());
        session.setAttribute("users", users);
        log.info( session.getAttribute("users").toString());
        log.info("session added");
        return true;
    }

    public boolean validateLogin(String userName, HttpSession session) {
        log.info(userName);
        Set<String> loginUsers = (Set<String>)session.getAttribute("users");
        log.info(session.toString());
        if (loginUsers == null) {
            log.info("null loginusers");
            loginUsers = new HashSet<String>();
        }
        if (loginUsers.contains(userName)) {
            return true;
        }
        return false;
    }

}
