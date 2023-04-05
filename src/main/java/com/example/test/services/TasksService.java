package com.example.test.services;

import com.example.test.dto.request.*;
import com.example.test.dto.response.StatusRs;
import com.example.test.dto.response.TaskByWorkerRs;
import com.example.test.dto.response.TaskRs;
import com.example.test.dto.response.TaskShortRs;
import com.example.test.model.Worker;

import java.util.List;

public interface TasksService {
    StatusRs addTask(TaskRq taskRq);
    StatusRs putTasksToDatabase();
    List<TaskShortRs> getAllTasks();
    TaskRs getTaskById(int id);
    StatusRs addWorker(WorkerForTaskRq workerForTaskRq);
    StatusRs changeTask(TaskForChangeRq task);
    TaskByWorkerRs getTaskByWorkerId(Integer id);
    WorkerRq getWorkerById(Integer id);
    StatusRs deleteWorkerById(Integer id);
    StatusRs changeWorkerById(WorkerForChangeRq worker);
}
