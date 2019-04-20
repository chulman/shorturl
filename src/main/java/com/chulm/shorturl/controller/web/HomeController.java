package com.chulm.shorturl.controller.web;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(ModelAndView modelAndView, HttpServletResponse resp) {
        if(!(resp.getHeaders(HttpHeaders.LOCATION) != null) && resp.getStatus() == 301 ){
            modelAndView.addObject("link",resp.getHeaders(HttpHeaders.LOCATION));
        }
        return "index";
    }
}