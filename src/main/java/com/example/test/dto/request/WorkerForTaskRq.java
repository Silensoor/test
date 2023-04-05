package com.example.test.dto.request;

import lombok.Data;

@Data
public class WorkerForTaskRq {
    WorkerRq workerRq;
    Integer idForTask;
}
