package com.example.act.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class BDispactherController {

    @RequestMapping("business/admanage")
    public String admanage() {
        return "advertisement";
    }

}
