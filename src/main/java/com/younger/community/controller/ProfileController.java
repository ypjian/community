package com.younger.community.controller;

import com.younger.community.dto.PageDto;
import com.younger.community.mapper.UserMapper;
import com.younger.community.model.User;
import com.younger.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 在登录用户下拉栏会有我的问题，最新回复等下拉栏
 * 当用户点击时，会发出对应的action请求
 * <a href="/profile/questions" th:class="${section == 'questions'}? 'active list-group-item' : 'list-group-item'">我的问题</a>
 * 此profileController拦截请求，进行处理
 * 去数据库中找对应用户的问题信息
 * 再展示到页面
 */
@Controller
public class ProfileController {

//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private QuestionService questionSevice;

    /*
    例如点击我的问题时，此时请求发出的action就是我的问题
     */
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size) {

//        User user = null;
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null && cookies.length != 0) {
//            for(Cookie cookie : cookies) {
//                if(cookie.getName().equals("token")) {
//                    String token = cookie.getValue();
//                    //根据cookie去数据库中查找是否有用户存在
//                    user = userMapper.findByToken(token);
//                    if(user != null) {
//                        //如果存在用户，将用户信息放到session中，用于前端展示
//                        request.getSession().setAttribute("user",user);
//                    }
//                    break;
//                }
//            }
//        }

        User user = (User) request.getSession().getAttribute("user");
        if(user == null) {
            return "redirect:/";
        }

        //如果前端点击我的问题按钮
        if("questions".equals(action)) {
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
        }
        //前端点击最新回复
        else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        //根据用户查到该用户专属的信息
        PageDto pagedto = questionSevice.list(user.getId(), page, size);
        model.addAttribute("pagedto",pagedto);
        return "profile";
    }
}
