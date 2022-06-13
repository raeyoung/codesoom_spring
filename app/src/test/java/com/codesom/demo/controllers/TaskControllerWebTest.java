// 1. Read Collection - GET /tasks
// 2. Read Item - GET /tasks/{id}
// 3. Create - POST /tasks
// 4. Update - PUT/PATCH /tasks/{id}
// 5. Delete - DELETE tasks/{id}
// TaskService  가 올바르다는 전제

package com.codesom.demo.controllers;

import com.codesom.demo.TaskNotFoundException;
import com.codesom.demo.application.TaskService;
import com.codesom.demo.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

// 일반 테스트에 비해 Spring에서 직접 테스트하는 것이기 때문에 더 정확하지만 오래걸린다

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    // 실제로 사용하는 게 아니라 가짜로 만들어지는 객체
    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle("Test Task");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(1L)).willReturn(task);

        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));

        given(taskService.updateTask(eq(100L), any(Task.class))).willThrow(new TaskNotFoundException(100L));

        given(taskService.deleteTask(100L)).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Task")));

        verify(taskService).getTasks();
    }

    @Test
    void detailWithValidId() throws Exception {
//        Task task = new Task();
//        taskService.createTask(task);
        mockMvc.perform(get("/tasks/1")).andExpect(status().isOk());

        verify(taskService).getTask(1L);
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100")).andExpect(status().isNotFound());

        verify(taskService).getTask(100L);
    }

    @Test
    void createTask() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Task\"}"))
                .andExpect(status().isCreated());

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    void updateExistedTask() throws Exception {
        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Renamed Task\"}"))
                .andExpect(status().isOk());

        verify(taskService).updateTask(eq(1L), any(Task.class));
    }

    @Test
    void updateNotExistedTask() throws Exception {
        mockMvc.perform(put("/tasks/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Renamed Task\"}"))
                .andExpect(status().isNotFound());

        verify(taskService).updateTask(eq(100L), any(Task.class));
    }

    @Test
    void deleteExistedTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteNotExistedTask() throws Exception {
        mockMvc.perform(delete("/tasks/100"))
                .andExpect(status().isNotFound());

        verify(taskService).deleteTask(100L);
    }
}
