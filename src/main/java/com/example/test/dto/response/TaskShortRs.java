package com.example.test.dto.response;

import com.example.test.model.Status;
import lombok.Data;

@Data
public class TaskShortRs {
    private Integer id;
    private String title;
    private Status status;
}
