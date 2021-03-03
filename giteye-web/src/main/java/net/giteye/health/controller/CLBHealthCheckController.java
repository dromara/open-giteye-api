package net.giteye.health.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/health")
public class CLBHealthCheckController {

    @ResponseBody
    @RequestMapping(value = "/clb/check", method = RequestMethod.GET)
    public String clbCheck(){
        return "ok";
    }

}
