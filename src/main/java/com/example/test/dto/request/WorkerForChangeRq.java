package com.example.test.dto.request;

import lombok.Data;

@Data
public class WorkerForChangeRq {
    private Integer id;
    private String name;
    private String position;
    private String avatar;
}
