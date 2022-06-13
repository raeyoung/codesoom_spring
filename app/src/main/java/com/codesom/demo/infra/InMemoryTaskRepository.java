package com.codesom.demo.infra;

import com.codesom.demo.TaskNotFoundException;
import com.codesom.demo.domain.Task;
import com.codesom.demo.domain.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryTaskRepository implements TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    public List<Task> findAll() {
        return tasks;
    }

    public Optional<Task> findById(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id))
                .findFirst();
//                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task save(Task task) {
        task.setId(generateId());
        tasks.add(task);
        return task;
    }

    public void delete(Task task) {
        tasks.remove(task);
//        return task;
    }

    // 아이디 생성하기
    private Long generateId() {
        newId += 1;
        return newId;
    }
}
