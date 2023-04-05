package com.example.test.utils;

import com.example.test.dto.request.TaskForChangeRq;
import com.example.test.dto.request.WorkerRq;
import com.example.test.dto.response.TaskRs;
import com.example.test.dto.request.TaskRq;
import com.example.test.dto.response.TaskShortRs;
import com.example.test.model.Status;
import com.example.test.model.Task;
import com.example.test.model.Worker;


import java.util.Date;

public class Mapper {


    public static Task taskRequestToTaskModel(TaskRq taskRq) {
        Task task = new Task();
        task.setTime(new Date());
        task.setStatus(Status.NotTakenOn);
        task.setTitle(taskRq.getTitle());
        task.setDescription(taskRq.getDescription());
        return task;
    }

    public static TaskShortRs taskModelToTaskShortResponse(Task task) {
        TaskShortRs taskShortRs = new TaskShortRs();
        taskShortRs.setId(task.getId());
        taskShortRs.setStatus(task.getStatus());
        taskShortRs.setTitle(task.getTitle());
        return taskShortRs;
    }

    public static TaskRs taskModelToTaskResponse(Task task) {
        TaskRs taskRs = new TaskRs();
        taskRs.setId(task.getId());
        taskRs.setTitle(task.getTitle());
        taskRs.setDescription(task.getDescription());
        taskRs.setStatus(task.getStatus());
        taskRs.setPerformer(task.getPerformer());
        return taskRs;
    }
    public static Task taskRequestToTaskModelChange(TaskForChangeRq taskForChangeRq){
        Task task = new Task();
        task.setId(taskForChangeRq.getId());
        Status status = Status.valueOf(taskForChangeRq.getStatus());
        task.setStatus(status);
        task.setTime(taskForChangeRq.getTime());
        task.setTitle(taskForChangeRq.getTitle());
        task.setDescription(taskForChangeRq.getDescription());
        return task;
    }
    public static WorkerRq workerToWorkerRequest(Worker worker){
        WorkerRq workerRq = new WorkerRq();
        workerRq.setPosition(worker.getPosition());
        workerRq.setName(worker.getName());
        workerRq.setAvatar(worker.getAvatar());
        return workerRq;
    }
}
