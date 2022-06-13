// TODO
// 1. Read Collection - GET /tasks
// 2. Read Item - GET /tasks/{id}
// 3. Create - POST /tasks
// 4. Update - PUT/PATCH /tasks/{id}
// 5. Delete - DELETE /tasks/{id}

package com.codesom.demo.controllers;

import com.codesom.demo.application.TaskService;
import com.codesom.demo.domain.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private final TaskService taskService;
//    private List<Task> tasks = new ArrayList<>();
//    private Long newId = 0L;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 목록
    @GetMapping
    public List<Task> list() {
        return taskService.getTasks();
//        return tasks;
    }

    // 낱개로 읽어오기
    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        return taskService.getTask(id);
//        return findTask(id);
    }

    // 등록
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        return taskService.createTask(task);
//        task.setId(generateId());
//        tasks.add(task);
//
//        return task;
    }

    @PutMapping("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    // 업데이트
    @PatchMapping("{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task Task) {
        return taskService.updateTask(id, Task);
//        Task task = findTask(id);
//        task.setTitle(source.getTitle());
//
//        return task;
    }

    // delete
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
//        Task task = findTask(id);
//
//        tasks.remove(task);
//
//        return ResponseEntity.noContent().build();
    }

//    private Task findTask(Long id) {
//        return tasks.stream().filter(task -> task.getId().equals(id))
//                .findFirst()
//                .orElseThrow(() -> new TaskNotFoundException(id));
//    }

//    private Long generateId() {
//        newId += 1;
//        return newId;
//    }
}
