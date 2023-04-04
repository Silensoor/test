package com.example.test.dto;

import lombok.Data;

@Data
public class WorkerForTaskRq {
    WorkerRq workerRq;
    Integer idForTask;
}
