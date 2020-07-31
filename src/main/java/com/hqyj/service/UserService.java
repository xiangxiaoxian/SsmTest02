package com.hqyj.service;

import com.hqyj.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName UserService.java
 * @Description TODO
 * @createTime 2020年07月28日 16:38:00
 */
public interface UserService {
    String checkLogin(User user, String mm, HttpServletResponse response,HttpServletRequest request);

    User queryCookle(HttpServletRequest request);
}
