package com.example.test.services.Impl;

import com.example.test.dto.*;
import com.example.test.model.Task;
import com.example.test.model.Worker;
import com.example.test.repository.TaskRepository;
import com.example.test.repository.WorkerRepository;
import com.example.test.services.TasksService;

import com.example.test.utils.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.example.test.utils.Mapper.taskModelToTaskResponse;
import static com.example.test.utils.Mapper.taskRequestToTaskModel;

@Service
public class TaskServiceImpl implements TasksService {

    private final TaskRepository taskRepository;
    private final Queue queue;
    private final ExecutorService executorService;
    private final WorkerRepository workerRepository;

    public TaskServiceImpl(TaskRepository taskRepository, Queue queue, WorkerRepository workerRepository) {
        this.taskRepository = taskRepository;
        this.queue = queue;
        this.workerRepository = workerRepository;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public StatusRs addTask(TaskRq taskRq) {
        return new StatusRs(queue.addTask(taskRequestToTaskModel(taskRq)));
    }

    @Override
    public StatusRs putTasksToDatabase() {
        try {
            List<Task> tasks = queue.get3Tasks();
            tasks.forEach(task -> executorService.submit(() -> taskRepository.saveTasks(task)));
            return new StatusRs(true);
        } catch (RuntimeException e) {
            return new StatusRs(false);
        }
    }

    @Override
    public List<TaskShortResponse> getAllTasks() {
        return taskRepository.findAllTasksShort().stream().map(Mapper::taskModelToTaskShortResponse).collect(Collectors.toList());
    }

    @Override
    public TaskResponse getTaskById(int id) {
        Task task = taskRepository.findById(id);
        Worker worker = workerRepository.getWorkerById(task.getPerformer().getId());
        task.setPerformer(worker);
        return taskModelToTaskResponse(task);
    }

    @Override
    public StatusRs addWorker(WorkerForTaskRq workerForTaskRq) {
        workerRepository.addWorker(workerForTaskRq.getWorkerRq());
        Worker worker = workerRepository.getWorkerForNPA(workerForTaskRq.getWorkerRq());
        return new StatusRs(taskRepository.addWorker(worker, workerForTaskRq.getIdForTask()));
    }
}
