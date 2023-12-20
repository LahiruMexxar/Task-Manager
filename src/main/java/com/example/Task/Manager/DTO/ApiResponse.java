package com.example.Task.Manager.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse <T>{
    private int code;
    private String message;
    private T payload;
}
