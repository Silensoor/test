package com.example.test.services.Impl;

import com.example.test.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class Queue {
    private final ConcurrentLinkedQueue<Task> queue;

    public Queue() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public Task getTask() {
        return queue.poll();
    }

    public boolean addTask(Task task) {
        return queue.add(task);
    }

    public int getQueueSize() {
        return queue.size();
    }

    public List<Task> get3Tasks() {
        synchronized (queue) {
            if (getQueueSize() <= 3) {
                throw new RuntimeException();
            }
            return List.of(getTask(), getTask(), getTask());
        }
    }
}
