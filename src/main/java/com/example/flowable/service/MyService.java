package com.example.flowable.service;

import com.example.flowable.entity.CurrentTask;
import com.example.flowable.entity.StartProcess;
import com.example.flowable.entity.SysGroup;
import com.example.flowable.entity.SysUser;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.GroupQuery;
import org.flowable.idm.api.User;
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
    private IdentityService identityService;

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

    public void saveUser(SysUser user) {
        identityService.saveUser(user);
    }

    public void saveGroup(SysGroup group) {
        identityService.saveGroup(group);
    }

    public void createMembership(String userId,String groupId) {
        identityService.createMembership(userId,groupId);
    }

    //切换执行人
    public void claim(String assignee) {
        //获取对应的组成员
        User user = identityService.createUserQuery().memberOfGroup("XXX").userDisplayName("xxx").singleResult();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        tasks.forEach(item -> {
            taskService.claim(item.getId(),user.getId());
        });

    }

    //置空执行人
    public void unclaim(String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        tasks.forEach(item -> {
            taskService.unclaim(item.getId());
        });

    }

}