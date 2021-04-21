package com.tommy.jpabook.bootjpaapplication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        Hello hello = new Hello("hangyeol");
        model.addAttribute("data", hello.getHello());
        return "hello";
    }

}
