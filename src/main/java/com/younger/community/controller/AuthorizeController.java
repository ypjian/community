package com.younger.community.controller;

import com.younger.community.dto.AccessTokenDto;
import com.younger.community.dto.GithubUser;
import com.younger.community.mapper.UserMapper;
import com.younger.community.model.User;
import com.younger.community.provider.GithubProvider;
import com.younger.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Action;
import java.util.UUID;

/**
    1. Request a user's GitHub identity
 实现第一步，点击登录按钮，跳转至github授权验证，返回一个code

 2.     2.Users are redirected back to your site by GitHub
        携带code访问github，返回access_token

 3.https://api.github.com/user
 携带access_token访问userAPI，返回user信息
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    //通过@value读取配置文件中的值
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    //处理github redirect回到index页面并使用@RequestParam(name="code")接收返回的code
    //session是从HttpServletRequest中拿到的
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setCode(code);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
        //获取access_token
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        //获取user
        GithubUser githubUser = githubProvider.getUser(accessToken);
        //System.out.println(githubUser.getName());
        if(githubUser != null && githubUser.getId() != null) {
            //为githubuser用户设置值
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());

            //如果数据库中能够查找当前登录用户的accountid，则更新token，否则创建新的
            userService.createOrUpdate(user);

            //将信息存入数据库
//            userMapper.insert(user);

            //登录成功，写cookie和session
            //将user放入到session中，相当于在银行中创建账户了，且给前端一个默认的银行卡（cookie），但此时不能自己选银行卡号
            //request.getSession().setAttribute("user",githubUser);
            //直接return index会是在当前页面刷新，且保留返回的code信息，使用redirect重定向到另一个页面

            /*
            此处是将数据写入到数据库中了，因此不用再写到session，每次去数据库中验证用户是否存在
            当不再写入session时，需要自己写一个cookie
            使用token作为cookie
             */
            response.addCookie(new Cookie("token",token));

            //注意重定向要写路径
            return "redirect:/";
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    /*
    完成退出登录功能
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        //删除session
        request.getSession().removeAttribute("user");
        //删除cookie
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}

