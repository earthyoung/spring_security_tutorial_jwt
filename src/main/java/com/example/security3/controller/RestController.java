package com.example.security3.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/")
    @ResponseBody
    public String hello(){
        return "hi";
    }

}
