package com.hqyj.controller;

import com.hqyj.pojo.User;
import com.hqyj.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName UserController.java
 * @Description TODO
 * @createTime 2020年07月28日 16:37:00
 */

@Controller
@RequestMapping("pc")
public class UserController {
    @Autowired
    private UserService us;

    //登录验证
    @RequestMapping("checkLogin.ajax")
    @ResponseBody
    public String checkLogin(User user, String mm, HttpServletResponse response,HttpServletRequest request){
        //System.err.println(user);
        System.err.println(mm);
        String info=us.checkLogin(user,mm,response,request);
        return info;
    }

    //跳转
    @RequestMapping("list.ajax")
    public String list(){
        return "index";
    }
    //跳转
    @RequestMapping("user.do")
    public String user(){
        return "user";
    }

    //跳转
    @RequestMapping("admin.do")
    public String admin(){
        return "admin";
    }

    //登出
    @RequestMapping(value = "logout.do",produces = "application/json;charset=utf-8")
    @ResponseBody
    public User logout(HttpServletRequest request){
        //拿到当前认证的用户
        Subject s= SecurityUtils.getSubject();
        //退出认证
        s.logout();
        User user=us.queryCookle(request);
        return user;
    }
    //右侧页面
    //跳转
    @RequestMapping("showWelcome.do")
    public String showWelcome(){
        return "welcome";
    }
}
