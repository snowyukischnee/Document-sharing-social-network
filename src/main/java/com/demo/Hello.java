package com.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("")
@Controller
public class Hello {

    @RequestMapping(method = RequestMethod.GET)
    public String heello(ModelMap model) {
        model.addAttribute("message", "heeeeelllllo");
        return "hello";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
        return "login";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }


    public String upload(@RequestParam("file") MultipartFile file, ModelMap model) {
        model.addAttribute("file", file);
        return "redirect:index";
    }
}
