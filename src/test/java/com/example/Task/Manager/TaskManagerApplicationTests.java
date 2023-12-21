package com.example.Task.Manager;

import com.example.Task.Manager.Models.Task;
import com.example.Task.Manager.Repository.TaskRepo;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TaskManagerApplicationTests {

	private static MySQLContainer mySQLContainer = new MySQLContainer<>();
	@Autowired
	private TaskRepo taskRepo;
	@Autowired
	private MockMvc mockMvc;
	//given when then format - BDD style
	@Test
	public void givenTasks_whenGetAllTask_Api_thenListOfTasks() throws Exception {
		//given
		List<Task> taskList = List.of(Task.builder().title("Task 1").description("Descrip 1").status("Done").build(),
				Task.builder().title("Task 2").description("Descrip 2").status("In Progress").build(),
		        Task.builder().title("Task 3").description("Descrip 3").status("To Do").build());
		taskRepo.saveAll(taskList);
		//when
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/task"));
		//then
		response.andExpect(MockMvcResultMatchers.status().isOk());
		response.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(taskList.size())));
	}

}
