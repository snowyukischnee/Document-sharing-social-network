package com.swd.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@Controller
public class TokenController {
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,String> getCsrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken)request.getAttribute("_csrf");
        return Collections.singletonMap("token", token.getToken());
    }
}
