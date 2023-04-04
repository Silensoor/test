package com.example.test.services;

import com.example.test.dto.StatusRs;
import com.example.test.dto.TaskRq;
import com.example.test.dto.TaskRs;

import java.util.List;

public interface TasksService {
    StatusRs addTask(TaskRq taskRq);
    StatusRs putTasksToDatabase();
    List<TaskRs> getAllTasks();
    TaskRs getTask();
}
