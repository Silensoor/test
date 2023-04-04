package com.example.test.controllers;

import com.example.test.dto.*;
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
    public List<TaskShortResponse> getTasks() {
        return tasksService.getAllTasks();
    }
    @GetMapping("/task")
    public TaskResponse getTaskForId(@RequestParam("id") Integer id){
        return tasksService.getTaskById(id);
    }
    @PostMapping("/worker")
    public StatusRs addWorkerForTask(@RequestBody WorkerForTaskRq workerForTaskRq){
            return tasksService.addWorker(workerForTaskRq);
    }


}
