package com.example.test.utils;

import com.example.test.dto.TaskResponse;
import com.example.test.dto.TaskRq;
import com.example.test.dto.TaskShortResponse;
import com.example.test.model.Status;
import com.example.test.model.Task;



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

    public static TaskShortResponse taskModelToTaskShortResponse(Task task) {
        TaskShortResponse taskShortResponse = new TaskShortResponse();
        taskShortResponse.setId(task.getId());
        taskShortResponse.setStatus(task.getStatus());
        taskShortResponse.setTitle(task.getTitle());
        return taskShortResponse;
    }

    public static TaskResponse taskModelToTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setPerformer(task.getPerformer());
        return taskResponse;
    }
}
