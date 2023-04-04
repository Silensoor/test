package com.example.test.services.Impl;

import com.example.test.dto.StatusRs;
import com.example.test.dto.TaskRq;
import com.example.test.dto.TaskRs;
import com.example.test.services.TasksService;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TaskServiceImpl implements TasksService {
    @Override
    public StatusRs addTask(TaskRq taskRq) {
        return new StatusRs(true);
    }

    @Override
    public StatusRs putTasksToDatabase() {
        return new StatusRs(true);
    }

    @Override
    public List<TaskRs> getAllTasks() {
        return  null;
    }

    @Override
    public TaskRs getTask() {
        return null;
    }
}
