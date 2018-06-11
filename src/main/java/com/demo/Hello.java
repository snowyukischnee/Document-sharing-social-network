package com.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("")
@Controller
public class Hello {

    @RequestMapping(method = RequestMethod.GET)
    public String heello(ModelMap model) {
        model.addAttribute("message", "heeeeelllllo");
        return "hello";
    }
}
