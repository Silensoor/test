package com.example.test.dto.response;

import com.example.test.model.Status;
import com.example.test.model.Worker;
import lombok.Data;

@Data
public class TaskByWorkerRs {
    private Integer id;
    private String title;
    private Status status;
    Worker worker;
}
