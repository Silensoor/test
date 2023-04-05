package com.example.test.controllers;

import com.example.test.dto.request.*;
import com.example.test.dto.response.StatusRs;
import com.example.test.dto.response.TaskByWorkerRs;
import com.example.test.dto.response.TaskRs;
import com.example.test.dto.response.TaskShortRs;
import com.example.test.services.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TasksService tasksService;


    @PostMapping("/task")
    public StatusRs addInQueue(@RequestBody TaskRq taskRq) {
        return tasksService.addTask(taskRq);
    }

    @PostMapping("/data")
    public StatusRs addTasksToDatabase() {
        return tasksService.putTasksToDatabase();
    }

    @GetMapping("/all")
    public List<TaskShortRs> getTasks() {
        return tasksService.getAllTasks();
    }
    @GetMapping("/task")
    public TaskRs getTaskById(@RequestParam("id") Integer id){
        return tasksService.getTaskById(id);
    }
    @PostMapping("/task_worker")
    public StatusRs addWorkerByTask(@RequestBody WorkerForTaskRq workerForTaskRq){
            return tasksService.addWorker(workerForTaskRq);
    }
    @PutMapping("/task")
    public StatusRs putTask(@RequestBody TaskForChangeRq task){
            return tasksService.changeTask(task);
    }
    @GetMapping("/task_worker")
    public TaskByWorkerRs getTaskByWorkerId(@RequestParam("id")Integer id){
        return tasksService.getTaskByWorkerId(id);
    }
    @GetMapping("/worker")
    public WorkerRq getWorkerById(@RequestParam("id")Integer id){
            return tasksService.getWorkerById(id);
    }
    @DeleteMapping("/worker")
    public StatusRs removeWorkerById(@RequestParam("id") Integer id){
            return tasksService.deleteWorkerById(id);
    }
    @PutMapping("/worker")
    public StatusRs putWorker(@RequestBody WorkerForChangeRq worker){
        return tasksService.changeWorkerById(worker);
    }
}
