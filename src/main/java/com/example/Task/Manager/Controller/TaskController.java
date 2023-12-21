package com.example.Task.Manager.Controller;

import com.example.Task.Manager.DTO.ApiResponse;
import com.example.Task.Manager.Models.Task;
import com.example.Task.Manager.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getTask(){
        try {
            List<Task> tasks = taskService.getAllTask();
            return ResponseEntity.ok(new ApiResponse<>(200,"Tasks Retrived",tasks));
        }catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> getTaskByID(@PathVariable Long id){
        try {
            Optional<Task> tasks = taskService.findTaskByID(id);
            if (tasks.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(200, "Task retrieved successfully", tasks.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(404, "Task not found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500,"Internal Server Error",null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<Task>>> addTask(@RequestBody Task task){
        return taskService.addTask(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<List<Task>>> updateTask(@PathVariable Long id,@RequestBody Task task){
        return taskService.updateTask(task,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<List<Void>>> deleteTask(@PathVariable Long id){
        return taskService.deleteTask(id);
    }
}
