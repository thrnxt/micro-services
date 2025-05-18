package org.example.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/hey")
    public String haveYouAccess(){
        return "you have access";
    }
    @PostMapping("test1")
    public String asdas(){
        return "test1";
    }
}
