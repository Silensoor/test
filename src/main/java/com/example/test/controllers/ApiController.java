package com.example.test.controllers;

import com.example.test.dto.StatusRs;
import com.example.test.dto.TaskRq;
import com.example.test.dto.TaskRs;
import com.example.test.services.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final TasksService tasksService;

    @PostMapping("/task")
    public ResponseEntity<?> putInQueue(TaskRq taskRq) {
        return ResponseEntity.ok(tasksService.addTask(taskRq));
    }

    @PostMapping("/data")
    public ResponseEntity<?> addTasksToDatabase() {
        return ResponseEntity.ok(tasksService.putTasksToDatabase());
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskRs>> getTasks() {
        return ResponseEntity.ok(tasksService.getAllTasks());
    }
    @GetMapping("/task")
    public ResponseEntity<TaskRs> getTask(Integer id){
        return ResponseEntity.ok(tasksService.getTask());
    }

}
