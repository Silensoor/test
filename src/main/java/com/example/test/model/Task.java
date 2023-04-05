package com.example.test.model;

import lombok.Data;

import java.util.Date;

@Data
public class Task {
    private Integer id;
    private String title;
    private String description;
    private Date time;
    private Status status;
    private Worker performer;

}
