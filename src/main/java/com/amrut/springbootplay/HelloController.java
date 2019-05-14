package com.amrut.springbootplay;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @RequestMapping("/")
    public String index() {
        return "Application that alerts with push notifications to sunscribed users about rain forecast for the day.";
    }
    
}
