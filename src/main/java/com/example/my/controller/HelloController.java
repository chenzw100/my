package com.example.my.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by laikui on 2019/1/31.
 */
@RestController
public class HelloController {
    @RequestMapping("/t/{end}")
    String t(@PathVariable("end")String end){
        System.out.println(end);
        return "success12<br>"+end;
    }
}
