package com.example.test.dto.request;

import com.example.test.model.Status;
import lombok.Data;

import java.util.Date;
@Data
public class TaskForChangeRq {
    private Integer id;
    private String title;
    private String description;
    private Date time;
    private String status;
}
