package com.codesom.demo.controllers;

import com.codesom.demo.TaskNotFoundException;
import com.codesom.demo.application.TaskService;
import com.codesom.demo.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// 아래 메서드 실행은 랜덤이다. (BeforeEach 제외)
// TaskService  가 올바르다는 전제
class TaskControllerTest {

    // 여기에 new 사용 시 초기화가 안되기 떄문에 별도의 메소드로 빼서 사용
    private TaskController controller;

    // 가능한 것
    // 1. Real object
    // 2. Mock object => 타입이 필요함
    // 3. Spy -> Proxy pattern => 진짜 오브젝트가 필요함
    private TaskService taskService;

    // 각각 실행될 때마다 사용
    @BeforeEach
    void setUp() {
//        taskService = spy(new TaskService());
        taskService = mock(TaskService.class);

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Test1");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
        given(taskService.updateTask(eq(100L), any(Task.class))).willThrow(new TaskNotFoundException(100L));
        given(taskService.deleteTask(100L)).willThrow(new TaskNotFoundException(100L));

        controller = new TaskController(taskService);
    }

    @Test
    void listWithoutTasks() {
        // TODO : service -> returns empty list
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        // taskService.getTasks
        // Controller -> Spy -> Real Object
        assertThat(controller.list()).isEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void listWithSomeTasks() {
//        Task task = new Task();
//        task.setTitle("Test1");
//        controller.create(task);

        // TODO : service -> returns list that contains one task.
        assertThat(controller.list()).isNotEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void detailWithExistedId() {
        Task task = controller.detail(1L);

        assertThat(task).isNotNull();

    }

    @Test
    void detailWithNotExistedId() {
        assertThatThrownBy(() -> controller.detail(100L)).isInstanceOf(TaskNotFoundException.class);

//        Task task = controller.detail(100L);
//
//        assertThat(task).isNull();

    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test2");

        controller.create(task);

        verify(taskService).createTask(task);

//        Task task = new Task();
//        task.setTitle("Test1");
//        controller.create(task);
//
//        task.setTitle("Test2");
//        controller.create(task);
//
//        assertThat(controller.list()).hasSize(2);
//        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
//        assertThat(controller.list().get(0).getTitle()).isEqualTo("Test1");
//        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
//        assertThat(controller.list().get(1).getTitle()).isEqualTo("Test2");
    }

    @Test
    void updateExistedTask() {
        Task task = new Task();
        task.setTitle("Renamed task");

        controller.update(1L, task);

        verify(taskService).updateTask(1L, task);
    }

    @Test
    void updateNotExistedTask() {
        Task task = new Task();
        task.setTitle("Renamed task");

        assertThatThrownBy(() -> controller.update(100L, task)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteExistedTask() {
        controller.delete(1L);

        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteNotExistedTask() {
        assertThatThrownBy(() -> controller.delete(100L)).isInstanceOf(TaskNotFoundException.class);
    }
}