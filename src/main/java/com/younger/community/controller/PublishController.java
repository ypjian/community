package com.younger.community.controller;

import com.younger.community.mapper.QuestionMapper;
import com.younger.community.mapper.UserMapper;
import com.younger.community.model.Question;
import com.younger.community.model.User;
import com.younger.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/*
发布问题提交到服务器端，进行验证，如果用户没有登录，返回发布页面，提示错误信息
如果成功发布，跳转到首页
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }


    /*
    实现编辑更新功能
    由于存在编辑后重复的问题
    所以需要creatOrpdate,
    <input type="hidden" name="id" th:value="${id}">
    根据隐藏标签question的id进行判断,如果id有值，更新，否则新建
     */
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id")Integer id,
                       Model model) {
        //根据id获取question,然后回显到publish页面,然后编辑内容进行发布
        Question question = questionMapper.getById(id);

        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tags",question.getTags());

        model.addAttribute("id",question.getId());

        return "publish";
    }



    /*
    用户在表单填写信息，点击发布后，如果没有登录，报错来到发布页面显示没有登录
    如果已经登录，来到服务器端，携带填写的信息，验证是否正确
    服务器想向浏览器传递信息时，需要将要输入的东西写到model，然后在html页面编写，使用theleaf语法，显示信息
     */
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title",required=false)String title,
                            @RequestParam(value = "description",required=false)String description,
                            @RequestParam(value = "tags",required = false)String tags,
                            HttpServletRequest request,
                            Model model,
                            @RequestParam(value = "id",required=false)Integer id) {

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tags",tags);

        if(title == null || title == "") {
            model.addAttribute("error","标题不能为空");
            return "publish";
        }

        if(description == null || description == "") {
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }

        if(tags == null || tags == "") {
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

//        User user = null;
//
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
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTags(tags);
        question.setCreator(user.getId());


        question.setId(id);
        questionService.creatOrUpdate(question);
        return "redirect:/";
    }
}
