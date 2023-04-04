package com.example.test.dto;

import com.example.test.model.Status;
import lombok.Data;

@Data
public class TaskShortResponse {
    private Integer id;
    private String title;
    private Status status;
}
