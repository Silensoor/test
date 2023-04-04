package com.example.test.services;

import com.example.test.dto.*;
import liquibase.pro.packaged.S;

import java.util.List;

public interface TasksService {
    StatusRs addTask(TaskRq taskRq);
    StatusRs putTasksToDatabase();
    List<TaskShortResponse> getAllTasks();
    TaskResponse getTaskById(int id);
    StatusRs addWorker(WorkerForTaskRq workerForTaskRq);
}
