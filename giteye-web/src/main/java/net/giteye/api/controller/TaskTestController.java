package net.giteye.api.controller;

import net.giteye.api.resp.ApiResp;
import net.giteye.task.TaskMain;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/task")
public class TaskTestController {

    @RequestMapping(value = "/run_increment", method = RequestMethod.GET)
    public ApiResp<Object> runAll4Increment(){
        TaskMain taskMain = new TaskMain();
        taskMain.runAll4Increment();
        return ApiResp.success();
    }

    @RequestMapping(value = "/run", method = RequestMethod.GET)
    public ApiResp<Object> run(){
        TaskMain taskMain = new TaskMain();
        taskMain.runAll();
        return ApiResp.success();
    }

    @RequestMapping(value = "/run_increment/{id}", method = RequestMethod.GET)
    public ApiResp<Object> runOne4Increment(@PathVariable("id") Long id){
        TaskMain taskMain = new TaskMain();
        taskMain.runOne4Increment(id);
        return ApiResp.success();
    }

    @RequestMapping(value = "/run/{id}", method = RequestMethod.GET)
    public ApiResp<Object> runOne(@PathVariable("id") Long id){
        TaskMain taskMain = new TaskMain();
        taskMain.runOne(id);
        return ApiResp.success();
    }
}
