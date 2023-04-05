package com.example.test.dto.request;

import lombok.Data;


import java.io.Serializable;

@Data
public class TaskRq implements Serializable {
    private String title;
    private String description;
}


