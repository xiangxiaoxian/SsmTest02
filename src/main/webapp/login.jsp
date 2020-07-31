<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>企业资产管理系统</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="static/css/font.css">
    <link rel="stylesheet" href="static/css/xadmin.css">
    <script type="text/javascript"
            src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="static/lib/layui/layui.js"
            charset="utf-8"></script>
    <script type="text/javascript" src="static/js/xadmin.js"></script>
    <script type="text/javascript">
        //如果该页面在iframe中出现，那么会自动调用最外层窗口刷新本链接
        if (window != top) {
            top.location.href = location.href;
        }
    </script>

</head>
<body class="login-bg">

<div class="login" >
    <div class="message">xxxx管理系统-用户登录</div>
    <font  id="error" size="10" class="text-align:center" color="red"></font>
    <div id="darkbannerwrap"></div>
    <form method="post" class="layui-form">
        <input  placeholder="用户名" type="text" name="username" id="u"
                class="layui-input">
        <hr class="hr15">
        <input  placeholder="密码" type="password" name="password" id="p"
                class="layui-input">
        <hr class="hr15">
        <input lay-submit lay-filter="login" style="width: 100%;"
               type="button" value="登录" onclick="login()" />
        <hr class="hr20">
        <div class="layui-form-item"/>
            <div class="layui-form-item" pane="">

                <input type="checkbox" lay-filter="filter"  id="memoryuser" lay-skin="primary" title="30天内自动登录" >

            </div>
    </form>
</div>

<script>
    //发送登出请求
   $(function () {
       $.ajax({
           type:"post",
           url:"${pageContext.request.contextPath}/pc/logout.do",
           dataType:"json",
           success:function (info) {
               if (info.username!=""){
                   $("#u").val(info.username);
                   $("#p").val(info.password);
                   $("#memoryuser").prop("checked","checked");
               }
           }
       })
   })
    //进行登录处理
    function login() {
       var rem=$("#memoryuser").prop("checked");
       var mm="No";
       if (rem){
           mm="YES";
       }
        $.ajax({
            type:"post",
            url:"${pageContext.request.contextPath}/pc/checkLogin.ajax?mm="+mm,
            dataType:"text",
            data:$("form").serialize(),
            success:function (info) {
                if(info=="success"){
                    window.location.href ="${pageContext.request.contextPath}/pc/list.ajax"
                }else{
                    layer.msg('账号或者密码错误！', {icon: 2});
                }
            },
            error:function () {
                layer.msg('ajax请求失败', {icon: 2});
            }
        })
    }



</script>

