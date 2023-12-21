package com.example.Task.Manager.Service;

import com.example.Task.Manager.DTO.ApiResponse;
import com.example.Task.Manager.Models.Task;
import com.example.Task.Manager.Repository.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }
    public List<Task> getAllTask(){
        return taskRepo.findAll();
    }

    public Optional<Task> findTaskByID(Long id){
        return taskRepo.findById(id);
    }

    public ResponseEntity<ApiResponse<List<Task>>> addTask(Task task){
        try{
            if(taskDuplicate(task.getTitle())){
                return ResponseEntity.badRequest().body(new ApiResponse<>(400,"Title Already Assigned",new ArrayList<>()));
            }
            //save Task
            taskRepo.save(task);
            //Response
            return ResponseEntity.ok(new ApiResponse<>(200,"Task Added Successfully",new ArrayList<>()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }

    public ResponseEntity<ApiResponse<List<Task>>> updateTask(Task updatedtask , Long id){
        try {//find if task already exists by ID
            Optional<Task> existingTask = taskRepo.findById(id);
            if (existingTask.isPresent()){
                Task presentTask = existingTask.get();
                //Update new Task
                presentTask.setTitle(updatedtask.getTitle());
                presentTask.setDescription(updatedtask.getDescription());
                presentTask.setStatus(updatedtask.getStatus());
                //save updated Task
                taskRepo.save(presentTask);

                return ResponseEntity.ok(new ApiResponse<>(200,"Task Updated Successfully",new ArrayList<>()));
            }else{
                return ResponseEntity.status(404).body(new ApiResponse<>(404,"Task Not Found",new ArrayList<>()));
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }

    public ResponseEntity<ApiResponse<List<Void>>> deleteTask(Long id){
        try{
            Optional<Task> existingTaskOptional = taskRepo.findById(id);
            if (existingTaskOptional.isPresent()){
                Task task = existingTaskOptional.get();
                taskRepo.deleteById(id);
                return ResponseEntity.ok(new ApiResponse<>(200,"Task Deleted",new ArrayList<>()));
            }
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Task Not Found", new ArrayList<>()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }
    private boolean taskDuplicate (String Title){
        return taskRepo.existsByTitle(Title);
    }
}
