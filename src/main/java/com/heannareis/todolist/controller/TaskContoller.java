package com.heannareis.todolist.controller;

import com.heannareis.todolist.domain.model.TaskModel;
import com.heannareis.todolist.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskContoller {
    @Autowired
    private ITaskRepository taskRepository;
    @PostMapping("/")
    public TaskModel created(@RequestBody TaskModel taskModel){
        System.out.println("Chegou na Controller");
        var task = this.taskRepository.save(taskModel);
        return task;
    }
}
