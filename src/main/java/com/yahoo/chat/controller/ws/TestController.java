package com.yahoo.chat.controller.ws;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/testhello")
    public String testhelo(){
        return "hello";
    }
}
