package com.example.test.services.Impl;

import com.example.test.dto.request.*;
import com.example.test.dto.response.StatusRs;
import com.example.test.dto.response.TaskByWorkerRs;
import com.example.test.dto.response.TaskRs;
import com.example.test.dto.response.TaskShortRs;
import com.example.test.model.Task;
import com.example.test.model.Worker;
import com.example.test.repository.TaskRepository;
import com.example.test.repository.WorkerRepository;
import com.example.test.services.TasksService;

import com.example.test.utils.Mapper;
import com.example.test.utils.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.example.test.utils.Mapper.*;

@Service
@Slf4j
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
        boolean b = queue.addTask(taskRequestToTaskModel(taskRq));
        if (b) {
            log.info("Задача добавлена в очередь");
            return new StatusRs(true, null);
        } else {
            log.debug("Задача не была добавлена в очередь");
            return new StatusRs(false, "Задача не была добавлена в очередь");
        }

    }

    @Override
    public StatusRs putTasksToDatabase() {
        try {
            List<Task> tasks = queue.get3Tasks();
            tasks.forEach(task -> executorService.submit(() -> taskRepository.saveTasks(task)));
            log.info("Данные добавлены в базу данных");
            return new StatusRs(true, null);
        } catch (RuntimeException e) {
            log.debug("Данные не были добавлены в базу данных");
            return new StatusRs(false, "Данные не были добавлены в базу данных");
        }
    }

    @Override
    public List<TaskShortRs> getAllTasks() {
        return taskRepository.findAllTasksShort().stream().map(Mapper::taskModelToTaskShortResponse).collect(Collectors.toList());
    }

    @Override
    public TaskRs getTaskById(int id) {
        Task task = taskRepository.findById(id);
        return taskModelToTaskResponse(task);
    }

    @Override
    public StatusRs addWorker(WorkerForTaskRq workerForTaskRq) {
        if (workerForTaskRq.getWorkerRq().getName() == null || workerForTaskRq.getWorkerRq().getPosition() == null ||
                workerForTaskRq.getWorkerRq().getAvatar() == null) {
            log.debug("Ошибка ввода данных");
            return new StatusRs(false, "Ошибка ввода данных");
        } else {
            log.info("Работник присоединен");
            int i = workerRepository.addWorker(workerForTaskRq.getWorkerRq());
            if(i!=1){
                log.debug("Работник не был добавлен");
                return new StatusRs(false,"Работник не был добавлен");
            }
            Worker worker = workerRepository.getWorkerForNPA(workerForTaskRq.getWorkerRq());
            return new StatusRs(taskRepository.addWorker(worker, workerForTaskRq.getIdForTask()), null);
        }
    }

    @Override
    public StatusRs changeTask(TaskForChangeRq taskForChangeRq) {
        Task task = taskRequestToTaskModelChange(taskForChangeRq);
        boolean b = taskRepository.saveTask(task);
        if(b){
            log.info("Данные успешно обновлены");
            return new StatusRs(true,null);
        }else {
            log.debug("Данные задачи не были изменены");
            return new StatusRs(false,"Данные задачи не были изменены");
        }
    }

    @Override
    public TaskByWorkerRs getTaskByWorkerId(Integer id) {
        return taskRepository.findTaskByWorker(id);
    }

    @Override
    public WorkerRq getWorkerById(Integer id) {
        Worker workerById = workerRepository.getWorkerById(id);
        return workerToWorkerRequest(workerById);
    }

    @Override
    public StatusRs deleteWorkerById(Integer id) {
        int i = workerRepository.removeWorkerById(id);
        if(i==1){
            log.info("Работник удален");
            return new StatusRs(true,null);
        }else {
            log.debug("Работник не был удален");
            return new StatusRs(false,"Работник не был удален");
        }
    }

    @Override
    public StatusRs changeWorkerById(WorkerForChangeRq worker) {
        int i = workerRepository.changeWorker(worker);
        if(i==1){
            log.info("Данные работника изменены");
            return new StatusRs(true,null);
        }else{
            log.debug("Данные работника не были изменены");
            return new StatusRs(false,"Данные работника не были изменены");
        }
    }
}
