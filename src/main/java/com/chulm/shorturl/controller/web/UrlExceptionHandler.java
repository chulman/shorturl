package com.chulm.shorturl.controller.web;

import com.chulm.shorturl.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class UrlExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView ResourceNotFound(HttpServletRequest req, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("error",ex.getMessage());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND);
        modelAndView.setViewName("error/4xx_error");
        return modelAndView;
    }
}
