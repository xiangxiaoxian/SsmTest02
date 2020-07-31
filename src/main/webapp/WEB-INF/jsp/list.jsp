<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Title</title>
</head>
<body>
<a href="/pc/logout">退出登录</a><br>
<shiro:hasRole name="user">
    <a href="${pageContext.request.contextPath}/pc/user.do">用户</a><br>
</shiro:hasRole>
<shiro:hasRole name="admin">
    <a href="${pageContext.request.contextPath}/pc/admin.do">管理员</a><br>
</shiro:hasRole>
</body>
</html>
