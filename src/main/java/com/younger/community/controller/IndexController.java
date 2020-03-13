package com.younger.community.controller;

import com.younger.community.dto.PageDto;
import com.younger.community.dto.QuestionDto;
import com.younger.community.mapper.QuestionMapper;
import com.younger.community.mapper.UserMapper;
import com.younger.community.model.Question;
import com.younger.community.model.User;
import com.younger.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class IndexController {

//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private QuestionService questionSevice;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size
    ) {

        /*
        此段用来，当点击登录从github获得用户后，验证用户是否存在于数据库中，并在前端展示用户信息
        （将登录改成我）
        场景：每次有人登录成功，就会在数据库中注册信息
        但是，当数据库重启后，如果每次都需要用户重新登录，会很麻烦
        因此在登录之前，会验证是否存在于库中，如果存在，不需要再重新登录
        页面发出请求，遍历页面的cookie，如果存在token，验证是否在库中，如果存在，直接显示我
         */
        //response是设置cookie，返回给浏览器
        //request是请求，获取cookie
//
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null && cookies.length != 0) {
//            for(Cookie cookie : cookies) {
//                if(cookie.getName().equals("token")) {
//                    String token = cookie.getValue();
//                    //根据cookie去数据库中查找是否有用户存在
//                    User user = userMapper.findByToken(token);
//                    if(user != null) {
//                        //如果存在用户，将用户信息放到session中，用于前端展示
//                        request.getSession().setAttribute("user",user);
//                    }
//                    break;
//                }
//            }
//        }

        /*
        当用户发布问题后，会跳转到index页面，将question内容展示，包括回复量，图像等等信息
        由于图像在user表，所以需要根据question的creator关联到user的id，拿到avatarUrl
        建立一个questionDto，用于向question传递user信息
        questionService同时组装了questinmapper和usermapper
        因此可以完整显示用户发布的问题及图像信息
         */
        PageDto pagedto = questionSevice.list(page,size);

        model.addAttribute("pagedto",pagedto);
        return "index";
    }
}
