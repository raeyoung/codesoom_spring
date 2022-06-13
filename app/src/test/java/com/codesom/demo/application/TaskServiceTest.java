package com.codesom.demo.application;

import com.codesom.demo.TaskNotFoundException;
import com.codesom.demo.domain.Task;
import com.codesom.demo.domain.TaskRepository;
import com.codesom.demo.infra.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class TaskServiceTest {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)

    private static final String TASK_TITLE = "test";
    private TaskService taskService;

    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);

        taskService = new TaskService(taskRepository);

        List<Task> tasks = new ArrayList<>();
        
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        
        tasks.add(task);

        given(taskRepository.findAll()).willReturn(tasks);

        given(taskRepository.findById(1L)).willReturn(Optional.of(task));
        given(taskRepository.findById(100L)).willReturn(Optional.empty());
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        verify(taskRepository).findAll();

        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
     }


    @Test
    void getTaskWithExistedId() {
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInValidId() {
        Task task = taskService.getTask(1L);
        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(100L);
    }

    @Test
    void createTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        verify(taskRepository).save(any(Task.class));
     }


    @Test
    void updateTaskExistedId() {
        Task source = new Task();
        source.setTitle("New Title");

        Task task = taskService.updateTask(1L, source);

        verify(taskRepository).findById(1L);

        assertThat(task.getTitle()).isEqualTo("New Title");
    }

    @Test
    void updateTaskNotExistedId() {
        Task source = new Task();
        source.setTitle("New Title");

        assertThatThrownBy(() -> taskService.updateTask(100L, source)).isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(100L);
    }

     @Test
     void deleteTaskWithExistedId() {
         taskService.deleteTask(1L);

         verify(taskRepository).findById(1L);
         verify(taskRepository).delete(any(Task.class));
     }

    @Test
    void deleteTaskWithNotExistedId() {
        assertThatThrownBy(() -> taskService.deleteTask(100L)).isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(100L);
    }


    @Test
    void getTask() {
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);
        //  ???
        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
     }

}

