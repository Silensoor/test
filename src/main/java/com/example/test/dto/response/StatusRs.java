package com.example.test.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusRs {

    private boolean result;
    private String error;

}
