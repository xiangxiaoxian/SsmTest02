package com.hqyj.realm;

import com.hqyj.dao.PermissionsMapper;
import com.hqyj.dao.UserMapper;
import com.hqyj.pojo.Permissions;
import com.hqyj.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MyRealm.java
 * @Description TODO
 * @createTime 2020年07月28日 16:12:00
 */
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper um;
    @Autowired
    private PermissionsMapper pm;
    //用户授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("进入授权方法");
        //通过principal获取用户信息
        String principal=(String) principals.getPrimaryPrincipal();//拿到用户名
        //给用户添加角色
        Set<String> roles=new HashSet<>();
        //查询数据库拿到权限
        List<Permissions> permissionsList=pm.queryPermissionsByPersonName(principal);
        for (Permissions permissions : permissionsList) {
            roles.add(permissions.getPermissionsName());
        }
     /*   roles.add("user");
        if ("admin".equals(principal) || "xxx".equals(principal)){
            roles.add("admin");
        }*/
        return new SimpleAuthorizationInfo(roles);
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("------我进来了----");
        //将token强转为UsernamePasswordToken 类型（可以通过这个类型拿到身份(用户名)）
        UsernamePasswordToken token1 = (UsernamePasswordToken)token;
        User u = um.queryUserByUserName(token1.getUsername());
        System.out.println("u = " + u);
        if (u != null) {
            //设置比对器里面的身份
            Object principal = u.getUsername();
            //设置比对器里面的的密码(把数据库里面的密码作为比对密码)
            Object credentials = u.getPassword();
            //自动给令牌类里的密码设置加盐方式
            ByteSource salt = ByteSource.Util.bytes(u.getUsername());
            //设置realm的名称
            String realmName = this.getName();
            //new SimpleAuthenticationInfo(principal,credentials,salt,realmName)才是真正的认证
            /*
           如何认证：
           将上面的principal和token1里面的用户名来比对
           将上面的credentials和token1里面的密码来比对
           因为spring-shiro.xml里面配置加密方式 所以会自动把token1里面的密码以MD5加密，加密1024次
          因为上面传了个salt，表示再把token1里面的密码再以salt加盐
           * */
            return new SimpleAuthenticationInfo(principal, credentials, salt, realmName);
        } else {
            throw new AuthenticationException();
        }

    }

    public static void main(String[] args) {
        //加密测试代码
        //设置加密方式
        String algorithmName = "MD5";
        //设置待加密的原密码
        Object source = "123";
        //设置加盐方式(一般来说都是以用户名来加盐)
        Object salt = ByteSource.Util.bytes("admin");
        //加密次数
        int hashIterations = 1024;
        SimpleHash newPassword = new SimpleHash(algorithmName, source, salt, hashIterations);
        System.out.println(newPassword.toString());
    }
}
