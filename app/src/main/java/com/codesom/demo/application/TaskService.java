package com.codesom.demo.application;

import com.codesom.demo.TaskNotFoundException;
import com.codesom.demo.models.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    // 목록 가져오기
    public List<Task> getTasks() {
        return tasks;
    }

    // todo 가져오기
    public Task getTask(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // 등록하기
    public Task createTask(Task source) {
        Task task = new Task();

        task.setId(generateId());
        task.setTitle(source.getTitle());
        tasks.add(task);

        return task;
    }

    // 업데이트
    public Task updateTask(Long id, Task source) {
        Task task = getTask(id);
        task.setTitle(source.getTitle());
        return task;
    }

    // 삭제하기
    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(task);
        return task;
    }

    // 아이디 생성하기
    private Long generateId() {
        newId += 1;
        return newId;
    }

}
