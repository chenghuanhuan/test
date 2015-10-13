package com.wanghua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenghuanhuan on 15/10/13.
 */
@Controller
@RequestMapping(value = "/first")
public class FirstController {

    @RequestMapping(value = "test")
    public String first(HttpServletRequest request){

        System.out.println("-------------test-------------------");
        return "redirect:/common/404.html";
    }
}
