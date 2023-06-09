package com.example.test.dto.response;

import com.example.test.model.Status;
import com.example.test.model.Worker;
import lombok.Data;

import java.util.Date;

@Data
public class TaskRs {
    private Integer id;
    private String title;
    private String description;
    private Date time;
    private Status status;
    private Worker performer;
}
