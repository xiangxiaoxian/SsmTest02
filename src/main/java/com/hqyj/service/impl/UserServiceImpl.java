package com.hqyj.service.impl;


import com.hqyj.pojo.User;
import com.hqyj.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName UserServiceImpl.java
 * @Description TODO
 * @createTime 2020年07月28日 16:38:00
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public String checkLogin(User user, String mm, HttpServletResponse response,HttpServletRequest request) {
        System.err.println("==进来了==");
        //拿到当前用户
        Subject s= SecurityUtils.getSubject();
        //判断当前用户是否被认证，并做相关处理
        if (!s.isAuthenticated()){
            //UsernamePasswordToken 令牌类 稍后会把保存在里面得用户名密码和shiro的身份和凭证比对
            UsernamePasswordToken utp=new UsernamePasswordToken(user.getUsername(),user.getPassword());
            utp.setRememberMe(true);
            try {
                //进行认证（因为我们写了自定义的realm类，所以会自动到realm
                // 类里进行认证）
                s.login(utp);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                //保存用户名在session
                s.getSession().setAttribute("userName",user.getUsername());
                s.getSession().setAttribute("LOGINTIME",simpleDateFormat.format(new Date()));
                if ("YES".equals(mm)){
                    /*//1.创建cookie
                    Cookie c1=new Cookie("USERNAME",user.getUsername());
                    Cookie c2=new Cookie("PASSWORD",user.getPassword());
                    //2.设置cookie的时间
                    c1.setMaxAge(1000);
                    c2.setMaxAge(1000);
                    //3.将cookie写回给浏览器
                    response.addCookie(c1);
                    response.addCookie(c2);*/
                    //shiro cookie的使用
                    //创建SimpleCookie
                    SimpleCookie simpleCookie=new SimpleCookie();
                    SimpleCookie simpleCookie1=new SimpleCookie();
                    //在cookie中存值
                    simpleCookie.setName("USERNAME");
                    simpleCookie.setValue(user.getUsername());
                    simpleCookie1.setName("PASSWORD");
                    simpleCookie1.setValue(user.getPassword()+user.getUsername());
                    //设置cookie时间
                    simpleCookie.setMaxAge(60*60*24*30);
                    simpleCookie1.setMaxAge(60*60*24*30);
                    //写回给浏览器
                    simpleCookie.saveTo(request,response);
                    simpleCookie1.saveTo(request,response);
                }else {
                    System.out.println("没有拿到");
                    Cookie[] cookies = request.getCookies();
                    if (cookies!=null){
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals("USERNAME")){
                                //servlet 如何删除Cookie ,将时间设置为0 并返回给浏览器
                                SimpleCookie sim=new SimpleCookie();
                                sim.setName("USERNAME");
                                sim.setValue("");
                                sim.setMaxAge(30);
                                sim.saveTo(request,response);
                            }
                            if ("PASSWORD".equals(cookie.getName())){
                                SimpleCookie simpleCookie=new SimpleCookie();
                                simpleCookie.setName("PASSWORD");
                                simpleCookie.setValue("");
                                simpleCookie.setMaxAge(30);
                                simpleCookie.saveTo(request,response);
                            }
                        }
                    }
                }
                return "success";
            }catch (AuthenticationException e){
                //登录失败
                return "error";
            }
        }
        return null;
    }

    //cookie保存密码
    @Override
    public User queryCookle(HttpServletRequest request) {
        User user=new User();
        Cookie[] cookies = request.getCookies();
        System.out.println("开始拿cookie");
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if ("USERNAME".equals(cookie.getName())){
                    user.setUsername(cookie.getValue());
                    System.out.println("拿到cookie");
                }
                if ("PASSWORD".equals(cookie.getName())){
                    user.setPassword(cookie.getValue().replace(user.getUsername(),""));
                    System.out.println("拿到cookie");
                }
            }
            return user;
        }
        user.setUsername("");
        return user;
    }
}
