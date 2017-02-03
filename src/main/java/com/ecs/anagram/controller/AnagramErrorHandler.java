package com.ecs.anagram.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnagramErrorHandler implements ErrorController{
	
	private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "404 Not Found";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
