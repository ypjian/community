package com.younger.community.controller;

import com.younger.community.dto.AccessTokenDto;
import com.younger.community.dto.GithubUser;
import com.younger.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.ws.Action;

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

    //通过@value读取配置文件中的值
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    //处理github redirect回到index页面并使用@RequestParam(name="code")接收返回的code
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setCode(code);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
        //获取access_token
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        //获取user
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
