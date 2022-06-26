package com.codesom.demo.application;

import com.codesom.demo.errors.TaskNotFoundException;
import com.codesom.demo.domain.Task;
import com.codesom.demo.domain.TaskRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskService {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)

    private final TaskRepository taskRepository;
    // TaskRepository 로 이동
//    private List<Task> tasks = new ArrayList<>();
//    private Long newId = 0L;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 목록 가져오기
    public List<Task> getTasks() {
        return taskRepository.findAll();
//        return tasks;
    }

    // todo 가져오기
    public Task getTask(Long id) {
//        return tasks.stream().filter(task -> task.getId().equals(id))
//                .findFirst()
//                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // 등록하기
    public Task createTask(Task source) {
        Task task = new Task();

        task.setTitle(source.getTitle());
//        tasks.add(task);
        return taskRepository.save(task);
//        return task;
    }

    // 업데이트
    public Task updateTask(Long id, Task source) {
//        Task task = getTask(id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(source.getTitle());
        return task;
    }

    // 삭제하기
    public Task deleteTask(Long id) {
//        Task task = getTask(id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
//        tasks.remove(task);
        return task;
    }

//    // 아이디 생성하기
//    private Long generateId() {
//        newId += 1;
//        return newId;
//    }

}
