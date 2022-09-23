package com.example.flowable.controller;

import com.example.flowable.entity.CurrentTask;
import com.example.flowable.entity.StartProcess;
import com.example.flowable.service.MyService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/flow")
public class FlowController {

    @Autowired
    private MyService myService;

    /**
     * 启动一个流程
     * @param startProcess
     * @return
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public String startLeaveProcess(@RequestBody StartProcess startProcess) {
        return myService.startProcess(startProcess);
    }

    /**
     * 查看关联所有任务
     * @param assignee
     * @return
     */
    @GetMapping(value = "/tasks")
    public List<CurrentTask> getTasks(@RequestParam String assignee) {
        return myService.getTasks(assignee);
    }

    /**
     * 拒绝
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/reject", method = RequestMethod.PUT)
    public String rejectTask(@RequestParam String taskId) {
        return myService.audit(taskId,false);
    }

    /**
     * 通过
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/pass", method = RequestMethod.PUT)
    public String passTask(@RequestParam String taskId) {
        return myService.audit(taskId,true);
    }

    /**
     * 查看历史任务
     * @param assignee
     * @return
     */
    @GetMapping(value = "/historyTasks")
    public List<CurrentTask> historyTasks(@RequestParam String assignee) {
        return myService.getHistoryTasks(assignee);
    }

}