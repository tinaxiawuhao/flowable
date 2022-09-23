package com.example.flowable.service;

import com.example.flowable.entity.CurrentTask;
import com.example.flowable.entity.StartProcess;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MyService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Transactional
    public String startProcess(StartProcess startProcess) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(startProcess.getProcessDefinitionKey(), startProcess.getVariables());
        return processInstance.getId();
    }

    @Transactional
    public List<CurrentTask> getTasks(String assignee) {
        List<CurrentTask> list = new ArrayList<>();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        tasks.forEach(item -> {
            list.add(CurrentTask.builder().id(item.getId()).name(item.getName()).assignee(item.getAssignee()).build());
        });
        return list;
    }

    @Transactional
    public String audit(String taskId, boolean flag) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("checkResult", flag);
        taskService.complete(taskId, map);
        if (flag) {
            return "申请审核通过";
        } else {
            return "申请审核驳回";
        }
    }

    public List<CurrentTask> getHistoryTasks(String assignee) {
        List<CurrentTask> result = new ArrayList<>();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        list.forEach(item -> {
            result.add(CurrentTask.builder().id(item.getId()).name(item.getName()).assignee(item.getAssignee()).build());
        });
        return result;
    }
}