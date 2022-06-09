package com.codesom.demo.application;

import com.codesom.demo.TaskNotFoundException;
import com.codesom.demo.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)

    private static final String TASK_TITLE = "test";
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
     }


    @Test
    void getTaskWithValidId() {
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInValidId() {
        Task task = taskService.getTask(1L);
        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void getTask() {
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);
        //  ???
        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
     }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
     }

    @Test
    void updateTaskExistedId() {
        Task source = new Task();
        source.setTitle("New Title");
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo("New Title");
    }

    @Test
    void updateTaskNotExistedId() {
        Task source = new Task();
        source.setTitle("New Title");

        assertThatThrownBy(() -> taskService.updateTask(100L, source)).isInstanceOf(TaskNotFoundException.class);
    }

     @Test
     void deleteTaskWithExistedId() {
         int oldSize = taskService.getTasks().size();

         taskService.deleteTask(1L);

         int newSize = taskService.getTasks().size();

         assertThat(oldSize - newSize).isEqualTo(1);
     }

    @Test
    void deleteTaskWithNotExistedId() {
        assertThatThrownBy(() -> taskService.deleteTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }
}