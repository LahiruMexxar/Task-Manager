package com.example.Task.Manager.Repository;

import com.example.Task.Manager.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task,Long> {
    boolean existsByName(String title);
}
