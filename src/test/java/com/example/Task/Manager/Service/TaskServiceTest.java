package com.example.Task.Manager.Service;

import com.example.Task.Manager.DTO.ApiResponse;
import com.example.Task.Manager.Models.Task;
import com.example.Task.Manager.Repository.TaskRepo;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceTest {

   private TaskRepo taskRepo;
   private TaskService taskService;


   @BeforeEach
   public void setUp(){
       taskRepo = mock(TaskRepo.class);
       taskService = new TaskService(taskRepo);
    }

    @Test
    public void testGetAllTasks() {
        // Mock data
        List<Task> tasks = new ArrayList<>();
        when(taskRepo.findAll()).thenReturn(tasks);

        // Call the service method
        List<Task> result = taskService.getAllTask();

        // Assertions
        assertEquals(tasks, result);
    }

    @Test
    public void testFindTaskById_ExistingId() {
        // Mock data
        Long taskId = 1L;
        Task task = new Task();
        when(taskRepo.findById(taskId)).thenReturn(Optional.of(task));

        // Call the service method
        Optional<Task> result = taskService.findTaskByID(taskId);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

    @Test
    public void testFindTaskById_NonExistingId() {
        // Mock data
        Long taskId = 2L;
        when(taskRepo.findById(taskId)).thenReturn(Optional.empty());

        // Call the service method
        Optional<Task> result = taskService.findTaskByID(taskId);

        // Assertions
        assertFalse(result.isPresent());
    }

    @Test
    public void testAddTask_Successful() {
        // Mock data
        Task task = new Task();
        when(taskRepo.existsByTitle(anyString())).thenReturn(false);

        // Call the service method
        ResponseEntity<ApiResponse<List<Task>>> result = taskService.addTask(task);

        // Assertions
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Task Added Successfully", result.getBody().getMessage());
    }

    @Test
    public void testAddTask_DuplicateTitle() {
        // Mock data
        Task task = new Task();
        when(taskRepo.existsByTitle(anyString())).thenReturn(true);

        // Call the service method
        ResponseEntity<ApiResponse<List<Task>>> result = taskService.addTask(task);

        // Assertions
        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Title Already Assigned", result.getBody().getMessage());
    }

    @Test
    public void testUpdateTask_ExistingId() {
        // Mock data
        Long taskId = 1L;
        Task updatedTask = new Task();
        when(taskRepo.findById(taskId)).thenReturn(Optional.of(new Task()));

        // Call the service method
        ResponseEntity<ApiResponse<List<Task>>> result = taskService.updateTask(updatedTask, taskId);

        // Assertions
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Task Updated Successfully", result.getBody().getMessage());
    }

    @Test
    public void testUpdateTask_NonExistingId() {
        // Mock data
        Long taskId = 2L;
        Task updatedTask = new Task();
        when(taskRepo.findById(taskId)).thenReturn(Optional.empty());

        // Call the service method
        ResponseEntity<ApiResponse<List<Task>>> result = taskService.updateTask(updatedTask, taskId);

        // Assertions
        assertEquals(404, result.getStatusCodeValue());
        assertEquals("Task Not Found", result.getBody().getMessage());
    }

    @Test
    public void testDeleteTask_ExistingId() {
        // Mock data
        Long taskId = 1L;
        when(taskRepo.findById(taskId)).thenReturn(Optional.of(new Task()));

        // Call the service method
        ResponseEntity<ApiResponse<List<Void>>> result = taskService.deleteTask(taskId);

        // Assertions
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Task Deleted", result.getBody().getMessage());
    }

    @Test
    public void testDeleteTask_NonExistingId() {
        // Mock data
        Long taskId = 2L;
        when(taskRepo.findById(taskId)).thenReturn(Optional.empty());

        // Call the service method
        ResponseEntity<ApiResponse<List<Void>>> result = taskService.deleteTask(taskId);

        // Assertions
        assertEquals(404, result.getStatusCodeValue());
        assertEquals("Task Not Found", result.getBody().getMessage());
    }
}