package com.younger.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

    //model将请求参数里的数据放到域中
    //@RequestParam获取请求参数
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
