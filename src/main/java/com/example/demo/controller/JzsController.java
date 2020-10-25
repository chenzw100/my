package com.example.demo.controller;

import com.example.demo.service.Gk27Service;
import com.example.demo.service.KpwHrService;
import com.example.demo.service.KpwService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JzsController {
    public Log log = LogFactory.getLog(JzsController.class);
    @Autowired
    KpwHrService kpwHrService;
    @Autowired
    KpwService kpwService;
    @Autowired
    Gk27Service gk27Service;
    @RequestMapping("/kpwhr/{code}")
    public String kpw(@PathVariable("code")String code) {
        kpwHrService.testId(code);
        return "add success";
    }
    @RequestMapping("/one")
    public String kpw() {
        kpwHrService.infoPages(1);
        return "one success";
    }
    @RequestMapping("/kpwhrs/{page}")
    public String kpw(@PathVariable("page")Integer page) {
        for(int i=page;i<page+10;i++){
            log.info("--------------------------------------------------------------------------------------------page = [" + i + "]");
            kpwHrService.infoPages(i);
            log.info("-----------------------------------------------------------------------------------------------完成page = [" + i + "]");
        }
        return "add success";
    }
    @RequestMapping("/kpwhr2/{page}/{count}")
    public String kpw(@PathVariable("page")Integer page,@PathVariable("page")Integer count) {
        for(int i=page;i<count;i++){
            log.info("page = [" + i + "]");
            gk27Service.infoPages(i);
            log.info("-----------------------完成page = [" + i + "]");
        }
        return "add success";
    }

}
